/**
 * 
 */
package it.freshminutes.oceanrunner.modules.builtin;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

/**
 * 
 * 
 * @author Eric Vialle
 */
public class DemoOceanModule extends OceanModule {

	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modulesengine.OceanModule#doBeforeAllTestedMethods(it.freshminutes.oceanrunner.OceanRunner, java.lang.Class)
	 */
	@Override
	public void doBeforeAllTestedMethods(OceanRunner oceanRunner, Class<?> klass) {
		System.out.println("Called doBeforeAllTestedMethods");

	}

	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modulesengine.OceanModule#doAfterAllTestedMethods(it.freshminutes.oceanrunner.OceanRunner, java.lang.Class)
	 */
	@Override
	public void doAfterAllTestedMethods(OceanRunner oceanRunner, Class<?> klass) {
		System.out.println("Called doAfterAllTestedMethods");
	}

	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modulesengine.OceanModule#doBeforeEachTestedMethod(it.freshminutes.oceanrunner.OceanRunner)
	 */
	@Override
	public void doBeforeEachTestedMethod(OceanRunner oceanRunner) {
		System.out.println(Thread.currentThread().getName() + " Called doBeforeEachTestedMethod");
	}

	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modulesengine.OceanModule#doAfterEachTestedMethod(it.freshminutes.oceanrunner.OceanRunner)
	 */
	@Override
	public void doAfterEachTestedMethod(OceanRunner oceanRunner, Description description) {
		System.out.println(Thread.currentThread().getName() + " Called doAfterEachTestedMethod");
	}



	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modulesengine.OceanModule#doAfterEachIgnoredMethod(it.freshminutes.oceanrunner.OceanRunner)
	 */
	@Override
	public void doAfterEachIgnoredMethod(OceanRunner oceanRunner, final Description description) {
		System.out.println(Thread.currentThread().getName() + " Called doAfterEachIgnoredMethod");
	}


	@Override
	public void doAfterEachFailedMethod(OceanRunner oceanRunner, Failure failure) {
		System.out.println(Thread.currentThread().getName() + " Called doAfterEachFailedMethod");
	}

	@Override
	public void doAfterEachAssumptionFailedMethod(OceanRunner oceanRunner,
			final Failure failure) {
		System.out.println(Thread.currentThread().getName() + " Called doAfterEachAssumptionFailedMethod");

		
	}

}
