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

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;
import it.freshminutes.oceanrunner.modules.builtin.concurrent.OceanRunTestsInDedicatedThreads;
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
