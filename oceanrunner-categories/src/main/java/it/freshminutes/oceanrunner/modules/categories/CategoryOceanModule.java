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
package it.freshminutes.oceanrunner.modules.categories;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;

import java.util.List;

import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.experimental.categories.Category;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.model.InitializationError;

import com.google.common.collect.Lists;

/**
 * 
 * New way to define @IncludeCategory() / @IncludeCategory(). In addition to
 * those annotation, you may use -Dcategory.included / -Dcategory.included or
 * those properties in oceanrunner.properties
 * 
 * @author Eric Vialle
 * 
 */
public class CategoryOceanModule extends OceanModule {

	/** Name of the property to include categories. */
	private final static String CATEGORY_INCLUDED_PROPERTYKEY = "category.included";

	/** Name of the property to exclude categories. */
	private final static String CATEGORY_EXCLUDED_PROPERTYKEY = "category.excluded";

	@Override
	public void doBeforeAllTestedMethods(final OceanRunner oceanRunner, final Class<?> klass) throws OceanModuleException {

		try {
			List<Class<?>> includedCategory = getIncludedCategory(oceanRunner, klass);
			List<Class<?>> excludedCategory = getExcludedCategory(oceanRunner, klass);
			Filter filter = new MultipleCategoriesFilter(includedCategory, excludedCategory);
			oceanRunner.filter(filter);
		} catch (NoTestsRemainException e) {
			throw new OceanModuleException(e);
		}
		try {
			assertNoCategorizedDescendentsOfUncategorizeableParents(oceanRunner
					.getDescription());
		} catch (InitializationError e) {
			throw new OceanModuleException(e);

		}
	}

	private void assertNoCategorizedDescendentsOfUncategorizeableParents(
			final Description description) throws InitializationError {
		if (!canHaveCategorizedChildren(description)) {
			assertNoDescendantsHaveCategoryAnnotations(description);
		}
		for (Description each : description.getChildren()) {
			assertNoCategorizedDescendentsOfUncategorizeableParents(each);
		}
	}

	private void assertNoDescendantsHaveCategoryAnnotations(
			final Description description) throws InitializationError {
		for (Description each : description.getChildren()) {
			if (each.getAnnotation(Category.class) != null) {
				throw new InitializationError(
						"Category annotations on Parameterized classes are not supported on individual methods.");
			}
			assertNoDescendantsHaveCategoryAnnotations(each);
		}
	}

	// If children have names like [0], our current magical category code can't
	// determine their
	// parentage.
	private static boolean canHaveCategorizedChildren(
			final Description description) {
		for (Description each : description.getChildren()) {
			if (each.getTestClass() == null) {
				return false;
			}
		}
		return true;
	}

	private List<Class<?>> getIncludedCategory(final OceanRunner runner,
			final Class<?> klass) throws OceanModuleException {

		IncludeCategory annotation = klass.getAnnotation(IncludeCategory.class);

		List<Class<?>> includedCategoryList = Lists.newArrayList();

		if (annotation == null) {
			String strClass = (String) runner.getAwareProperty(CATEGORY_INCLUDED_PROPERTYKEY);
			if (strClass != null && !strClass.isEmpty()) {
				try {
					String[] classArray = strClass.trim().split(";");
					for (String classForName : classArray) {
						includedCategoryList.add(Class.forName(classForName));
					}
				} catch (ClassNotFoundException e) {
					throw new OceanModuleException(e);
				}
			}
		} else {
			includedCategoryList.add(annotation.value());
		}

		return includedCategoryList;
	}

	private List<Class<?>> getExcludedCategory(final OceanRunner runner,
			final Class<?> klass) throws OceanModuleException {

		ExcludeCategory annotation = klass.getAnnotation(ExcludeCategory.class);

		List<Class<?>> excludedCategoryList = Lists.newArrayList();

		if (annotation == null) {
			String strClass = (String) runner
					.getAwareProperty(CATEGORY_EXCLUDED_PROPERTYKEY);
			if (strClass != null && !strClass.isEmpty()) {
				try {
					String[] classArray = strClass.split(";");
					for (String classForName : classArray) {
						excludedCategoryList.add(Class.forName(classForName
								.trim()));
					}
				} catch (ClassNotFoundException e) {
					throw new OceanModuleException(e);
				}
			}
		} else {
			excludedCategoryList.add(annotation.value());
		}

		return excludedCategoryList;
	}

}
