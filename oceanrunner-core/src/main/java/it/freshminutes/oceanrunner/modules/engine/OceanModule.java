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

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

/**
 * Define an OceanModule. An OceanModule could be compared to a JUnit Runner.
 * But when you may have only one JUnit Runner for your class, you may use
 * multiple OceanModules.
 * 
 * @author Eric Vialle
 * 
 */
public abstract class OceanModule {


	public void doBeforeAllTestedMethods(final OceanRunner oceanRunner, final Class<?> klass) throws OceanModuleException {
	}

	public void doAfterAllTestedMethods(final OceanRunner oceanRunner, final Class<?> klass) throws OceanModuleException {
	}
	
	public void doBeforeEachTestedMethod(final OceanRunner oceanRunner) throws OceanModuleException {
	}
	
	public void doAfterEachTestedMethod(final OceanRunner oceanRunner, final Description description) throws OceanModuleException {
	}
	
	public void doAfterEachFailedMethod(final OceanRunner oceanRunner, final Failure failure) throws OceanModuleException {
	}
	
	public void doAfterEachIgnoredMethod(final OceanRunner oceanRunner, final Description description) throws OceanModuleException {
	}
	
	public void doAfterEachAssumptionFailedMethod(final OceanRunner oceanRunner, final Failure failure) throws OceanModuleException {
	}
	
	public void doAfterEachAssertionFailedMethod(final OceanRunner oceanRunner, final Failure failure) throws OceanModuleException {
	}
	
}
