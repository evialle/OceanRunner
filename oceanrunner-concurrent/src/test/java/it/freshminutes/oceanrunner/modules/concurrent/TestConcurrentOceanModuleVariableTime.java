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
package it.freshminutes.oceanrunner.modules.concurrent;

import static org.junit.Assert.assertTrue;
import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;

import java.util.Random;

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
@OceanRunTestsInDedicatedThreads()
public class TestConcurrentOceanModuleVariableTime {

	private void waitRandomTime() throws InterruptedException {
		Random r = new Random();
		long timeToWait = r.nextInt(20) * 1000L;
		System.out.println(Thread.currentThread().getName() + "waiting " + timeToWait + "ms");
		synchronized (this) {
			wait(timeToWait);
		}
	}

	@Test
	public void testTrue() throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + " testTrue");
		waitRandomTime();
		assertTrue(true);
	}

	@Test
	@OceanRunConcurrencyForbidden
	public void testFalse() throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + " testFalse");
		waitRandomTime();

		assertTrue(false);
	}


	@Test
	@OceanRunConcurrencyForbidden
	public void testTrueMonoThread() throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + " testTrueMonoThread");
		waitRandomTime();

		assertTrue(true);
	}

	@Test
	@OceanRunConcurrencyForbidden
	public void testFalseMonoThread() throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + " testFalseMonoThread");
		waitRandomTime();

		assertTrue(false);
	}

	@Test
	public void testTrueAgainMonoThread() throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + " testTrueMonoThread");
		waitRandomTime();

		assertTrue(true);
	}

	@Test
	public void testAgainFalseMonoThread() throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + " testFalseMonoThread");
		waitRandomTime();

		assertTrue(false);
	}

}
