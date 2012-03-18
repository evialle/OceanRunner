package it.freshminutes.oceanrunner;

import org.junit.runners.model.FrameworkMethod;

public interface OceanRunnerScheduler {

	/**
	 * Schedule a child statement to run
	 */
	public void schedule(Runnable childStatement, FrameworkMethod each);

	/**
	 * Override to implement any behavior that must occur after all children
	 * have been scheduled (for example, waiting for them all to finish)
	 */
	void finished();
}
