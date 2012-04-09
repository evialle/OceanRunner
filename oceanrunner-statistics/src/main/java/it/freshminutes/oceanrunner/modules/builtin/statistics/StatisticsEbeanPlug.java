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
package it.freshminutes.oceanrunner.modules.builtin.statistics;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;
import it.freshminutes.oceanrunner.modules.builtin.StatisticsOceanModule;

import java.util.Collection;
import java.util.List;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.Query;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;

public class StatisticsEbeanPlug extends StatisticsDataPlug {


	private static final int MAX_RUN_TO_STUDY_DEFAULT = 30;

	/** Database server. */
	private EbeanServer dbServer;

	private int maxRunToStudy;

	private String environment;

	public StatisticsEbeanPlug(final OceanRunner oceanRunner) throws OceanModuleException {
		super(oceanRunner);

		// Define DataSource parameters
		DataSourceConfig myDbConfig = new DataSourceConfig();
		myDbConfig.setDriver(oceanRunner.getAwareProperty("statistics.jpa.jdbc.driver"));
		myDbConfig.setUsername(oceanRunner.getAwareProperty("statistics.jpa.jdbc.user"));
		myDbConfig.setPassword(oceanRunner.getAwareProperty("statistics.jpa.jdbc.password"));
		myDbConfig.setUrl(oceanRunner.getAwareProperty("statistics.jpa.jdbc.url"));

		ServerConfig config = new ServerConfig();
		config.setName("StatisticsEbeanPlug");
		config.setDataSourceConfig(myDbConfig);
		config.setDdlGenerate(true);
		config.setDdlRun(true);
		config.setDefaultServer(true);

		config.addClass(StatisticsResult.class);
		
		this.dbServer = EbeanServerFactory.create(config);
		
		
		//Properties for StatisticsEbeanPlug
		this.environment = oceanRunner.getAwareProperty(StatisticsOceanModule.STATISTICS_ENVIRONEMENT_PROPERTY, "default");
		String maxRunToStudy = oceanRunner.getAwareProperty("statistics.maxRunToStudy", Integer.toString(MAX_RUN_TO_STUDY_DEFAULT));
		try {
			this.maxRunToStudy = Integer.parseInt(maxRunToStudy);
		} catch (NumberFormatException e) {
			this.maxRunToStudy = MAX_RUN_TO_STUDY_DEFAULT;
		}
	}


	@Override
	public List<StatisticsResult> loadTestStatus(String testsToSearch) {

		Query<StatisticsResult> selectResultsQuery = this.dbServer.find(StatisticsResult.class);
		selectResultsQuery.where().eq("classUnderTestName", oceanRunner.getClassUnderTest().getName()).eq("methodUnderTestName", testsToSearch)
				.eq("environement", this.environment).orderBy()
				.desc("runDate").setMaxRows(this.maxRunToStudy);
		

		List<StatisticsResult> lastStatusList = selectResultsQuery.findList();

		return lastStatusList;
	}

	@Override
	public void storeLastTestStatus(Collection<StatisticsResult> statisticsResultsList) {
		this.dbServer.save(statisticsResultsList);
	}

}
