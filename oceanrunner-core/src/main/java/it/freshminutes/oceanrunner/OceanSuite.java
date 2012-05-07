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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * Declare a Suite to use. All the test classes are ordered in a text file
 * declared in @OceanSuiteClassListFile
 * 
 * eg:
 * 
 * @RunWith(OceanSuite.class)
 * @OceanSuiteClassListFile("suitetestliste.txt")
 * 
 * @author Eric Vialle
 * 
 */
public class OceanSuite extends ParentRunner<Runner> {

	private static final String TEST_CLASS_SUITE_FILE_PATH = "testClassSuiteFilePath";

	private final static String DIRECTORY_PATH_PROPERTY_ = "OceanSuiteDir";

	private final static String DEFAULT_DIRECTORY_PATH = "";

	/*
	 * Returns an empty suite.
	 */
	public static Runner emptySuite() {
		try {
			return new OceanSuite((Class<?>) null, new Class<?>[0]);
		} catch (InitializationError e) {
			throw new RuntimeException("This shouldn't be possible");
		}
	}

	/**
	 * The <code>OceanSuiteClasses</code> annotation specifies the classes to be
	 * run when a class annotated with
	 * <code>@RunWith(OceanSuiteClasses.class)</code> is run.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Inherited
	public @interface OceanSuiteClassListFile {
		/**
		 * @return the classes to be run
		 */
		public String value();
	}

	private static Class<?>[] getAnnotatedClasses(Class<?> klass) throws InitializationError {
		File file = getFileWithClassToTest(klass);

		// Convert the content of the file as a list of
		Class<?>[] classArray = getListOfClass(file);
		return classArray;
	}

	/**
	 * @param klass
	 * @return
	 * @throws InitializationError
	 */
	private static File getFileWithClassToTest(Class<?> klass) throws InitializationError {

		File file;

		OceanSuiteClassListFile annotation = klass.getAnnotation(OceanSuiteClassListFile.class);
		// Check the annotation
		if (annotation == null) {

			String filePath = System.getProperty(TEST_CLASS_SUITE_FILE_PATH);
			if (filePath == null) {
				throw new InitializationError(String.format("class '%s' must have a OceanSuiteClasses annotation or a JVM parameter -D"
						+ TEST_CLASS_SUITE_FILE_PATH, klass.getName()));
			} else {
				file = new File(filePath);
			}
		} else {
			String fileName = annotation.value();
			file = getFileFromItsAnnotedName(fileName);
		}

		return file;
	}

	private static Class<?>[] getListOfClass(File file) throws InitializationError {

		InputStream in = null;
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(file);
			in = new DataInputStream(fstream);
		} catch (FileNotFoundException e) {
			in = OceanSuite.class.getClassLoader().getResourceAsStream(file.toString());
		}

		if (in == null) {
			throw new InitializationError(String.format("The file %s doesn't exist or can't be read", file.toString()));
		}

		List<Class<?>> classList = new ArrayList<Class<?>>();
		try {
			// Open the file that is the first
			// command line parameter
			// Get the object of DataInputStream
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				if ((strLine != null) && (strLine.isEmpty() == false) && ('#' != strLine.charAt(0))) {
					strLine = strLine.replace("/", ".");
					strLine = strLine.replace("\\", ".");

					strLine = strLine.replace(System.getProperty("line.separator"), "");

					if ("".equals(strLine) == false) {
						try {
							Class<?> classToAdd = Class.forName(strLine);
							classList.add(classToAdd);

						} catch (ClassNotFoundException e) {
							System.err.println("The class " + strLine + " can't be found as described in the file " + file.toString());
						}
					}
				}

			}
			// Close the input stream
			in.close();
		} catch (IOException e) {// Catch exception if any
			throw new InitializationError("IOException with file " + file.toString() + ": " + e.getMessage());
		}

		int i = 0;
		Class<?>[] classArray = new Class<?>[classList.size()];
		for (Class<?> classToCopy : classList) {
			classArray[i++] = classToCopy;
		}

		return classArray;
	}

	private static File getFileFromItsAnnotedName(final String fileName) throws InitializationError {
		// Check the fileName
		if ((fileName == null) || fileName.isEmpty()) {
			throw new InitializationError("tested class must have a OceanSuiteClasses annotation, with a fileName");
		}

		// Get the file
		String filePathDir = System.getProperty(DIRECTORY_PATH_PROPERTY_ + System.getProperty("file.separator"), DEFAULT_DIRECTORY_PATH);

		File file = new File(filePathDir + fileName);

		return file;

	}

	private final List<Runner> fRunners;

	/**
	 * Called reflectively on classes annotated with
	 * <code>@RunWith(Suite.class)</code>
	 * 
	 * @param klass
	 *            the root class
	 * @param builder
	 *            builds runners for classes in the suite
	 * @throws InitializationError
	 */
	public OceanSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
		this(builder, klass, getAnnotatedClasses(klass));
	}

	/**
	 * Call this when there is no single root class (for example, multiple class
	 * names passed on the command line to {@link org.junit.runner.JUnitCore}
	 * 
	 * @param builder
	 *            builds runners for classes in the suite
	 * @param classes
	 *            the classes in the suite
	 * @throws InitializationError
	 */
	public OceanSuite(RunnerBuilder builder, Class<?>[] classes) throws InitializationError {
		this(null, builder.runners(null, classes));
	}

	/**
	 * Call this when the default builder is good enough. Left in for
	 * compatibility with JUnit 4.4.
	 * 
	 * @param klass
	 *            the root of the suite
	 * @param suiteClasses
	 *            the classes in the suite
	 * @throws InitializationError
	 */
	protected OceanSuite(Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
		this(new AllDefaultPossibilitiesBuilder(true), klass, suiteClasses);
	}

	/**
	 * Called by this class and subclasses once the classes making up the suite
	 * have been determined
	 * 
	 * @param builder
	 *            builds runners for classes in the suite
	 * @param klass
	 *            the root of the suite
	 * @param suiteClasses
	 *            the classes in the suite
	 * @throws InitializationError
	 */
	protected OceanSuite(RunnerBuilder builder, Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
		this(klass, builder.runners(klass, suiteClasses));
	}

	/**
	 * Called by this class and subclasses once the runners making up the suite
	 * have been determined
	 * 
	 * @param klass
	 *            root of the suite
	 * @param runners
	 *            for each class in the suite, a {@link Runner}
	 * @throws InitializationError
	 */
	protected OceanSuite(Class<?> klass, List<Runner> runners) throws InitializationError {
		super(klass);
		fRunners = runners;
	}

	@Override
	protected List<Runner> getChildren() {
		return fRunners;
	}

	@Override
	protected Description describeChild(Runner child) {
		return child.getDescription();
	}

	@Override
	protected void runChild(Runner runner, final RunNotifier notifier) {
		runner.run(notifier);
	}

}
