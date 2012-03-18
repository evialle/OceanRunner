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
package it.freshminutes.oceanrunner.modules.engine;

import org.junit.runners.model.FrameworkMethod;

/**
 * OceanRunnerScheduler is an enhanced RunnerScheduler. You may have access to
 * the FrameworkMethod while scheduling a statement.
 * 
 * @author Eric Vialle
 */
public interface OceanRunnerScheduler {

	/**
	 * Schedule a child statement to run.
	 */
	public void schedule(Runnable childStatement, FrameworkMethod each);

	/**
	 * Override to implement any behavior that must occur after all children
	 * have been scheduled (for example, waiting for them all to finish)
	 */
	void finished();
}
