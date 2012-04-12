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

	/** System property name defining the statistics environement. */
	public static final String STATISTICS_ENVIRONEMENT_PROPERTYKEY = "statistics.environement";

	private static final String STATISTICS_JPA_COMMENTSSIZE_PROPERTYKEY = "statistics.commentssize";

	private static final int STATISTICS_JPA_COMMENTSSIZE_DEFAULT = 512;

	/**
	 * System property name defining if the user may write statistics or if it
	 * only use it.
	 */
	public static final String STATISTICS_AUTHORIZEDWRITE_PROPERTYKEY = "statistics.authorizedwrite";

	/** SimpleDateFormat static. */
	private static final SimpleDateFormat SDF = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm");

	private Map<String, StatisticsResult> actualResultsMap = Maps.newHashMap();

	private StatisticsDataPlug statisticsDataPlug;

	/** Environement name where the unit test is run. */
	private String environment;

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

		String statisticsDataPlugClassName = oceanRunner
				.getAwareProperty(STATISTICS_DATAPLUG_PROPERTYKEY);
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
			Constructor<?> constructor = klass
					.getConstructor(OceanRunner.class);

			statisticsDataPlug = (StatisticsDataPlug) constructor
					.newInstance(oceanRunner);

			this.actualResultsMap = Maps.newHashMap();
			this.environment = oceanRunner
					.getAwareProperty(STATISTICS_ENVIRONEMENT_PROPERTYKEY);

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
	public void doAfterAllTestedMethods(final OceanRunner oceanRunner,
			final Class<?> klass) throws OceanModuleException {
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

		// Add the statistics result in the cache result
		StatisticsResult sResults = new StatisticsResult();
		String comments;
		if (failure.getMessage() == null) {
			comments = failure.getTrace();
		} else {
			comments = failure.getMessage() + " >> Trace: " + failure.getTrace();
		}
		sResults.setComments(optimizeCommentsSize(oceanRunner, comments));
		sResults.setRunDate(new Date());
		sResults.setStatus(StatusTestResult.FAILED);
		sResults.setThrowable(failure.getException());
		sResults.setEnvironment(this.environment);
		sResults.setClassUnderTestName(failure.getDescription().getClassName());
		sResults.setMethodUnderTestName(failure.getDescription().getMethodName());
		actualResultsMap.put(failure.getDescription().getMethodName(), sResults);

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
			return comments.replace(System.lineSeparator(),";");
		}else {
			return new String(comments.substring(0, commentsSize - 4) + "...").replace(System.lineSeparator(),";");
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
	public void doAfterEachIgnoredMethod(final OceanRunner oceanRunner,
			final Description description) {
		// Add the statistics result in the cache result
		StatisticsResult sResults = new StatisticsResult();
		sResults.setRunDate(new Date());
		sResults.setStatus(StatusTestResult.IGNORE);
		sResults.setClassUnderTestName(description.getClassName());
		sResults.setMethodUnderTestName(description.getMethodName());
		sResults.setEnvironment(this.environment);
		actualResultsMap.put(description.getMethodName(), sResults);
	}

	public void doAfterEachAssertionFailedMethod(final OceanRunner oceanRunner,
			final Failure failure) throws OceanModuleException {

		// Add the statistics result in the cache result
		StatisticsResult sResults = new StatisticsResult();
		String comments;

		if (failure.getMessage() == null) {
			comments = failure.getTrace();
		} else {
			comments = failure.getMessage() + " >> Trace: " + failure.getTrace();
		}
		String optimizeCommentsSize = optimizeCommentsSize(oceanRunner, comments);
		sResults.setComments(optimizeCommentsSize);
		sResults.setRunDate(new Date());
		sResults.setStatus(StatusTestResult.ASSERTION_FAILED);
		sResults.setThrowable(failure.getException());
		sResults.setClassUnderTestName(failure.getDescription().getClassName());
		sResults.setEnvironment(this.environment);
		sResults.setMethodUnderTestName(failure.getDescription().getMethodName());
		actualResultsMap.put(failure.getDescription().getMethodName(), sResults);

		// Enhance the exception
		String statisticsMsg = processStatisticMessage(failure);
		enhanceThrowable(failure.getException(), failure.getMessage() + "; "+ statisticsMsg);
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
	public void doAfterEachAssumptionFailedMethod(
			final OceanRunner oceanRunner, final Failure failure) {
		// Add the statistics result in the cache result
		StatisticsResult sResults = new StatisticsResult();
		if (failure.getMessage() == null) {
			sResults.setComments(failure.getTrace());
		} else {
			sResults.setComments(failure.getMessage() + " >> Trace: " + failure.getTrace());
		}
		sResults.setRunDate(new Date());
		sResults.setStatus(StatusTestResult.ASSUMPTION_FAILED);
		sResults.setThrowable(failure.getException());
		sResults.setClassUnderTestName(failure.getDescription().getClassName());
		sResults.setEnvironment(this.environment);
		sResults.setMethodUnderTestName(failure.getDescription().getMethodName());
		actualResultsMap.put(failure.getDescription().getMethodName(), sResults);

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
						.append(", the ").append(date);
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
				msgBuilder.append("; ");
			}
			
		}

		return msgBuilder.toString();
	}

	@Override
	public void doAfterEachTestedMethod(final OceanRunner oceanRunner,
			final Description description) throws OceanModuleException {
		StatisticsResult sResult = actualResultsMap.get(description
				.getMethodName());
		// Doesn't exist -> The test was successful
		if (sResult == null) {
			// Add the statistics result in the cache result
			StatisticsResult sResults = new StatisticsResult();
			sResults.setRunDate(new Date());
			sResults.setStatus(StatusTestResult.SUCCESS);
			sResults.setClassUnderTestName(description.getClassName());
			sResults.setMethodUnderTestName(description.getMethodName());
			sResults.setEnvironment(this.environment);

			actualResultsMap.put(description.getMethodName(), sResults);
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
			String detailMessage = (String) field.get(throwable);

			String enhancedMessage;
			if (Strings.isNullOrEmpty(detailMessage)) {
				enhancedMessage = message;
			} else if (Strings.isNullOrEmpty(message)){
				enhancedMessage = detailMessage;
			} else {
				enhancedMessage = detailMessage + " - " + message;
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

}
