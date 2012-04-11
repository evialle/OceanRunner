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
import it.freshminutes.oceanrunner.modules.statistics.StatisticsOceanModule;

import java.util.Collection;
import java.util.List;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.Query;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;

public class StatisticsEbeanPlug extends StatisticsDataPlug {

	/** Property key defining the maximum statistics to analyse for a method. */
	private static final String STATISTICS_MAX_RUN_TO_STUDY_PROPERTYKEY = "statistics.maxRunToStudy";

	private static final int MAX_RUN_TO_STUDY_DEFAULT = 30;

	/** Class of the JDBC driver, property key. */
	private static final String STATISTICS_JPA_JDBC_DRIVER_PROPERTYKEY = "statistics.jpa.jdbc.driver";

	/** Username to access db, property key. */
	private static final String STATISTICS_JPA_JDBC_USER_PROPERTYKEY = "statistics.jpa.jdbc.user";

	/** Password to access db, property key. */
	private static final String STATISTICS_JPA_JDBC_PASSWORD_PROPERTYKEY = "statistics.jpa.jdbc.password";

	/** JDBC URL, property key. */
	private static final String STATISTICS_JPA_JDBC_URL_PROPERTYKEY = "statistics.jpa.jdbc.url";

	/** Database server. */
	private static EbeanServer DB_SERVER = null;

	/** The maximum statistics to analyse for a method. */
	private int maxRunToStudy;

	/** Name of the environnement tested. */
	private String environment;

	/**
	 * 
	 * @param oceanRunner
	 * @throws OceanModuleException
	 */
	public StatisticsEbeanPlug(final OceanRunner oceanRunner)
			throws OceanModuleException {
		super(oceanRunner);

		if (DB_SERVER == null) {
			// Define DataSource parameters
			DataSourceConfig myDbConfig = new DataSourceConfig();
			myDbConfig.setDriver(oceanRunner.getAwareProperty(STATISTICS_JPA_JDBC_DRIVER_PROPERTYKEY));
			myDbConfig.setUsername(oceanRunner.getAwareProperty(STATISTICS_JPA_JDBC_USER_PROPERTYKEY));
			myDbConfig.setPassword(oceanRunner.getAwareProperty(STATISTICS_JPA_JDBC_PASSWORD_PROPERTYKEY));
			myDbConfig.setUrl(oceanRunner.getAwareProperty(STATISTICS_JPA_JDBC_URL_PROPERTYKEY));

			ServerConfig config = new ServerConfig();
			config.setName("StatisticsEbeanPlug");
			config.setDataSourceConfig(myDbConfig);
			//config.setDdlGenerate(true);
			//config.setDdlRun(true);
			config.setDefaultServer(true);

			config.addClass(StatisticsResult.class);

			DB_SERVER = EbeanServerFactory.create(config);

			// Properties for StatisticsEbeanPlug
			this.environment = oceanRunner.getAwareProperty(
					StatisticsOceanModule.STATISTICS_ENVIRONEMENT_PROPERTYKEY,
					"default");
			String maxRunToStudy = oceanRunner.getAwareProperty(
					STATISTICS_MAX_RUN_TO_STUDY_PROPERTYKEY,
					Integer.toString(MAX_RUN_TO_STUDY_DEFAULT));
			try {
				this.maxRunToStudy = Integer.parseInt(maxRunToStudy);
			} catch (NumberFormatException e) {
				this.maxRunToStudy = MAX_RUN_TO_STUDY_DEFAULT;
			}
		}
	}

	@Override
	public List<StatisticsResult> loadTestStatus(String testsToSearch) {

		Query<StatisticsResult> selectResultsQuery = DB_SERVER
				.find(StatisticsResult.class);
		selectResultsQuery
				.where()
				.eq("classundertestname",
						oceanRunner.getClassUnderTest().getName())
				.eq("methodundertestname", testsToSearch)
				.eq("environment", this.environment).orderBy().desc("rundate")
				.setMaxRows(this.maxRunToStudy);

		List<StatisticsResult> lastStatusList = selectResultsQuery.findList();

		return lastStatusList;
	}

	@Override
	public void storeLastTestStatus(
			Collection<StatisticsResult> statisticsResultsList) {
		DB_SERVER.save(statisticsResultsList);
	}

}
