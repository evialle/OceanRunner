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

import static org.junit.Assert.assertTrue;
import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;
import it.freshminutes.oceanrunner.modules.builtin.ConcurrentOceanModule;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * Should display two pools, one monothreaded, one multithreaded:
 * 
 * <pre>
 * pool-2-thread-1 testTrueMonoThread
 * pool-1-thread-3 testFalse
 * pool-1-thread-4 testTrue
 * pool-1-thread-1 testTrue
 * pool-2-thread-1 testFalseMonoThread
 * pool-1-thread-2 testFalse
 * pool-1-thread-5 testFalse
 * pool-1-thread-4 testTrue
 * pool-2-thread-1 testTrueMonoThread
 * pool-2-thread-1 testFalseMonoThread
 * pool-2-thread-1 testTrueMonoThread
 * pool-2-thread-1 testFalseMonoThread
 * </pre>
 * 
 * 
 * @author Eric Vialle
 * 
 */
@RunWith(OceanRunner.class)
@OceanModulesToUse(ConcurrentOceanModule.class)
@OceanRunTestsInDedicatedThreads(threads = 5)
public class TestConcurrentOceanModule {

	@Test
	@OceanRunConcurrencyForbidden
	public void testTrue() {
		System.out.println(Thread.currentThread().getName() + " testTrue");

		assertTrue(true);
	}

	@Test
	@OceanRunConcurrencyForbidden
	public void testFalse() {
		System.out.println(Thread.currentThread().getName() + " testFalse");

		assertTrue(false);
	}


	@Test
	@OceanRunConcurrencyForbidden
	public void testTrueMonoThread() {
		System.out.println(Thread.currentThread().getName() + " testTrueMonoThread");

		assertTrue(true);
	}

	@Test
	@OceanRunConcurrencyForbidden
	public void testFalseMonoThread() {
		System.out.println(Thread.currentThread().getName() + " testFalseMonoThread");

		assertTrue(false);
	}

	@Test
	@OceanRunConcurrencyForbidden
	public void testTrueAgainMonoThread() {
		System.out.println(Thread.currentThread().getName() + " testTrueMonoThread");

		assertTrue(true);
	}

	@Test
	@OceanRunConcurrencyForbidden
	public void testAgainFalseMonoThread() {
		System.out.println(Thread.currentThread().getName() + " testFalseMonoThread");

		assertTrue(false);
	}

}
