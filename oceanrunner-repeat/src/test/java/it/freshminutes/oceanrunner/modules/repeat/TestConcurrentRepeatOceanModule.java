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

package it.freshminutes.oceanrunner.modules.repeat;

import static org.junit.Assert.*;
import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;
import it.freshminutes.oceanrunner.modules.concurrent.ConcurrentOceanModule;
import it.freshminutes.oceanrunner.modules.concurrent.OceanRunTestsInDedicatedThreads;
import it.freshminutes.oceanrunner.modules.repeat.RepeatOceanModule;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(OceanRunner.class)
@OceanModulesToUse({RepeatOceanModule.class, ConcurrentOceanModule.class})
@OceanRunTestsInDedicatedThreads
public class TestConcurrentRepeatOceanModule {

	static int repeatMe;
	
	static int repeatMeFailed;

	
	@Test
	@OceanRunRepeat
	public void testConcurrent() {
		System.out.println(Thread.currentThread().getName() + " 1 - Repeat" + repeatMe++);
		assertTrue(repeatMe <= 2);
	}
	
	@Test
	@OceanRunRepeat(5)
	public void testConcurrentFailed() {
		
		System.out.println(Thread.currentThread().getName() + " 1 - Repeat Failed" + repeatMeFailed++);
		assertTrue(repeatMeFailed <= 5);
	}

	@Test
	public void testConcurrent2() {
		System.out.println(Thread.currentThread().getName() + " 2");
	}

	@Test
	public void testConcurrent3() {
		System.out.println(Thread.currentThread().getName() + " 3");
	}
}
