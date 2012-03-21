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

import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;

/**
 * OceanModule dedicated to run test in different threads. Custom the process of
 * that module by setting @OceanRunTestsInDedicatedThreads()
 * 
 * @author Eric Vialle
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
		if ((klass.getAnnotation(OceanRunTestsInDedicatedThreads.class) != null) && (klass.getAnnotation(OceanRunTestsInDedicatedThreads.class).value())) {
			oceanRunner.setScheduler(new OceanRunnerScheduler() {

				private ExecutorService executorConcurrentService;
				private ExecutorService executorMonoThreadService;

				private CompletionService<Void> completionConcurrentService;
				private CompletionService<Void> completionMonoThreadService;

				private Queue<Future<Void>> multithreadTasks = new LinkedList<Future<Void>>();
				private Queue<Future<Void>> monothreadTasks = new LinkedList<Future<Void>>();

				@Override
				public void schedule(final Runnable childStatement, final FrameworkMethod method) {

					OceanRunConcurrencyForbidden annotation = method.getAnnotation(OceanRunConcurrencyForbidden.class);

					if (annotation == null) {
						// Multi Thread
						multithreadTasks.offer(getCompletionConcurrentService().submit(childStatement, null));
					} else {
						// Mono Thread
						monothreadTasks.offer(getCompletionMonoThreadService().submit(childStatement, null));
					}

				}

				/** Define the number of threads to use. */
				private int getBestNbOfThreads(final OceanRunTestsInDedicatedThreads annotation) {

					int nbOfThreads = annotation.threads();

					int threadsOptim;
					if (nbOfThreads > 0) {
						threadsOptim = nbOfThreads;
					} else {
						// Compare the nb of methods to test and the available
						// cpu
						int nbOfMethodToTest = oceanRunner.getTestClass().getAnnotatedMethods(Test.class).size();
						int nbOfAvailableCpu = Runtime.getRuntime().availableProcessors();

						if (nbOfMethodToTest > nbOfAvailableCpu) {
							threadsOptim = nbOfAvailableCpu;
							// Keep one thread for the mono thread
							if (threadsOptim > 2) {
								threadsOptim--;
							}
						} else {
							threadsOptim = nbOfMethodToTest;
						}
					}

					return threadsOptim;
				}

				/** Lazy loading of the ComplemetionConcurrentService. */
				private CompletionService<Void> getCompletionConcurrentService() {
					if (completionConcurrentService == null) {
						int nbOfThreads = getBestNbOfThreads(klass.getAnnotation(OceanRunTestsInDedicatedThreads.class));
						executorConcurrentService = Executors.newFixedThreadPool(nbOfThreads);
						completionConcurrentService = new ExecutorCompletionService<Void>(executorConcurrentService);
					}
					return completionConcurrentService;
				}

				/** Lazy loading of the completionMonoThreadService. */
				private CompletionService<Void> getCompletionMonoThreadService() {
					if (completionMonoThreadService == null) {
						executorMonoThreadService = Executors.newSingleThreadExecutor();
						completionMonoThreadService = new ExecutorCompletionService<Void>(executorMonoThreadService);
					}
					return completionMonoThreadService;
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
						while (!multithreadTasks.isEmpty()) {
							multithreadTasks.poll().cancel(true);
						}
						if (executorConcurrentService != null) {
							executorConcurrentService.shutdownNow();
						}

						while (!monothreadTasks.isEmpty()) {
							monothreadTasks.poll().cancel(true);
						}
						if (executorMonoThreadService != null) {
							executorMonoThreadService.shutdownNow();
						}
					}
				}
			});
		}

	}

}
