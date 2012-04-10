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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.experimental.categories.Category;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

import com.google.common.collect.Lists;

/**
 * Filter to manage multiple Categories.
 * 
 * @author Eric Vialle
 */
public class MultipleCategoriesFilter extends Filter {

	private final List<Class<?>> fIncluded;

	private final List<Class<?>> fExcluded;

	public MultipleCategoriesFilter(final List<Class<?>> includedCategory, final List<Class<?>> excludedCategory) {
		//fIncluded can't be null
		if (includedCategory == null) {
			this.fIncluded = Lists.newArrayList();
		} else {
			this.fIncluded = includedCategory;
		}
		
		//fExcluded can't be null (too)
		if (excludedCategory == null) {
			this.fExcluded = Lists.newArrayList();
		} else {
			this.fExcluded = excludedCategory;
		}
	}

	@Override
	public String describe() {
		return "category " + this.fIncluded;
	}

	@Override
	public boolean shouldRun(final Description description) {
		if (hasCorrectCategoryAnnotation(description)) {
			return true;
		}
		for (Description each : description.getChildren()) {
			if (shouldRun(each)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasCorrectCategoryAnnotation(final Description description) {
		List<Class<?>> categories = categories(description);
		if (categories.isEmpty()) {
			return fIncluded.isEmpty();
		}
		// Excluded Categories
		for (Class<?> each : categories) {
			if (fExcluded.isEmpty()) {
				return false;
			} else {
				for (Class<?> excludedClass : fExcluded) {
					if (excludedClass != null && excludedClass.isAssignableFrom(each)) {
						return false;
					}
				}
			}
		}
		// Included Categories
		for (Class<?> each : categories) {
			if (fIncluded.isEmpty()) {
				return true;
			} else {
			for (Class<?> includedClass : fIncluded) {
				if (includedClass == null || includedClass.isAssignableFrom(each)) {
					return true;
				}
			}
			}
		}
		
		return false;
	}

	private List<Class<?>> categories(final Description description) {
		ArrayList<Class<?>> categories = Lists.newArrayList();
		categories.addAll(Arrays.asList(directCategories(description)));
		categories.addAll(Arrays.asList(directCategories(parentDescription(description))));
		return categories;
	}

	private Description parentDescription(final Description description) {
		Class<?> testClass = description.getTestClass();
		if (testClass == null) {
			return null;
		}
		return Description.createSuiteDescription(testClass);
	}

	private Class<?>[] directCategories(final Description description) {
		if (description == null) {
			return new Class<?>[0];
		}
		Category annotation = description.getAnnotation(Category.class);
		if (annotation == null) {	
			return new Class<?>[0];
		}
		return annotation.value();
	}
}
