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
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;

import org.junit.experimental.categories.Categories.CategoryFilter;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.experimental.categories.Category;
import org.junit.runner.Description;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.Failure;
import org.junit.runners.model.InitializationError;

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
	public void doBeforeAllTestedMethods(OceanRunner oceanRunner, Class<?> klass) throws OceanModuleException {
		
		try {
			oceanRunner.filter(new CategoryFilter(getIncludedCategory(oceanRunner, klass), getExcludedCategory(oceanRunner, klass)));
		} catch (NoTestsRemainException e) {
			throw new OceanModuleException(e);

		}
		try {
			assertNoCategorizedDescendentsOfUncategorizeableParents(oceanRunner.getDescription());
		} catch (InitializationError e) {
			throw new OceanModuleException(e);

		}
	}

	private void assertNoCategorizedDescendentsOfUncategorizeableParents(Description description) throws InitializationError {
		if (!canHaveCategorizedChildren(description))
			assertNoDescendantsHaveCategoryAnnotations(description);
		for (Description each : description.getChildren())
			assertNoCategorizedDescendentsOfUncategorizeableParents(each);
	}

	private void assertNoDescendantsHaveCategoryAnnotations(Description description) throws InitializationError {			
		for (Description each : description.getChildren()) {
			if (each.getAnnotation(Category.class) != null)
				throw new InitializationError("Category annotations on Parameterized classes are not supported on individual methods.");
			assertNoDescendantsHaveCategoryAnnotations(each);
		}
	}

	// If children have names like [0], our current magical category code can't determine their
	// parentage.
	private static boolean canHaveCategorizedChildren(Description description) {
		for (Description each : description.getChildren())
			if (each.getTestClass() == null)
				return false;
		return true;
	}

	private Class<?> getIncludedCategory(final OceanRunner runner, final Class<?> klass) throws OceanModuleException {
		IncludeCategory annotation= klass.getAnnotation(IncludeCategory.class);

		if (annotation == null) {
			String strClass = (String) runner.getAwareProperty(CATEGORY_INCLUDED_PROPERTYKEY);
			if (strClass == null || strClass.isEmpty()) {
				return null;
			} else {
				try {
					return Class.forName(strClass);
				} catch (ClassNotFoundException e) {
					throw new OceanModuleException(e);
				}
			}
		} else {
			return annotation.value();
		}
	}

	private Class<?> getExcludedCategory(final OceanRunner runner, final Class<?> klass) throws OceanModuleException {
		ExcludeCategory annotation= klass.getAnnotation(ExcludeCategory.class);

		if (annotation == null) {
			String strClass = (String) runner.getAwareProperty(CATEGORY_EXCLUDED_PROPERTYKEY);
			if (strClass == null || strClass.isEmpty()) {
				return null;
			} else {
				try {
					return Class.forName(strClass);
				} catch (ClassNotFoundException e) {
					throw new OceanModuleException(e);

				}
			}
		} else {
			return annotation.value();
		}
	}

	@Override
	public void doAfterAllTestedMethods(OceanRunner oceanRunner, Class<?> klass) {
		
	}

	@Override
	public void doBeforeEachTestedMethod(OceanRunner oceanRunner) {
	}

	@Override
	public void doAfterEachTestedMethod(OceanRunner oceanRunner, Description description) {
		
	}

	@Override
	public void doAfterEachIgnoredMethod(OceanRunner oceanRunner,final Description description) {
		
	}



	@Override
	public void doAfterEachFailedMethod(OceanRunner oceanRunner, Failure failure) {
		
	}

	@Override
	public void doAfterEachAssumptionFailedMethod(OceanRunner oceanRunner,
			final Failure failure) {
		
	}


	
}
