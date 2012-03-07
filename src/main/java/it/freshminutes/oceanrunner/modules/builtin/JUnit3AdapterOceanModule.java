package it.freshminutes.oceanrunner.modules.builtin;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;
import it.freshminutes.oceanrunner.utils.ReflectionUtils;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

public class JUnit3AdapterOceanModule extends OceanModule {

	@Override
	public void doBeforeAllTestedMethods(OceanRunner oceanRunner, Class<?> klass) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doAfterAllTestedMethods(OceanRunner oceanRunner, Class<?> klass) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doBeforeEachTestedMethod(OceanRunner oceanRunner) {
		if (oceanRunner.getTarget() instanceof TestCase) {
			try {
				Method m = ReflectionUtils.invokeHeritedMethod(oceanRunner.getTarget(), "setUp");
				m.setAccessible(true);
				m.invoke(oceanRunner.getTarget());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void doAfterEachTestedMethod(OceanRunner oceanRunner,
			Description description) {
		if (oceanRunner.getTarget() instanceof TestCase) {
			try {
				Method m = ReflectionUtils.invokeHeritedMethod(oceanRunner.getTarget(), "tearDown");

				m.setAccessible(true);
				m.invoke(oceanRunner.getTarget());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void doAfterEachFailedMethod(OceanRunner oceanRunner, Failure failure) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doAfterEachIgnoredMethod(OceanRunner oceanRunner,
			Description description) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doAfterEachAssumptionFailedMethod(OceanRunner oceanRunner,
			Failure failure) {
		// TODO Auto-generated method stub

	}

}
