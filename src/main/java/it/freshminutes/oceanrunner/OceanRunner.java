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
package it.freshminutes.oceanrunner;

import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;
import it.freshminutes.oceanrunner.exceptions.NoOceanModuleException;
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;
import it.freshminutes.oceanrunner.modules.engine.OceanRunnerScheduler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Ignore;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.internal.runners.statements.InvokeMethod;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * OceanRunner is the JUnit Runner to use, to use OceanModule. Add
 * @RunWith(OceanRunner.class) to your test class.
 * 
 * @author Eric Vialle
 */
public class OceanRunner extends BlockJUnit4ClassRunner {

	/** Empty String. */
	private static final String EMPTY_STRING = "";

	/** property defining the OceanModules used by default. */
	private static final String RUNNERS_DEFAULTMODULES = "runners.defaultmodules";

	/** Path used for oceanrunner.properties by default. */
	private final static String DEFAULT_PROPERTIES_PATH = "/oceanrunner.properties";

	/**
	 * JVM System property defining the path of the oceanrunner.properties path.
	 */
	private static final String OCEAN_RUNNER_PROPERTIES = "OceanRunnerProp";

	/** Properties. */
	private static Properties properties = null;

	private ThreadLocal<Object> targetThreadLocal = new ThreadLocal<Object>();

	/** List of OceanModules. */
	private List<OceanModule> oceanModulesList = new ArrayList<OceanModule>();

	/** Class actualy tested. */
	private Class<?> classUnderTest = null;

	/**
	 * @return the classUnderTest
	 */
	public Class<?> getClassUnderTest() {
		return classUnderTest;
	}

	private List<FrameworkMethod> fFilteredChildren = null;

	/** Default Scheduler. */
	private OceanRunnerScheduler fScheduler = new OceanRunnerScheduler() {

		@Override
		public void finished() {
			// do nothing
		}

		@Override
		public void schedule(Runnable childStatement, FrameworkMethod method) {
			childStatement.run();
		}
	};

	/**
	 * Constructor.
	 * 
	 * @param classToTest
	 * @throws InitializationError
	 * @throws NoOceanModuleException
	 * @throws OceanModuleException
	 */
	public OceanRunner(Class<?> classToTest) throws InitializationError, NoOceanModuleException, OceanModuleException {
		super(classToTest);
		this.classUnderTest = classToTest;
		loadAllOceanModules();
		initializeAllOceanModules();
	}

