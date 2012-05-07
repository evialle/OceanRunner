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
import it.freshminutes.oceanrunner.modules.engine.OceanModule;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

/**
 * 
 * 
 * @author Eric Vialle
 */
public class DemoOceanModule extends OceanModule {

	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modulesengine.OceanModule#doBeforeAllTestedMethods(it.freshminutes.oceanrunner.OceanRunner, java.lang.Class)
	 */
	@Override
	public void doBeforeAllTestedMethods(OceanRunner oceanRunner, Class<?> klass) {
		System.out.println("Called doBeforeAllTestedMethods");

	}

	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modulesengine.OceanModule#doAfterAllTestedMethods(it.freshminutes.oceanrunner.OceanRunner, java.lang.Class)
	 */
	@Override
	public void doAfterAllTestedMethods(OceanRunner oceanRunner, Class<?> klass) {
		System.out.println("Called doAfterAllTestedMethods");
	}

	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modulesengine.OceanModule#doBeforeEachTestedMethod(it.freshminutes.oceanrunner.OceanRunner)
	 */
	@Override
	public void doBeforeEachTestedMethod(OceanRunner oceanRunner) {
		System.out.println(Thread.currentThread().getName() + " Called doBeforeEachTestedMethod");
	}

	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modulesengine.OceanModule#doAfterEachTestedMethod(it.freshminutes.oceanrunner.OceanRunner)
	 */
	@Override
	public void doAfterEachTestedMethod(OceanRunner oceanRunner, Description description) {
		System.out.println(Thread.currentThread().getName() + " Called doAfterEachTestedMethod");
	}



	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modulesengine.OceanModule#doAfterEachIgnoredMethod(it.freshminutes.oceanrunner.OceanRunner)
	 */
	@Override
	public void doAfterEachIgnoredMethod(OceanRunner oceanRunner, final Description description) {
		System.out.println(Thread.currentThread().getName() + " Called doAfterEachIgnoredMethod");
	}


	@Override
	public void doAfterEachFailedMethod(OceanRunner oceanRunner, Failure failure) {
		System.out.println(Thread.currentThread().getName() + " Called doAfterEachFailedMethod");
	}

	@Override
	public void doAfterEachAssumptionFailedMethod(OceanRunner oceanRunner,
			final Failure failure) {
		System.out.println(Thread.currentThread().getName() + " Called doAfterEachAssumptionFailedMethod");

		
	}

}
