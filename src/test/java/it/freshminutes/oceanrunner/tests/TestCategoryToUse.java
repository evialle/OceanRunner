package it.freshminutes.oceanrunner.tests;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;
import it.freshminutes.oceanrunner.modules.builtin.CategoryOceanModule;
import it.freshminutes.oceanrunner.tests.categories.CategoryToDoNotUse;
import it.freshminutes.oceanrunner.tests.categories.CategoryToUse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@RunWith(OceanRunner.class)
@OceanModulesToUse(CategoryOceanModule.class)
public class TestCategoryToUse {

	@Test
	@Category(CategoryToUse.class)
	public void testCategoryToUseWithTheRightCategory() {
		Assert.assertTrue(true);
	}

	@Test
	@Category(CategoryToDoNotUse.class)
	public void testCategoryToDoNotUser() {
		Assert.fail("Do not execute that method");
	}

}
