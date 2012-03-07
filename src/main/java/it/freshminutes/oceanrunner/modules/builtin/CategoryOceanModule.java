package it.freshminutes.oceanrunner.modules.builtin;

import it.freshminutes.oceanrunner.OceanRunner;
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

	private final static String CATEGORY_INCLUDED_PROPERTYKEY = "category.included";

	private final static String CATEGORY_EXCLUDED_PROPERTYKEY = "category.excluded";

	@Override
	public void doBeforeAllTestedMethods(OceanRunner oceanRunner, Class<?> klass) {
		
		try {
			oceanRunner.filter(new CategoryFilter(getIncludedCategory(oceanRunner, klass), getExcludedCategory(oceanRunner, klass)));
		} catch (NoTestsRemainException e) {
			//TODO
			e.printStackTrace();
		}
		try {
			assertNoCategorizedDescendentsOfUncategorizeableParents(oceanRunner.getDescription());
		} catch (InitializationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	private Class<?> getIncludedCategory(final OceanRunner runner, final Class<?> klass) {
		IncludeCategory annotation= klass.getAnnotation(IncludeCategory.class);

		if (annotation == null) {
			String strClass = (String) runner.getAwareProperty(CATEGORY_INCLUDED_PROPERTYKEY);
			if (strClass == null || strClass.isEmpty()) {
				return null;
			} else {
				try {
					return Class.forName(strClass);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
		} else {
			return annotation.value();
		}
	}

	private Class<?> getExcludedCategory(final OceanRunner runner, final Class<?> klass) {
		ExcludeCategory annotation= klass.getAnnotation(ExcludeCategory.class);

		if (annotation == null) {
			String strClass = (String) runner.getAwareProperty(CATEGORY_EXCLUDED_PROPERTYKEY);
			if (strClass == null || strClass.isEmpty()) {
				return null;
			} else {
				try {
					return Class.forName(strClass);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
		} else {
			return annotation.value();
		}
	}

	@Override
	public void doAfterAllTestedMethods(OceanRunner oceanRunner, Class<?> klass) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doBeforeEachTestedMethod(OceanRunner oceanRunner) {
		// TODO Auto-generated method stub
	}

	@Override
	public void doAfterEachTestedMethod(OceanRunner oceanRunner, Description description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doAfterEachIgnoredMethod(OceanRunner oceanRunner,final Description description) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void doAfterEachFailedMethod(OceanRunner oceanRunner, Failure failure) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doAfterEachAssumptionFailedMethod(OceanRunner oceanRunner,
			final Failure failure) {
		// TODO Auto-generated method stub
		
	}


	
}
