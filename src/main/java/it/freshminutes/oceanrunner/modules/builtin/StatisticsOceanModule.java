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
package it.freshminutes.oceanrunner.modules.builtin;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;
import it.freshminutes.oceanrunner.modules.builtin.statistics.StatisticsDataPlug;
import it.freshminutes.oceanrunner.modules.builtin.statistics.StatisticsResult;
import it.freshminutes.oceanrunner.modules.builtin.statistics.StatisticsResult.StatusTestResult;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Not developped.
 * 
 * 
 * @author Eric Vialle
 * 
 */
public class StatisticsOceanModule extends OceanModule {
	



	private List<String> testmethodsList = Lists.newArrayList();
	private Map<String, StatisticsResult> lastResultsMap = Maps.newHashMap();
	private Map<String, StatisticsResult> actualResultsMap = Maps.newHashMap();


	private OceanRunner oceanRunner;
	
	private static Connection dbConn;
	
	private static StatisticsDataPlug statisticsDataPlug;

	private ExecutorService threadExecutor = Executors.newSingleThreadExecutor();



	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modules.engine.OceanModule#doBeforeAllTestedMethods(it.freshminutes.oceanrunner.OceanRunner, java.lang.Class)
	 */
	@Override
	public void doBeforeAllTestedMethods(OceanRunner oceanRunner, Class<?> klass) throws OceanModuleException {
		String StatisticsDataPlugClassName = oceanRunner.getAwareProperty("statistics.dataplug");
		try {

			// get constructor that takes a String as argument
			Constructor<?> constructor = Class.forName(StatisticsDataPlugClassName).getConstructor(OceanRunner.class);

			StatisticsOceanModule.statisticsDataPlug = (StatisticsDataPlug) constructor.newInstance(oceanRunner);

			this.testmethodsList = loadTestsToSearch(klass);
			this.lastResultsMap = statisticsDataPlug.loadLastTestStatus(this.testmethodsList);
			this.actualResultsMap = Maps.newHashMap();

		} catch (ClassNotFoundException e) {
			throw new OceanModuleException(e);
		} catch (NoSuchMethodException e) {
			throw new OceanModuleException(e);
		} catch (SecurityException e) {
			throw new OceanModuleException(e);
		} catch (InstantiationException e) {
			throw new OceanModuleException(e);
		} catch (IllegalAccessException e) {
			throw new OceanModuleException(e);
		} catch (IllegalArgumentException e) {
			throw new OceanModuleException(e);
		} catch (InvocationTargetException e) {
			throw new OceanModuleException(e);
		}

	}



	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modules.engine.OceanModule#doAfterAllTestedMethods(it.freshminutes.oceanrunner.OceanRunner, java.lang.Class)
	 */
	@Override
	public void doAfterAllTestedMethods(final OceanRunner oceanRunner, final Class<?> klass) {
		// we do not block the JUnit test
		this.threadExecutor.execute(new Thread() {

			@Override
			public void run() {
				statisticsDataPlug.storeLastTestStatus(actualResultsMap.values());
			}
		});
	}

	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modules.engine.OceanModule#doAfterEachFailedMethod(it.freshminutes.oceanrunner.OceanRunner, org.junit.runner.notification.Failure)
	 */
	@Override
	public void doAfterEachFailedMethod(OceanRunner oceanRunner, Failure failure) {
		// Add the statistics result in the cache result
		StatisticsResult sResults = new StatisticsResult();
		sResults.setComments(failure.getMessage() + "\n> Trace:" + failure.getTrace() );
		sResults.setRunDate(new Date());
		sResults.setStatus(StatusTestResult.FAILED);
		sResults.setThrowable(failure.getException());
		sResults.setClassUnderTestName(failure.getDescription().getClassName());
		sResults.setMethodUnderTestName(failure.getDescription().getMethodName());

		enhanceThrowable(failure.getException(), "Failed enhanced");

		actualResultsMap.put(failure.getDescription().getMethodName(), sResults);
	}

	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modules.engine.OceanModule#doAfterEachIgnoredMethod(it.freshminutes.oceanrunner.OceanRunner, org.junit.runner.Description)
	 */
	@Override
	public void doAfterEachIgnoredMethod(OceanRunner oceanRunner,
			Description description) {
		// Add the statistics result in the cache result
		StatisticsResult sResults = new StatisticsResult();
		sResults.setRunDate(new Date());
		sResults.setStatus(StatusTestResult.IGNORE);
		sResults.setClassUnderTestName(description.getClassName());
		sResults.setMethodUnderTestName(description.getMethodName());

		actualResultsMap.put(description.getMethodName(), sResults);
	}

	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modules.engine.OceanModule#doAfterEachAssumptionFailedMethod(it.freshminutes.oceanrunner.OceanRunner, org.junit.runner.notification.Failure)
	 */
	@Override
	public void doAfterEachAssumptionFailedMethod(OceanRunner oceanRunner,
			Failure failure) {
		// Add the statistics result in the cache result
		StatisticsResult sResults = new StatisticsResult();
		sResults.setComments(failure.getMessage() + "\n> Trace:" + failure.getTrace());
		sResults.setRunDate(new Date());
		sResults.setStatus(StatusTestResult.ASSUMPTION_FAILED);
		sResults.setThrowable(failure.getException());
		sResults.setClassUnderTestName(failure.getDescription().getClassName());
		sResults.setMethodUnderTestName(failure.getDescription().getMethodName());

		enhanceThrowable(failure.getException(), "Assumption enhanced");

		actualResultsMap.put(failure.getDescription().getMethodName(), sResults);
	}

	@Override
	public void doAfterEachTestedMethod(final OceanRunner oceanRunner, final Description description) throws OceanModuleException {
		StatisticsResult sResult = actualResultsMap.get(description.getMethodName());
		// Doesn't exist -> The test was successful
		if (sResult == null) {
			// Add the statistics result in the cache result
			StatisticsResult sResults = new StatisticsResult();
			sResults.setRunDate(new Date());
			sResults.setStatus(StatusTestResult.FAILED);
			sResults.setClassUnderTestName(description.getClassName());
			sResults.setMethodUnderTestName(description.getMethodName());

			actualResultsMap.put(description.getMethodName(), sResults);
		}
	}

	/**
	 * @param klass
	 * @return the list of the name of the method used for the test
	 */
	private List<String> loadTestsToSearch(final Class<?> klass) {
		List<String> methodsList = new ArrayList<String>();

		Method[] methodArray = klass.getMethods();
		for (Method method : methodArray) {
			if ((method != null) && (method.getAnnotation(Test.class) != null)) {
				methodsList.add(method.getName());
			}
		}

		return methodsList;
	}

	private Throwable enhanceThrowable(final Throwable throwable, String message) {
		try {
			Field field = Throwable.class.getDeclaredField("detailMessage");
			field.setAccessible(true);
			String detailMessage = (String) field.get(throwable);
			
			String enhancedMessage;
			if (detailMessage == null) {
				enhancedMessage = message;
			} else {
				enhancedMessage = detailMessage + " - " + message;
			}
			
			field.set(throwable, enhancedMessage);

		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return throwable;
	}



}
