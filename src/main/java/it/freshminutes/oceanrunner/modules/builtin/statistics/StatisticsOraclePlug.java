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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Eric
 *
 */
public class StatisticsOraclePlug extends StatisticsDataPlug {

	private static Connection dbConn;

	private static final String JDBC_DRIVER_CLASSNAME = "statistics.jdb.driver";

	private static final String JDBC_USER = "statistics.jdbc.user";

	private static final String JDBC_PASSWORD = " statistics.jdbc.password";

	private static final String JDBC_URL = "statistics.jdbc.url";

	private static final String ENV = "statistics.env";

	private static final String APPLINAME = "statistics.appliname";

	@Override
	public Map<String, StatisticsResult> loadLastTestStatus(List<String> testsToSearch) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void storeLastTestStatus(Collection<StatisticsResult> statisticsResultsList) {
		
	}

	/**
	 * Constructor.
	 * 
	 * @param oceanRunner
	 * @throws OceanModuleException
	 */
	public StatisticsOraclePlug(final OceanRunner oceanRunner) throws OceanModuleException {
		super(oceanRunner);
		if (StatisticsOraclePlug.dbConn == null) {
			try {
				String jdbcClassName = oceanRunner.getAwareProperty(JDBC_DRIVER_CLASSNAME);
				String jdbcUser = oceanRunner.getAwareProperty(JDBC_USER);
				String jdbcPass = oceanRunner.getAwareProperty(JDBC_PASSWORD);
				String jdbcUrl = oceanRunner.getAwareProperty(JDBC_URL);
				Class.forName(jdbcClassName);
				StatisticsOraclePlug.dbConn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
			} catch (ClassNotFoundException e) {
				StatisticsOraclePlug.dbConn = null;
				throw new OceanModuleException(e);
			} catch (SQLException e) {
				StatisticsOraclePlug.dbConn = null;
				throw new OceanModuleException(e);
			}
		}
	}

	private Connection getDbConn() throws OceanModuleException {
		return StatisticsOraclePlug.dbConn;
	}

}
