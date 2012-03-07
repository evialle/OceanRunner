package it.freshminutes.oceanrunner.modules.engine;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runners.model.InitializationError;

import it.freshminutes.oceanrunner.OceanRunner;

public abstract class OceanModule {

	public abstract void doBeforeAllTestedMethods(final OceanRunner oceanRunner, final Class<?> klass);
	
	public abstract void doAfterAllTestedMethods(final OceanRunner oceanRunner, final Class<?> klass);
	
	public abstract void doBeforeEachTestedMethod(final OceanRunner oceanRunner);
	
	public abstract void doAfterEachTestedMethod(final OceanRunner oceanRunner, final Description description);
	
	public abstract void doAfterEachFailedMethod(final OceanRunner oceanRunner, final Failure failure);
	
	public abstract void doAfterEachIgnoredMethod(final OceanRunner oceanRunner, final Description description);
	
	public abstract void doAfterEachAssumptionFailedMethod(final OceanRunner oceanRunner, final Failure failure);
	
}
