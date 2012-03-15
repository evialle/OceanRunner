package it.freshminutes.oceanrunner.modules.engine;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

/**
 * Define an OceanModule. An OceanModule could be compared to a JUnit Runner.
 * But when you may have only one JUnit Runner for your class, you may use
 * multiple OceanModules.
 * 
 * @author Eric Vialle
 * 
 */
public abstract class OceanModule {

	public abstract void doBeforeAllTestedMethods(final OceanRunner oceanRunner, final Class<?> klass) throws OceanModuleException;
	
	public abstract void doAfterAllTestedMethods(final OceanRunner oceanRunner, final Class<?> klass) throws OceanModuleException;
	
	public abstract void doBeforeEachTestedMethod(final OceanRunner oceanRunner) throws OceanModuleException;
	
	public abstract void doAfterEachTestedMethod(final OceanRunner oceanRunner, final Description description) throws OceanModuleException;
	
	public abstract void doAfterEachFailedMethod(final OceanRunner oceanRunner, final Failure failure) throws OceanModuleException;
	
	public abstract void doAfterEachIgnoredMethod(final OceanRunner oceanRunner, final Description description) throws OceanModuleException;
	
	public abstract void doAfterEachAssumptionFailedMethod(final OceanRunner oceanRunner, final Failure failure) throws OceanModuleException;
	
}
