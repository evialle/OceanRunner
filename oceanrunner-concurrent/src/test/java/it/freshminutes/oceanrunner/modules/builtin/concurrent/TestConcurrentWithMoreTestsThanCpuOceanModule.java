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
package it.freshminutes.oceanrunner.modules.builtin.concurrent;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;
import it.freshminutes.oceanrunner.modules.builtin.ConcurrentOceanModule;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(OceanRunner.class)
@OceanModulesToUse(ConcurrentOceanModule.class)
@OceanRunTestsInDedicatedThreads(value = true)
public class TestConcurrentWithMoreTestsThanCpuOceanModule {

	@Test
	public void testConcurrent1() {
		System.out.println(Thread.currentThread().getName() + " 1");
	}

	@Test
	public void testConcurrent2() {
		System.out.println(Thread.currentThread().getName() + " 2");

	}

	@Test
	public void testConcurrent3() {
		System.out.println(Thread.currentThread().getName() + " 3");

	}

	@Test
	public void testConcurrent4() {
		System.out.println(Thread.currentThread().getName() + " 4");

	}

	@Test
	public void testConcurrent5() {
		System.out.println(Thread.currentThread().getName() + " 5");

	}

	@Test
	public void testConcurrent6() {
		System.out.println(Thread.currentThread().getName() + " 6");

	}

	@Test
	public void testConcurrent7() {
		System.out.println(Thread.currentThread().getName() + " 7");

	}

	@Test
	public void testConcurrent8() {
		System.out.println(Thread.currentThread().getName() + " 8");
	}

	@Test
	public void testConcurrent9() {
		System.out.println(Thread.currentThread().getName() + " 9");
	}

	@Test
	public void testConcurrent10() {
		System.out.println(Thread.currentThread().getName() + " 10");
	}

	@Test
	public void testConcurrent11() {
		System.out.println(Thread.currentThread().getName() + " 11");
	}

	@Test
	public void testConcurrent12() {
		System.out.println(Thread.currentThread().getName() + " 12");
	}

	@Test
	public void testConcurrent13() {
		System.out.println(Thread.currentThread().getName() + " 13");
	}

	@Test
	public void testConcurrent14() {
		System.out.println(Thread.currentThread().getName() + " 14");
	}
}
