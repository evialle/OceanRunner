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
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;
import it.freshminutes.oceanrunner.utils.ReflectionUtils;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.junit.runner.Description;

/**
 * Convert easily JUnit3 module in JUnit4 modules (sometimes NPE due to huge
 * reflections in test classes).
 * 
 * @author Eric Vialle
 * 
 */
public class JUnit3AdapterOceanModule extends OceanModule {

	@Override
	public void doBeforeEachTestedMethod(OceanRunner oceanRunner)
			throws OceanModuleException {
		if (oceanRunner.getTarget() instanceof TestCase) {
			try {
				Method m = ReflectionUtils.invokeHeritedMethod(
						oceanRunner.getTarget(), "setUp");
				m.setAccessible(true);
				m.invoke(oceanRunner.getTarget());
			} catch (Exception e) {
				throw new OceanModuleException(e);
			}
		}
	}

	@Override
	public void doAfterEachTestedMethod(OceanRunner oceanRunner,
			Description description) throws OceanModuleException {
		if (oceanRunner.getTarget() instanceof TestCase) {
			try {
				Method m = ReflectionUtils.invokeHeritedMethod(
						oceanRunner.getTarget(), "tearDown");

				m.setAccessible(true);
				m.invoke(oceanRunner.getTarget());
			} catch (Exception e) {
				throw new OceanModuleException(e);
			}
		}
	}

}
