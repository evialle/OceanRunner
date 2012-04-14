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
package it.freshminutes.oceanrunner.modules.statistics;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;
import it.freshminutes.oceanrunner.modules.statistics.StatisticsResult.StatusTestResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

/**
 * Not developped.
 * 
 * 
 * @author Eric Vialle
 * 
 */
public class StatisticsOceanModule extends OceanModule {

	private static final String STATISTICS_DATAPLUG_PROPERTYKEY = "statistics.dataplug";

	/** System property name defining the statistics environment. */
	public static final String STATISTICS_ENVIRONEMENT_PROPERTYKEY = "statistics.environement";

	private static final String STATISTICS_JPA_COMMENTSSIZE_PROPERTYKEY = "statistics.commentssize";

	private static final int STATISTICS_JPA_COMMENTSSIZE_DEFAULT = 256;

	/**
	 * System property name defining if the user may write statistics or if it
	 * only use it.
	 */
	public static final String STATISTICS_AUTHORIZEDWRITE_PROPERTYKEY = "statistics.authorizedwrite";
	
	/** System property defining the name of the project. */
	public static final String STATISTICS_PROJECT_PROPERTYKEY = "statistics.project";
	
	/** System property defining the version of the project. */
	public static final String STATISTICS_VERSION_PROPERTYKEY = "statistics.version";

	/** SimpleDateFormat static. */
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	private Map<String, StatisticsResult> actualResultsMap = Maps.newHashMap();

	private StatisticsDataPlug statisticsDataPlug;

	/** Environement name where the unit test is run. */
	private String environment;
	
	private ThreadLocal<Long> startTestThreadLocal = new ThreadLocal<Long>();

	private String project;

