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
package it.freshminutes.oceanrunner.tests;
import static org.junit.Assert.assertTrue;
import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;
import it.freshminutes.oceanrunner.modules.categories.CategoryOceanModule;
import it.freshminutes.oceanrunner.tests.categories.TestCategoryOceanModule;

import java.util.Random;

import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;


@RunWith(OceanRunner.class)
@OceanModulesToUse({ CategoryOceanModule.class})
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
