/**
 * 
 */
package it.freshminutes.oceanrunner.modules.parameterized;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;
import it.freshminutes.oceanrunner.modules.concurrent.ConcurrentOceanModule;
import it.freshminutes.oceanrunner.modules.concurrent.annotations.OceanRunTestsInDedicatedThreads;
import it.freshminutes.oceanrunner.modules.parameterized.annotations.OceanParameterizedVariable;
import it.freshminutes.oceanrunner.modules.repeat.OceanRunRepeat;
import it.freshminutes.oceanrunner.modules.repeat.RepeatOceanModule;

import java.util.Arrays;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Eric Vialle
 */
@RunWith(OceanRunner.class)
@OceanModulesToUse({ParameterizedOceanModule.class, RepeatOceanModule.class, ConcurrentOceanModule.class})
@OceanRunTestsInDedicatedThreads()
public class TestParameterizedRepeatConcurrentOceanModule {

	@OceanParameterizedVariable
	String validEmail;
	
	private static int counter = 0;
	private static int counter2 = 0;


	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ "a@a.pl" /* more params here */},
				{ "a1@a.pl" /* more params here */},
				{ "a2@a.pl" /* more params here */},
				{ "a3@a.pl" /* more params here */},
				{ "exmaple@example.com", /* more params here */}, });
	}

	@Test
	public void testIsValidEmail() throws Exception {
		Assert.assertNotNull(validEmail);
		counter ++;
		System.out.println(Thread.currentThread().getName() + " testIsValidEmail " + counter);
	}
	
	@Test
	@OceanRunRepeat(3)
	public void testIsValidEmail2() throws Exception {
		Assert.assertNotNull(validEmail);
		counter2 ++;
		System.out.println(Thread.currentThread().getName() + " testIsValidEmail2 " + counter2);

	}
	
	
	
	@AfterClass
	public static void afterClass() {
		Assert.assertEquals(5, counter);
		Assert.assertEquals(15, counter2);
	}
}