	private String version;

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.freshminutes.oceanrunner.modules.engine.OceanModule#
	 * doBeforeAllTestedMethods(it.freshminutes.oceanrunner.OceanRunner,
	 * java.lang.Class)
	 */
	@Override
	public void doBeforeAllTestedMethods(final OceanRunner oceanRunner,
			Class<?> klass) throws OceanModuleException {

		String statisticsDataPlugClassName = oceanRunner.getAwareProperty(STATISTICS_DATAPLUG_PROPERTYKEY);
		
		if (statisticsDataPlugClassName == null) {
			throw new OceanModuleException(
					"Supply the property "
							+ STATISTICS_DATAPLUG_PROPERTYKEY
							+ ", to run StatisticsOceanModule with a valid class extending StatisticsDataPlug");
		}

		try {

			// get constructor that takes a String as argument
			klass = Class.forName(statisticsDataPlugClassName);
			if (klass == null) {
				throw new OceanModuleException(
						"Supply the property "
								+ STATISTICS_DATAPLUG_PROPERTYKEY
								+ ", to run StatisticsOceanModule with a valid class extending StatisticsDataPlug");
			}
			Constructor<?> constructor = klass.getConstructor(OceanRunner.class);

			statisticsDataPlug = (StatisticsDataPlug) constructor.newInstance(oceanRunner);

			this.actualResultsMap = Maps.newHashMap();
			this.environment = oceanRunner.getAwareProperty(STATISTICS_ENVIRONEMENT_PROPERTYKEY, "default");
			this.project = oceanRunner.getAwareProperty(STATISTICS_PROJECT_PROPERTYKEY, "default");;
			this.version = oceanRunner.getAwareProperty(STATISTICS_VERSION_PROPERTYKEY, "default");

		} catch (ClassNotFoundException e) {
			throw new OceanModuleException(
					"Supply the property "
							+ STATISTICS_DATAPLUG_PROPERTYKEY
							+ ", to run StatisticsOceanModule with a valid class extending StatisticsDataPlug",
					e);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.freshminutes.oceanrunner.modules.engine.OceanModule#
	 * doAfterAllTestedMethods(it.freshminutes.oceanrunner.OceanRunner,
	 * java.lang.Class)
	 */
	@Override
	public void doAfterAllTestedMethods(final OceanRunner oceanRunner, final Class<?> klass) throws OceanModuleException {
		String writeAuthorizedString = oceanRunner.getAwareProperty(STATISTICS_AUTHORIZEDWRITE_PROPERTYKEY, "true");
		boolean writeAuthorized = Boolean.parseBoolean(writeAuthorizedString);
		if (writeAuthorized) {
			statisticsDataPlug.storeLastTestStatus(actualResultsMap.values());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.freshminutes.oceanrunner.modules.engine.OceanModule#
	 * doAfterEachFailedMethod(it.freshminutes.oceanrunner.OceanRunner,
	 * org.junit.runner.notification.Failure)
	 */
	@Override
	public void doAfterEachFailedMethod(final OceanRunner oceanRunner, final Failure failure) throws OceanModuleException {
		long duration = getFinalTime();

		// Add the statistics result in the cache result
		StatisticsResult sResult = new StatisticsResult();
		String comments;
		if (failure.getMessage() == null) {
			comments = failure.getTrace();
		} else {
			comments = failure.getMessage() + " #Trace: " + failure.getException().getStackTrace()[0];
		}
		sResult.setComments(optimizeCommentsSize(oceanRunner, comments));
		sResult.setRunDate(new Date());
		sResult.setStatus(StatusTestResult.FAILED);
		sResult.setThrowable(failure.getException());
		sResult.setEnvironment(this.environment);
		sResult.setProject(this.project);
		sResult.setVersion(this.version);
		sResult.setClassUnderTestName(failure.getDescription().getClassName());
		sResult.setMethodUnderTestName(failure.getDescription().getMethodName());
		sResult.setDuration(duration);

		actualResultsMap.put(failure.getDescription().getMethodName(), sResult);

		// Enhance the exception
		String statisticsMsg = processStatisticMessage(failure);
		enhanceThrowable(failure.getException(), statisticsMsg);
	}

	/**
	 * @param commentsSize
	 * @param comments
	 * @return
	 * @throws OceanModuleException
	 */
	private String optimizeCommentsSize(OceanRunner oceanRunner, String comments)
			throws OceanModuleException {
		int commentsSize = commentsSize(oceanRunner);
		if ((Strings.isNullOrEmpty(comments))) {
			return comments;
		} else if (comments.length() < commentsSize) {
			return comments.replace(System.getProperty("line.separator"),",");
		}else {
			return new String(comments.substring(0, commentsSize - 4) + "...").replace(System.getProperty("line.separator"),",");
		}
	}

	/**
	 * @param oceanRunner
	 * @return
	 * @throws OceanModuleException
	 */
	private int commentsSize(final OceanRunner oceanRunner)
			throws OceanModuleException {
		String commentsSizeStr = oceanRunner.getAwareProperty(
				STATISTICS_JPA_COMMENTSSIZE_PROPERTYKEY,
				Integer.toString(STATISTICS_JPA_COMMENTSSIZE_DEFAULT));
		int commentsSize = Integer.parseInt(commentsSizeStr);
		return commentsSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.freshminutes.oceanrunner.modules.engine.OceanModule#
	 * doAfterEachIgnoredMethod(it.freshminutes.oceanrunner.OceanRunner,
	 * org.junit.runner.Description)
	 */
	@Override
	public void doAfterEachIgnoredMethod(final OceanRunner oceanRunner, final Description description) {

		// Add the statistics result in the cache result
		StatisticsResult sResult = new StatisticsResult();
		sResult.setRunDate(new Date());
		sResult.setStatus(StatusTestResult.IGNORE);
		sResult.setClassUnderTestName(description.getClassName());
		sResult.setMethodUnderTestName(description.getMethodName());
		sResult.setEnvironment(this.environment);
		sResult.setProject(this.project);
		sResult.setVersion(this.version);

		actualResultsMap.put(description.getMethodName(), sResult);
	}

	public void doAfterEachAssertionFailedMethod(final OceanRunner oceanRunner, final Failure failure) throws OceanModuleException {
		
		long duration = getFinalTime();


		// Add the statistics result in the cache result
		StatisticsResult sResult = new StatisticsResult();
		String comments;

		if (failure.getMessage() == null) {
			comments = failure.getTrace();
		} else {
			comments = failure.getMessage();
		}
		String optimizeCommentsSize = optimizeCommentsSize(oceanRunner, comments);
		sResult.setComments(optimizeCommentsSize);
		sResult.setRunDate(new Date());
		sResult.setStatus(StatusTestResult.ASSERTION_FAILED);
		sResult.setThrowable(failure.getException());
		sResult.setClassUnderTestName(failure.getDescription().getClassName());
		sResult.setEnvironment(this.environment);
		sResult.setProject(this.project);
		sResult.setVersion(this.version);
		sResult.setMethodUnderTestName(failure.getDescription().getMethodName());
		sResult.setDuration(duration);

		actualResultsMap.put(failure.getDescription().getMethodName(), sResult);

		// Enhance the exception
		String statisticsMsg = processStatisticMessage(failure);
		enhanceThrowable(failure.getException(), statisticsMsg);
	}

	/**
	 * 
	 */
	public void doBeforeEachTestedMethod(final OceanRunner oceanRunner) throws OceanModuleException {
		//used to compute the duration of the test
		startTestThreadLocal.set(System.currentTimeMillis());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.freshminutes.oceanrunner.modules.engine.OceanModule#
	 * doAfterEachAssumptionFailedMethod
	 * (it.freshminutes.oceanrunner.OceanRunner,
	 * org.junit.runner.notification.Failure)
	 */
	@Override
	public void doAfterEachAssumptionFailedMethod(final OceanRunner oceanRunner, final Failure failure) {
		long duration = getFinalTime();
		
		// Add the statistics result in the cache result
		StatisticsResult sResult = new StatisticsResult();
		if (failure.getMessage() == null) {
			sResult.setComments(failure.getTrace());
		} else {
			sResult.setComments(failure.getMessage() + " >> Trace: " + failure.getTrace());
		}
		sResult.setRunDate(new Date());
		sResult.setStatus(StatusTestResult.ASSUMPTION_FAILED);
		sResult.setThrowable(failure.getException());
		sResult.setClassUnderTestName(failure.getDescription().getClassName());
		sResult.setEnvironment(this.environment);
		sResult.setProject(this.project);
		sResult.setVersion(this.version);
		sResult.setMethodUnderTestName(failure.getDescription().getMethodName());
		sResult.setDuration(duration);

		actualResultsMap.put(failure.getDescription().getMethodName(), sResult);

		// Enhance the exception
		String statisticsMsg = processStatisticMessage(failure);
		enhanceThrowable(failure.getException(), statisticsMsg);
	}

	/**
	 * Build report for a given failure.
	 * 
	 * @param failure
	 * @return
	 */
	private String processStatisticMessage(final Failure failure) {
		
		List<StatisticsResult> statisticsResultList = statisticsDataPlug.loadTestStatus(failure.getDescription().getMethodName());

		StringBuilder msgBuilder = new StringBuilder();
		StatusTestResult lastStatusTestResult = StatusTestResult.SUCCESS;
		
		String lastComments = "";
		
		for (StatisticsResult statisticsResult : statisticsResultList) {

			if (StatusTestResult.SUCCESS.equals(statisticsResult.getStatus())) {
				String date = SDF.format(statisticsResult.getRunDate());
				msgBuilder.append("Last success of ")
						.append(failure.getDescription().getMethodName())
						.append(", the ").append(date)
						.append(" with duration: ").append(statisticsResult.getDuration()).append("ms");
				break;
			} else if (!lastStatusTestResult.equals(statisticsResult.getStatus()) || !(lastComments.equals(Strings.nullToEmpty(statisticsResult.getComments())))) {
				lastStatusTestResult = statisticsResult.getStatus();
				lastComments = Strings.nullToEmpty(statisticsResult.getComments());

				String date = SDF.format(statisticsResult.getRunDate());

				msgBuilder.append("Last ")
						.append(statisticsResult.getStatus().name())
						.append(" of ")
						.append(failure.getDescription().getMethodName())
						.append(", the ").append(date);
						if (! Strings.isNullOrEmpty(statisticsResult.getComments())) {
							msgBuilder.append(", with comments: ").append(statisticsResult.getComments());
						}
				msgBuilder.append(" / ");
			}
			
		}
		return msgBuilder.toString();
	}

	@Override
	public void doAfterEachTestedMethod(final OceanRunner oceanRunner, final Description description) throws OceanModuleException {
		long duration = getFinalTime();
		
		StatisticsResult sResultFromCache = actualResultsMap.get(description.getMethodName());
		// Doesn't exist -> The test was successful
		if (sResultFromCache == null) {
			// Add the statistics result in the cache result
			StatisticsResult sResult = new StatisticsResult();
			sResult.setRunDate(new Date());
			sResult.setStatus(StatusTestResult.SUCCESS);
			sResult.setClassUnderTestName(description.getClassName());
			sResult.setMethodUnderTestName(description.getMethodName());
			sResult.setEnvironment(this.environment);
			sResult.setProject(this.project);
			sResult.setVersion(this.version);
			sResult.setDuration(duration);

			actualResultsMap.put(description.getMethodName(), sResult);
		}
	}

	/**
	 * Modify a throwable to include a message.
	 * 
	 * @param throwable
	 * @param message
	 * @return
	 */
	private Throwable enhanceThrowable(final Throwable throwable, String message) {
		try {
			Field field = Throwable.class.getDeclaredField("detailMessage");
			field.setAccessible(true);
			String detailMessage = throwable.getMessage();

			String enhancedMessage;
			if (Strings.isNullOrEmpty(detailMessage)) {
				enhancedMessage = message;
			} else if (Strings.isNullOrEmpty(message)){
				enhancedMessage = detailMessage;
			} else {
				enhancedMessage = detailMessage + " / " + message;
			}

			field.set(throwable, enhancedMessage);

		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return throwable;
	}
	
	/** used to process the duration of a test. */
	private long getFinalTime() {
		return System.currentTimeMillis() - startTestThreadLocal.get();
	}

}
