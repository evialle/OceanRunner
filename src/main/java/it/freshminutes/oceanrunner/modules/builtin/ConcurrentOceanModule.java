package it.freshminutes.oceanrunner.modules.builtin;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanRunTestsInDedicatedThreads;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runners.model.RunnerScheduler;

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
			oceanRunner.setScheduler(new RunnerScheduler() {

				int nbOfThreads = klass.getAnnotation(OceanRunTestsInDedicatedThreads.class).threads();

				ExecutorService executorService = Executors.newFixedThreadPool(nbOfThreads);

				CompletionService<Void> completionService = new ExecutorCompletionService<Void>(executorService);
				Queue<Future<Void>> tasks = new LinkedList<Future<Void>>();

				@Override
				public void schedule(Runnable childStatement) {
					tasks.offer(completionService.submit(childStatement, null));
				}

				@Override
				public void finished() {
					try {
						while (!tasks.isEmpty())
							tasks.remove(completionService.take());
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					} finally {
						while (!tasks.isEmpty())
							tasks.poll().cancel(true);
						executorService.shutdownNow();
					}
				}
			});
		}

	}

	@Override
	public void doAfterAllTestedMethods(final OceanRunner oceanRunner, Class<?> klass) {

	}

	@Override
	public void doAfterEachIgnoredMethod(final OceanRunner oceanRunner, final Description description) {

	}

	@Override
	public void doBeforeEachTestedMethod(OceanRunner oceanRunnert) {

	}

	@Override
	public void doAfterEachTestedMethod(OceanRunner oceanRunner, Description description) {
	}

	@Override
	public void doAfterEachFailedMethod(OceanRunner oceanRunner, Failure failure) {

	}

	@Override
	public void doAfterEachAssumptionFailedMethod(OceanRunner oceanRunner, final Failure failure) {
	}

}
