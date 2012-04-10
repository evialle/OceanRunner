/**
 * 
 */
package it.freshminutes.oceanrunner.modules.categories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.experimental.categories.Category;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

/**
 * Filter to manage multiple Categories.
 * 
 * @author Eric Vialle
 */
public class MultipleCategoriesFilter extends Filter {

	private final List<Class<?>> fIncluded;

	private final List<Class<?>> fExcluded;

	public MultipleCategoriesFilter(final List<Class<?>> includedCategory,
			final List<Class<?>> excludedCategory) {
		fIncluded = includedCategory;
		fExcluded = excludedCategory;
	}

	@Override
	public String describe() {
		return "category " + fIncluded;
	}

	@Override
	public boolean shouldRun(Description description) {
		if (hasCorrectCategoryAnnotation(description))
			return true;
		for (Description each : description.getChildren())
			if (shouldRun(each))
				return true;
		return false;
	}

	private boolean hasCorrectCategoryAnnotation(Description description) {
		List<Class<?>> categories = categories(description);
		if (categories.isEmpty()) {
			return fIncluded == null;
		}
		//Excluded Categories
		for (Class<?> each : categories) {
			for (Class<?> excludedClass : fExcluded) {
				if (excludedClass != null
						&& excludedClass.isAssignableFrom(each)) {
					return false;
				}
			}
		}
		//Included Categories
		for (Class<?> each : categories) {
			for (Class<?> includedClass : fIncluded) {
				if (includedClass == null
						|| includedClass.isAssignableFrom(each)) {
					return true;
				}
			}
		}
		return false;
	}

	private List<Class<?>> categories(Description description) {
		ArrayList<Class<?>> categories = new ArrayList<Class<?>>();
		categories.addAll(Arrays.asList(directCategories(description)));
		categories.addAll(Arrays
				.asList(directCategories(parentDescription(description))));
		return categories;
	}

	private Description parentDescription(Description description) {
		Class<?> testClass = description.getTestClass();
		if (testClass == null)
			return null;
		return Description.createSuiteDescription(testClass);
	}

	private Class<?>[] directCategories(Description description) {
		if (description == null)
			return new Class<?>[0];
		Category annotation = description.getAnnotation(Category.class);
		if (annotation == null)
			return new Class<?>[0];
		return annotation.value();
	}
}
