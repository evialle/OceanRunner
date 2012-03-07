/**
 * 
 */
package it.freshminutes.oceanrunner;

import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;
import it.freshminutes.oceanrunner.exceptions.NoOceanModuleException;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;

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
 * @author Eric Vialle
 * 
 */
public class OceanRunner extends BlockJUnit4ClassRunner {

	private static final String EMPTY_STRING = "";

	private static final String RUNNERS_DEFAULTMODULES = "runners.defaultmodules";

	/**
	 * 
	 */
	private final static String DEFAULT_PROPERTIES_PATH = "/oceanrunner.properties";

	/**
	 * 
	 */
	private static final String OCEAN_RUNNER_PROPERTIES = "OceanRunnerProp";

	/**
	 * 
	 */
	private static Properties properties = null;

	private ThreadLocal<Object> targetThreadLocal = new ThreadLocal<Object>();

	/** */
	private List<OceanModule> oceanModulesList = new ArrayList<OceanModule>();

	/** */
	private Class<?> classUnderTest = null;

	/**
	 * 
	 * @param classToTest
	 * @throws InitializationError
	 * @throws NoOceanModuleException
	 */
	public OceanRunner(Class<?> classToTest) throws InitializationError, NoOceanModuleException {
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
					Field fTargetField = InvokeMethod.class.getDeclaredField("fTarget");
					fTargetField.setAccessible(true);
					Object target = fTargetField.get(stmt);
					this.setTarget(target);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			runLeaf(stmt, description, notifier);
		}
	}

	public Properties getProperties() {
		if (OceanRunner.properties == null) {
			OceanRunner.properties = new Properties();

			InputStream in;
			try {
				in = getAbsoluteInputStream(getPropertiesFilePath());
				OceanRunner.properties.load(in);
				in.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return OceanRunner.properties;
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
	 * @throws NoOceanModuleException
	 * 
	 */
	private void loadAllOceanModules() throws NoOceanModuleException {
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
				e.printStackTrace();
			}
		}

	}

	/**
	 * @return
	 * @throws IOException
	 * @throws NoOceanModuleException
	 */
	@SuppressWarnings("unchecked")
	private List<Class<? extends OceanModule>> listOceanModuleFromProperties() throws IOException, NoOceanModuleException {

		String defaultModulesProperty = getProperties().getProperty(RUNNERS_DEFAULTMODULES, EMPTY_STRING);
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

	/**
	 * @return
	 */
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

	/**
	 * 
	 */
	private void initializeAllOceanModules() {
		for (OceanModule oceanModule : this.oceanModulesList) {
			oceanModule.doBeforeAllTestedMethods(this, this.classUnderTest);
		}
	}

	/**
	 * 
	 */
	private void endAllOceanModules() {
		for (OceanModule oceanModule : this.oceanModulesList) {
			oceanModule.doAfterAllTestedMethods(this, this.classUnderTest);
		}
	}

	/**
	 * @param notifier
	 */
	public void run(final RunNotifier notifier) {
		notifier.addListener(new OceanListener());
		super.run(notifier);
	}

	/**
	 * 
	 */
	private void doBeforeEachTestTestedMethodForAllModules() {
		for (OceanModule oceanModule : this.oceanModulesList) {
			oceanModule.doBeforeEachTestedMethod(this);
		}
	}

	/**
	 * 
	 * @param description
	 */
	private void doAfterEachTestTestedMethodForAllModules(final Description description) {

		for (OceanModule oceanModule : this.oceanModulesList) {
			oceanModule.doAfterEachTestedMethod(this, description);
		}

		// Remove the target
		this.setTarget(null);
	}

	/**
	 * 
	 * @param failure
	 */
	private void doAfterEachFailureForAllModules(Failure failure, Object target) {
		for (OceanModule oceanModule : this.oceanModulesList) {
			oceanModule.doAfterEachFailedMethod(this, failure);
		}
	}

	/**
	 * 
	 * @param failure
	 */
	private void doAfterEachAssumptionFailureForAllModules(final Failure failure) {
		for (OceanModule oceanModule : this.oceanModulesList) {
			oceanModule.doAfterEachAssumptionFailedMethod(this, failure);
		}
	}

	/**
	 * 
	 * @param description
	 */
	private void doAfterEachIgnoreForAllModules(final Description description, final Object object) {
		for (OceanModule oceanModule : this.oceanModulesList) {
			oceanModule.doAfterEachIgnoredMethod(this, description);
		}

	}

	/**
	 * 
	 * @author Eric Vialle
	 * 
	 */
	private class OceanListener extends RunListener {
		/**
		 * Called before any tests have been run.
		 * 
		 * @param description
		 *            describes the tests to be run
		 */
		public void testRunStarted(Description description) throws Exception {
			initializeAllOceanModules();
		}

		/**
		 * Called when all tests have finished
		 * 
		 * @param result
		 *            the summary of the test run, including all the tests that
		 *            failed
		 */
		public void testRunFinished(Result result) throws Exception {
			endAllOceanModules();
		}

		/**
		 * Called when an atomic test is about to be started.
		 * 
		 * @param description
		 *            the description of the test that is about to be run
		 *            (generally a class and method name)
		 */
		public void testStarted(Description description) throws Exception {
			doBeforeEachTestTestedMethodForAllModules();
		}

		/**
		 * Called when an atomic test has finished, whether the test succeeds or
		 * fails.
		 * 
		 * @param description
		 *            the description of the test that just ran
		 */
		public void testFinished(Description description) throws Exception {
			doAfterEachTestTestedMethodForAllModules(description);
		}

		/**
		 * Called when an atomic test fails.
		 * 
		 * @param failure
		 *            describes the test that failed and the exception that was
		 *            thrown
		 */
		public void testFailure(Failure failure) throws Exception {
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
		public void testAssumptionFailure(Failure failure) {
			doAfterEachAssumptionFailureForAllModules(failure);
		}

		/**
		 * Called when a test will not be run, generally because a test method
		 * is annotated with {@link org.junit.Ignore}.
		 * 
		 * @param description
		 *            describes the test that will not be run
		 */
		public void testIgnored(Description description) throws Exception {
			doAfterEachIgnoreForAllModules(description, getTarget());
		}

	}

}
