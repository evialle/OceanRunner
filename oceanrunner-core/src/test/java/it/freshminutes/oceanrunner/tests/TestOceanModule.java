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
package it.freshminutes.oceanrunner.tests;

import static org.junit.Assert.assertTrue;
import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;
import it.freshminutes.oceanrunner.modules.builtin.DemoOceanModule;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Demo of OceanModule
 * 
 * @author Eric Vialle
 */
@RunWith(OceanRunner.class)
@OceanModulesToUse(DemoOceanModule.class)
public class TestOceanModule {

	@Test
	public void myTest() {
		assertTrue(true);
	}

	@Test
	public void myTestAgain() {
		assertTrue(true);
	}
}
