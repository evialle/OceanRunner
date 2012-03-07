package it.freshminutes.oceanrunner.modules.builtin;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;
import it.freshminutes.oceanrunner.annotations.OceanRunTestsInDedicatedThreads;
import junit.framework.TestCase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(OceanRunner.class)
@OceanRunTestsInDedicatedThreads(value = false)
@OceanModulesToUse({ JUnit3AdapterOceanModule.class, CategoryOceanModule.class, ConcurrentOceanModule.class })
public class TestJUnit3AdapterOceanModule extends TestCase {
	
	
	private int value = 1;
	
	
	@BeforeClass
	public static void beforeClass() {
		System.out.println("This is beforeClass");
	}
		
	@AfterClass
	public static void afterClass() {
		System.out.println("This is afterClass");
	}
	
	/*
	 * If you enable before and after setup and after will be disabled
	 * 
	 * @Before public void before() { myPrint("This is before"); }
	 * 
	 * @After public void after() { myPrint("This is after"); }
	 */
		
	
	@Override
	public void setUp() {
		myPrint("This is a setup " + value);
		value++;
	}
	
	@Override
	public void tearDown() {
		myPrint("This is a tearDown" + value);
		value++;
	}

	
	@Test
	public void testMachine(){
		myPrint("Super test " + value);
		value++;
	}
	
	@Test
	public void testMachine2(){
		myPrint("Super test again " + value);
		value++;

	}

	private void myPrint(String str) {
		System.out.println("[" + Thread.currentThread().getName() + "-" + this + "] : " + str);
	}
}
