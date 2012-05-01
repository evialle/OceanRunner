/**
 * Copyright 2012 Eric P. Vialle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package it.freshminutes.modules.arquilian;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;

import org.jboss.arquillian.test.spi.TestRunnerAdaptor;
import org.jboss.arquillian.test.spi.TestRunnerAdaptorBuilder;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

/**
 * Based on Arquilian Module from RedHat JBoss.
 * 
 * Not fully developped.
 * 
 * @author Eric Vialle
 */
public class OceanArquilianModule extends OceanModule {
	
	public void doBeforeAllTestedMethods(final OceanRunner oceanRunner, final Class<?> klass) throws OceanModuleException {
		TestRunnerAdaptor adaptor = TestRunnerAdaptorBuilder.build();
		try {
			// don't set it if beforeSuite fails
			adaptor.beforeSuite();
			ArquilianOceanState.testAdaptor(adaptor);
		} catch (Exception e) {
			// caught exception during BeforeSuite, mark this as failed
			ArquilianOceanState.caughtInitializationException(e);
			throw new OceanModuleException(e);
		}
	}
	
	/** Called after all the tested method (last method to be called). */
	public void doAfterAllTestedMethods(final OceanRunner oceanRunner, final Class<?> klass) throws OceanModuleException {
	}
	
	/** Called before each tested method. */
	public void doBeforeEachTestedMethod(final OceanRunner oceanRunner) throws OceanModuleException {
		if (ArquilianOceanState.hasInitializationException()) {
			throw new OceanModuleException("Arquillian has previously been attempted initialized, but failed. See cause for previous exception",
					ArquilianOceanState.getInitializationException());
		}
	}
	
	/** Called after each tested method (successful or not). */
	public void doAfterEachTestedMethod(final OceanRunner oceanRunner, final Description description) throws OceanModuleException {
		ArquilianOceanState.runnerFinished();
		
		try {
			if (ArquilianOceanState.isLastRunner()) {
				try {
					if (ArquilianOceanState.hasTestAdaptor()) {
						TestRunnerAdaptor adaptor = ArquilianOceanState.getTestAdaptor();
						adaptor.afterSuite();
						adaptor.shutdown();
					}
				} finally {
					ArquilianOceanState.clean();
				}
			}
		} catch (Exception e) {
			throw new OceanModuleException("Could not run @AfterSuite", e);
		}
	}
	
	/** Called after each failed tested method. */
	public void doAfterEachFailedMethod(final OceanRunner oceanRunner, final Failure failure) throws OceanModuleException {
	}
	
	/** Called after each ignored method. */
	public void doAfterEachIgnoredMethod(final OceanRunner oceanRunner, final Description description) throws OceanModuleException {
	}
	
	/** Called after each tested method with a failed assumption. */
	public void doAfterEachAssumptionFailedMethod(final OceanRunner oceanRunner, final Failure failure) throws OceanModuleException {
	}
	
	/** Called after each tested method with a failed assertion. */
	public void doAfterEachAssertionFailedMethod(final OceanRunner oceanRunner, final Failure failure) throws OceanModuleException {
	}
	
}
