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
package it.freshminutes.oceanrunner.modules.statistics;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.freshminutes.oceanrunner.OceanRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(OceanRunner.class)
public class TestStatisticsOceanModule {

	@Test
	public void dummyTest() {
		assertTrue(true);
	}

	@Test
	public void dummyFailedAssumptionTest() {
		assertFalse("failed assumption...", true);
	}

	@Test
	public void dummyExceptionTest() throws Exception {
		throw new Exception("that works!");
	}
}
