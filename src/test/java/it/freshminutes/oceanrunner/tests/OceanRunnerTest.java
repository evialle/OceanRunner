package it.freshminutes.oceanrunner.tests;
import static org.junit.Assert.assertTrue;
import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;
import it.freshminutes.oceanrunner.annotations.OceanRunTestsInDedicatedThreads;
import it.freshminutes.oceanrunner.modules.builtin.CategoryOceanModule;
import it.freshminutes.oceanrunner.modules.builtin.ConcurrentOceanModule;
import it.freshminutes.oceanrunner.modules.builtin.TestModuleOceanModule;
import it.freshminutes.oceanrunner.tests.categories.TestCategoryOceanModule;

import java.util.Random;

import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;


@RunWith(OceanRunner.class)
@OceanRunTestsInDedicatedThreads(value = false)
@OceanModulesToUse({ TestModuleOceanModule.class, CategoryOceanModule.class, ConcurrentOceanModule.class})
@Category(TestCategoryOceanModule.class)
public class OceanRunnerTest {

	@Test()
	@Category(TestCategoryOceanModule.class)
	public void voidMyMethod() {
		printAndWait();
	}
	
	@Test()
	public void voidMyMethod2() {
		printAndWait();

	}
	
	@Test()
	public void voidMyMethodFail() {	
		printAndWait();
		assertTrue(false);
	}
	
	@Test()
	public void voidMyMethodAssumptionFail() {
		printAndWait();

		Assume.assumeTrue(false);
	}
	
	@Ignore
	@Test()
	public void voidMyMethodIgnore() {
		printAndWait();
		assertTrue(false);
	}
	
	 void printAndWait() {
	        int w = new Random().nextInt(1000);
	        System.out.println(String.format("[%s] %s", 
	        		Thread.currentThread().getName(), 
	        		new Throwable().getStackTrace()[1].getMethodName()));
	        try {
				Thread.sleep(w+1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	
}
