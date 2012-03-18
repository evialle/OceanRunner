package it.freshminutes.oceanrunner.tests;

import static org.junit.Assert.assertTrue;
import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;
import it.freshminutes.oceanrunner.modules.builtin.ConcurrentOceanModule;
import it.freshminutes.oceanrunner.modules.builtin.concurrent.OceanRunConcurrencyForbidden;
import it.freshminutes.oceanrunner.modules.builtin.concurrent.OceanRunTestsInDedicatedThreads;

import org.junit.Test;
import org.junit.runner.RunWith;

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
