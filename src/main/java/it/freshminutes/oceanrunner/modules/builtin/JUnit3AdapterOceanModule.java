package it.freshminutes.oceanrunner.modules.builtin;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;
import it.freshminutes.oceanrunner.utils.ReflectionUtils;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

/**
 * Convert easily JUnit3 module in JUnit4 modules (sometimes NPE due to huge
 * reflections in test classes).
 * 
 * @author Eric Vialle
 * 
 */
public class JUnit3AdapterOceanModule extends OceanModule {

	@Override
	public void doBeforeAllTestedMethods(OceanRunner oceanRunner, Class<?> klass) {
	}

	@Override
	public void doAfterAllTestedMethods(OceanRunner oceanRunner, Class<?> klass) {
	}

	@Override
	public void doBeforeEachTestedMethod(OceanRunner oceanRunner) throws OceanModuleException {
		if (oceanRunner.getTarget() instanceof TestCase) {
			try {
				Method m = ReflectionUtils.invokeHeritedMethod(oceanRunner.getTarget(), "setUp");
				m.setAccessible(true);
				m.invoke(oceanRunner.getTarget());
			} catch (Exception e) {
				throw new OceanModuleException(e);
			}
		}
	}

	@Override
	public void doAfterEachTestedMethod(OceanRunner oceanRunner,
 Description description) throws OceanModuleException {
		if (oceanRunner.getTarget() instanceof TestCase) {
			try {
				Method m = ReflectionUtils.invokeHeritedMethod(oceanRunner.getTarget(), "tearDown");

				m.setAccessible(true);
				m.invoke(oceanRunner.getTarget());
			} catch (Exception e) {
				throw new OceanModuleException(e);
			}
		}
	}

	@Override
	public void doAfterEachFailedMethod(OceanRunner oceanRunner, Failure failure) {

	}

	@Override
	public void doAfterEachIgnoredMethod(OceanRunner oceanRunner,
			Description description) {

	}

	@Override
	public void doAfterEachAssumptionFailedMethod(OceanRunner oceanRunner,
			Failure failure) {

	}

}
