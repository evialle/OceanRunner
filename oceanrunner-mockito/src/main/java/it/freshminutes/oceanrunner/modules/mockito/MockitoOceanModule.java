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
/**
 * 
 */
package it.freshminutes.oceanrunner.modules.mockito;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;

import org.junit.runner.Description;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author Eric Vialle
 * 
 */
public class MockitoOceanModule extends OceanModule {

	private boolean firstRun = true;

	@Override
	public void doBeforeEachTestedMethod(final OceanRunner oceanRunner) throws OceanModuleException {
		synchronized (this) {
			if (this.firstRun) {
				this.firstRun = false;
				MockitoAnnotations.initMocks(oceanRunner.getTarget());
			}
		}

	}

	@Override
	public void doAfterEachTestedMethod(final OceanRunner oceanRunner, final Description description) throws OceanModuleException {
		Mockito.validateMockitoUsage();
	}

}
