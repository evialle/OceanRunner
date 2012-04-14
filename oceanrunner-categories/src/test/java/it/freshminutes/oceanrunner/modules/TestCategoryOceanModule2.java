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
package it.freshminutes.oceanrunner.modules;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;
import it.freshminutes.oceanrunner.modules.categories.CategoryOceanModule;
import it.freshminutes.oceanrunner.tests.categories.CategoryToDoNotUse;
import it.freshminutes.oceanrunner.tests.categories.CategoryToUse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@RunWith(OceanRunner.class)
@OceanModulesToUse(CategoryOceanModule.class)
public class TestCategoryOceanModule2 {

	@Test
	@Category(CategoryToUse.class)
	public void testCategoryToUseWithTheRightCategory() {
		Assert.assertTrue(true);
	}
	
	@Test
	@Category(CategoryToUse.class)
	public void testCategoryToUseWithTheRightCategory2() {
		Assert.assertTrue(true);
	}

	@Test
	@Category(CategoryToDoNotUse.class)
	public void testCategoryToDoNotUse() {
		Assert.fail("Do not execute that method");
	}
	

	
	@Test
	@Category(CategoryToDoNotUse.class)
	public void testCategoryToDoNotUse2() {
		Assert.fail("Do not execute that method");
	}
	
	@Test
	@Category(CategoryToDoNotUse.class)
	public void testCategoryToDoNotUse3() {
		Assert.fail("Do not execute that method");
	}

}