	@Override
	protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
		Description description = describeChild(method);
		if (method.getAnnotation(Ignore.class) != null) {
			notifier.fireTestIgnored(description);
		} else {
			Statement stmt = methodBlock(method);
			if (stmt instanceof InvokeMethod) {
				try {
					// Get the target
					Field fTargetField = InvokeMethod.class.getDeclaredField("fTarget");
					fTargetField.setAccessible(true);
					Object target = fTargetField.get(stmt);
					this.setTarget(target);

					// Run Statement
					EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
					eachNotifier.fireTestStarted();
					try {
						stmt.evaluate();
					} catch (AssumptionViolatedException e) {
						eachNotifier.addFailedAssumption(e);
					} catch (Throwable e) {
						eachNotifier.addFailure(e);
					} finally {
						eachNotifier.fireTestFinished();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}


	/**
	 * 
	 * @return Properties from the properties file
	 * @throws OceanModuleException
	 */
	private Properties getProperties() throws OceanModuleException {
		if (OceanRunner.properties == null) {
			OceanRunner.properties = new Properties();

			InputStream in;
			try {
				in = getAbsoluteInputStream(getPropertiesFilePath());
				OceanRunner.properties.load(in);
				in.close();
			} catch (FileNotFoundException e) {
				throw new OceanModuleException(e);
			} catch (IOException e) {
				throw new OceanModuleException(e);
			}

		}

		return OceanRunner.properties;
	}

	/**
	 * Return a property in priority from System, then from Properties file.
	 * 
	 * @param key
	 * @return
	 * @throws OceanModuleException
	 */
	public String getAwareProperty(final String key) throws OceanModuleException {

		String toReturn = System.getProperties().getProperty(key);
		if (toReturn == null) {
			toReturn = getProperties().getProperty(key);
		}
		return toReturn;
	}

	/**
	 * Return a property in priority from System, then from Properties file.
	 * 
	 * @param key
	 * @param defaultValue
	 *            default value, if the value attached to the key is not
	 *            retrieved
	 * @return
	 * @throws OceanModuleException
	 */
	public String getAwareProperty(final String key, final String defaulValue) throws OceanModuleException {

		String toReturn = getAwareProperty(key);

		if (toReturn == null) {
			toReturn = defaulValue;
		}

		return toReturn;

	}

	/**
	 * Load from local or absolute filepath
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	private InputStream getAbsoluteInputStream(final String path) throws FileNotFoundException {
		InputStream in;
		try {
			in = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			in = getClass().getResourceAsStream(getPropertiesFilePath());
			if (in == null) {
				throw e;
			}
		}

		return in;
	}

	/**
	 * 
	 * @return the Running test case, could be null
	 */
	public Object getTarget() {
		return targetThreadLocal.get();

	}

	protected void setTarget(Object obj) {
		this.targetThreadLocal.set(obj);
	}

	private String getPropertiesFilePath() {
		return System.getProperty(OCEAN_RUNNER_PROPERTIES, DEFAULT_PROPERTIES_PATH);
	}


	/**
	 * Initialize all OceanModules.
	 * 
	 * @throws NoOceanModuleException
	 * @throws OceanModuleException
	 *             while instantiating a Module
	 */
	private void loadAllOceanModules() throws NoOceanModuleException, OceanModuleException {
		List<Class<? extends OceanModule>> oceanModulesClass;
		if (this.classUnderTest.getAnnotation(OceanModulesToUse.class) != null) {
			// load from annotation
			oceanModulesClass = listOceanModulesFromAnnotation();
		} else {
			// load from properties
			try {
				oceanModulesClass = listOceanModuleFromProperties();
			} catch (IOException e) {
				oceanModulesClass = new ArrayList<Class<? extends OceanModule>>();
			}
		}

		for (Class<? extends OceanModule> moduleClass : oceanModulesClass) {
			try {
				OceanModule oceanModule = moduleClass.newInstance();
				oceanModulesList.add(oceanModule);

			} catch (Exception e) {
				throw new OceanModuleException(e);
			}
		}

	}


	@SuppressWarnings("unchecked")
	private List<Class<? extends OceanModule>> listOceanModuleFromProperties() throws IOException, NoOceanModuleException, OceanModuleException {

		String defaultModulesProperty = getAwareProperty(RUNNERS_DEFAULTMODULES, EMPTY_STRING);
		String[] defaultModulesArray = defaultModulesProperty.split(";");
		List<Class<? extends OceanModule>> oceanModulesClassList = new ArrayList<Class<? extends OceanModule>>(defaultModulesArray.length);
		for (int i = 0; i < defaultModulesArray.length; i++) {
			String classToLoadStr = defaultModulesArray[i];
			try {
				oceanModulesClassList.add((Class<? extends OceanModule>) Class.forName(classToLoadStr));

			} catch (ClassNotFoundException e) {
				if ((classToLoadStr != null) && (!classToLoadStr.trim().equals(EMPTY_STRING))) {
					throw new NoOceanModuleException(classToLoadStr);
				}
			} catch (ClassCastException e) {
				throw new NoOceanModuleException(classToLoadStr);
			}
		}
		return oceanModulesClassList;
	}


	private List<Class<? extends OceanModule>> listOceanModulesFromAnnotation() {
		List<Class<? extends OceanModule>> oceanModulesClassList;
		if (this.classUnderTest.getAnnotation(OceanModulesToUse.class).value() == null) {
			oceanModulesClassList = new ArrayList<Class<? extends OceanModule>>();
		} else {
			Class<? extends OceanModule>[] array = this.classUnderTest.getAnnotation(OceanModulesToUse.class).value();
			oceanModulesClassList = new ArrayList<Class<? extends OceanModule>>(Arrays.asList(array));
		}
		return oceanModulesClassList;
	}


	private void initializeAllOceanModules() throws OceanModuleException {
		for (OceanModule oceanModule : this.oceanModulesList) {
			oceanModule.doBeforeAllTestedMethods(this, this.classUnderTest);
		}
	}


	private void endAllOceanModules() throws OceanModuleException {
		for (OceanModule oceanModule : this.oceanModulesList) {
			oceanModule.doAfterAllTestedMethods(this, this.classUnderTest);
		}
	}


	public void run(final RunNotifier notifier) {
		notifier.addFirstListener(new OceanListener());
		super.run(notifier);
	}


	private void doBeforeEachTestTestedMethodForAllModules() throws OceanModuleException {
		for (OceanModule oceanModule : this.oceanModulesList) {
			oceanModule.doBeforeEachTestedMethod(this);
		}
	}


	private void doAfterEachTestTestedMethodForAllModules(final Description description) throws OceanModuleException {

		for (OceanModule oceanModule : this.oceanModulesList) {
			oceanModule.doAfterEachTestedMethod(this, description);
		}

		// Remove the target
		this.setTarget(null);
	}



	private void doAfterEachFailureForAllModules(final Failure failure, Object target) throws OceanModuleException {
		for (OceanModule oceanModule : this.oceanModulesList) {
			oceanModule.doAfterEachFailedMethod(this, failure);
		}
	}

	private void doAfterEachAssumptionFailureForAllModules(final Failure failure) throws OceanModuleException {
		for (OceanModule oceanModule : this.oceanModulesList) {
			oceanModule.doAfterEachAssumptionFailedMethod(this, failure);
		}
	}


	private void doAfterEachIgnoreForAllModules(final Description description, final Object object) throws OceanModuleException {
		for (OceanModule oceanModule : this.oceanModulesList) {
			oceanModule.doAfterEachIgnoredMethod(this, description);
		}

	}


	/**
	 * Returns a {@link Statement}: Call {@link #runChild(Object, RunNotifier)}
	 * on each object returned by {@link #getChildren()} (subject to any imposed
	 * filter and sort)
	 */
	protected Statement childrenInvoker(final RunNotifier notifier) {
		return new Statement() {
			@Override
			public void evaluate() {
				runChildren(notifier);
			}
		};
	}

	/**
	 * Run children is special, it use an Enhanced Scheduler.
	 * 
	 * @param notifier
	 */
	private void runChildren(final RunNotifier notifier) {
		for (final FrameworkMethod each : getFilteredChildren())

			fScheduler.schedule(new Runnable() {
				public void run() {
					OceanRunner.this.runChild(each, notifier);
				}
			}, each);
		fScheduler.finished();
	}
	

	private List<FrameworkMethod> getFilteredChildren() {
		if (fFilteredChildren == null)
			fFilteredChildren = new ArrayList<FrameworkMethod>(getChildren());
		return fFilteredChildren;
	}

	/**
	 * Sets a scheduler that determines the order and parallelization of
	 * children. Highly experimental feature that may change.
	 */
	public void setScheduler(OceanRunnerScheduler scheduler) {
		this.fScheduler = scheduler;
	}

	/**
	 * Listener used for Runner.
	 * 
	 * @author Eric Vialle
	 */
	private class OceanListener extends RunListener {
		/**
		 * Called before any tests have been run.
		 * 
		 * @param description
		 *            describes the tests to be run
		 */
		public void testRunStarted(final Description description) throws Exception {
			initializeAllOceanModules();
		}

		/**
		 * Called when all tests have finished
		 * 
		 * @param result
		 *            the summary of the test run, including all the tests that
		 *            failed
		 */
		public void testRunFinished(final Result result) throws Exception {
			endAllOceanModules();
		}

		/**
		 * Called when an atomic test is about to be started.
		 * 
		 * @param description
		 *            the description of the test that is about to be run
		 *            (generally a class and method name)
		 */
		public void testStarted(final Description description) throws Exception {
			doBeforeEachTestTestedMethodForAllModules();
		}

		/**
		 * Called when an atomic test has finished, whether the test succeeds or
		 * fails.
		 * 
		 * @param description
		 *            the description of the test that just ran
		 */
		public void testFinished(final Description description) throws Exception {
			doAfterEachTestTestedMethodForAllModules(description);
		}

		/**
		 * Called when an atomic test fails.
		 * 
		 * @param failure
		 *            describes the test that failed and the exception that was
		 *            thrown
		 */
		public void testFailure(final Failure failure) throws Exception {
			doAfterEachFailureForAllModules(failure, getTarget());
		}

		/**
		 * Called when an atomic test flags that it assumes a condition that is
		 * false
		 * 
		 * @param failure
		 *            describes the test that failed and the
		 *            {@link AssumptionViolatedException} that was thrown
		 */
		public void testAssumptionFailure(final Failure failure) {
			try {
				doAfterEachAssumptionFailureForAllModules(failure);
			} catch (OceanModuleException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Called when a test will not be run, generally because a test method
		 * is annotated with {@link org.junit.Ignore}.
		 * 
		 * @param description
		 *            describes the test that will not be run
		 */
		public void testIgnored(final Description description) throws Exception {
			doAfterEachIgnoreForAllModules(description, getTarget());
		}

	}

}
