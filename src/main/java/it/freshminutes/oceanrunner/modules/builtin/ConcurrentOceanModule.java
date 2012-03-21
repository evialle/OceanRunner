/**
 * Copyright 2012 Eric P. Vialle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.freshminutes.oceanrunner.modules.builtin;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.modules.builtin.concurrent.OceanRunConcurrencyForbidden;
import it.freshminutes.oceanrunner.modules.builtin.concurrent.OceanRunTestsInDedicatedThreads;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;
import it.freshminutes.oceanrunner.modules.engine.OceanRunnerScheduler;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.runners.model.FrameworkMethod;

/**
 * OceanModule dedicated to run test in different threads. Custom the process of
 * that module by setting @OceanRunTestsInDedicatedThreads()
 * 
 * @author Eric Vialle
 * 
 */
public class ConcurrentOceanModule extends OceanModule {

	static final class NamedThreadFactory implements ThreadFactory {
		static final AtomicInteger poolNumber = new AtomicInteger(1);
		final AtomicInteger threadNumber = new AtomicInteger(1);
		final ThreadGroup group;

		NamedThreadFactory(String poolName) {
			group = new ThreadGroup(poolName + "-" + poolNumber.getAndIncrement());
		}

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(group, r, group.getName() + "-thread-" + threadNumber.getAndIncrement(), 0);
		}
	}

	@Override
	public void doBeforeAllTestedMethods(final OceanRunner oceanRunner, final Class<?> klass) {

		// Do we authorize the fact to be tested in dedicated threads?
		if (klass.getAnnotation(OceanRunTestsInDedicatedThreads.class).value()) {
			oceanRunner.setScheduler(new OceanRunnerScheduler() {

				private int nbOfThreads = klass.getAnnotation(OceanRunTestsInDedicatedThreads.class).threads();

				private ExecutorService executorConcurrentService = Executors.newFixedThreadPool(nbOfThreads);
				private ExecutorService executorMonoThreadService = Executors.newSingleThreadExecutor();

				private CompletionService<Void> completionConcurrentService = new ExecutorCompletionService<Void>(executorConcurrentService);
				private CompletionService<Void> completionMonoThreadService = new ExecutorCompletionService<Void>(executorMonoThreadService);

				private Queue<Future<Void>> multithreadTasks = new LinkedList<Future<Void>>();
				private Queue<Future<Void>> monothreadTasks = new LinkedList<Future<Void>>();


				@Override
				public void schedule(Runnable childStatement, FrameworkMethod method) {

					OceanRunConcurrencyForbidden annotation = method.getAnnotation(OceanRunConcurrencyForbidden.class);

					if (annotation == null) {
						// Multi Thread
						multithreadTasks.offer(completionConcurrentService.submit(childStatement, null));
					} else {
						// Mono Thread
						monothreadTasks.offer(completionMonoThreadService.submit(childStatement, null));
					}

				}

				@Override
				public void finished() {
					try {
						while (!multithreadTasks.isEmpty()) {
							multithreadTasks.remove(completionConcurrentService.take());
						}
						while (!monothreadTasks.isEmpty()) {
							monothreadTasks.remove(completionMonoThreadService.take());
						}
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					} finally {
						while (!multithreadTasks.isEmpty()){
							multithreadTasks.poll().cancel(true);
						}
						while (!monothreadTasks.isEmpty()) {
							monothreadTasks.poll().cancel(true);
						}
						executorConcurrentService.shutdownNow();
						executorMonoThreadService.shutdownNow();
					}
				}
			});
		}

	}

}
