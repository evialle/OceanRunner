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

import static org.junit.Assert.assertTrue;
import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;
import it.freshminutes.oceanrunner.modules.builtin.concurrent.OceanRunConcurrencyForbidden;
import it.freshminutes.oceanrunner.modules.builtin.concurrent.OceanRunTestsInDedicatedThreads;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * 
 * 
 * 
 * Should display two pools, one mono threaded, one multi threaded:
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
	public void testTrue() {
		System.out.println(Thread.currentThread().getName() + " testTrue");

		assertTrue(true);
	}

	@Test
	public void testFalse() {
		System.out.println(Thread.currentThread().getName() + " testFalse");

		assertTrue(false);
	}

	@Test
	public void testTrue2() {
		System.out.println(Thread.currentThread().getName() + " testTrue");

		assertTrue(true);
	}

	@Test
	public void testFalse2() {
		System.out.println(Thread.currentThread().getName() + " testFalse");

		assertTrue(false);
	}

	@Test
	public void testTrue3() {
		System.out.println(Thread.currentThread().getName() + " testTrue");

		assertTrue(true);
	}

	@Test
	public void testFalse3() {
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
	public void testTrueMonoThread1() {
		System.out.println(Thread.currentThread().getName() + " testTrueMonoThread");

		assertTrue(true);
	}

	@Test
	@OceanRunConcurrencyForbidden
	public void testFalseMonoThread1() {
		System.out.println(Thread.currentThread().getName() + " testFalseMonoThread");

		assertTrue(false);
	}

	@Test
	@OceanRunConcurrencyForbidden
	public void testTrueMonoThread2() {
		System.out.println(Thread.currentThread().getName() + " testTrueMonoThread");

		assertTrue(true);
	}

	@Test
	@OceanRunConcurrencyForbidden
	public void testFalseMonoThread2() {
		System.out.println(Thread.currentThread().getName() + " testFalseMonoThread");

		assertTrue(false);
	}

}
