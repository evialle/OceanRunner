package it.freshminutes.oceanrunner.modules.builtin;

import org.junit.experimental.categories.Category;
import org.junit.experimental.categories.Categories.CategoryFilter;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.Description;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.Failure;
import org.junit.runners.model.InitializationError;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;

public class CategoryOceanModule extends OceanModule {

	@Override
	public void doBeforeAllTestedMethods(OceanRunner oceanRunner, Class<?> klass) {
		
		try {
			oceanRunner.filter(new CategoryFilter(getIncludedCategory(klass),
					getExcludedCategory(klass)));
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
	private Class<?> getIncludedCategory(Class<?> klass) {
		IncludeCategory annotation= klass.getAnnotation(IncludeCategory.class);
		return annotation == null ? null : annotation.value();
	}

	private Class<?> getExcludedCategory(Class<?> klass) {
		ExcludeCategory annotation= klass.getAnnotation(ExcludeCategory.class);
		return annotation == null ? null : annotation.value();
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
