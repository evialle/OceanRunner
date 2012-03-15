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
import it.freshminutes.oceanrunner.modules.engine.OceanModule;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

/**
 * Not developped.
 * 
 * 
 * @author Eric Vialle
 * 
 */
public class StatisticsOceanModule extends OceanModule {
	
	private static final String JDBC_DRIVER_CLASSNAME = "statistics.jdb.driver";

	private static final String JDBC_USER = "statistics.jdbc.user";

	private static final String JDBC_PASSWORD = " statistics.jdbc.password";

	private static final String JDBC_URL = "statistics.jdbc.url";
	
	private static final String  ENV = "statistics.env";
	
	private static final String  APPLINAME = "statistics.appliname";

	private OceanRunner oceanRunner;
	
	private static Connection dbConn = null;
	
	private Connection getDbConn() throws OceanModuleException {
		if (StatisticsOceanModule.dbConn == null) {
			try {
				String jdbcClassName= oceanRunner.getProperties().getProperty(JDBC_DRIVER_CLASSNAME);
				String jdbcUser = oceanRunner.getProperties().getProperty(JDBC_USER);
				String jdbcPass = oceanRunner.getProperties().getProperty(JDBC_PASSWORD);
				String jdbcUrl = oceanRunner.getProperties().getProperty(JDBC_URL);
				Class.forName(jdbcClassName);
				StatisticsOceanModule.dbConn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				StatisticsOceanModule.dbConn = null;
				throw new OceanModuleException(e);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				StatisticsOceanModule.dbConn = null;
				throw new OceanModuleException(e);
			}
		}
		return StatisticsOceanModule.dbConn;
	}

	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modules.engine.OceanModule#doBeforeAllTestedMethods(it.freshminutes.oceanrunner.OceanRunner, java.lang.Class)
	 */
	@Override
	public void doBeforeAllTestedMethods(OceanRunner oceanRunner, Class<?> klass) {
		// TODO Auto-generated method stub
		

	}

	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modules.engine.OceanModule#doAfterAllTestedMethods(it.freshminutes.oceanrunner.OceanRunner, java.lang.Class)
	 */
	@Override
	public void doAfterAllTestedMethods(OceanRunner oceanRunner, Class<?> klass) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modules.engine.OceanModule#doBeforeEachTestedMethod(it.freshminutes.oceanrunner.OceanRunner)
	 */
	@Override
	public void doBeforeEachTestedMethod(OceanRunner oceanRunner) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modules.engine.OceanModule#doAfterEachTestedMethod(it.freshminutes.oceanrunner.OceanRunner, org.junit.runner.Description)
	 */
	@Override
	public void doAfterEachTestedMethod(OceanRunner oceanRunner,
			Description description) {
		// TODO Auto-generated method stub

	}



	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modules.engine.OceanModule#doAfterEachFailedMethod(it.freshminutes.oceanrunner.OceanRunner, org.junit.runner.notification.Failure)
	 */
	@Override
	public void doAfterEachFailedMethod(OceanRunner oceanRunner, Failure failure) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modules.engine.OceanModule#doAfterEachIgnoredMethod(it.freshminutes.oceanrunner.OceanRunner, org.junit.runner.Description)
	 */
	@Override
	public void doAfterEachIgnoredMethod(OceanRunner oceanRunner,
			Description description) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see it.freshminutes.oceanrunner.modules.engine.OceanModule#doAfterEachAssumptionFailedMethod(it.freshminutes.oceanrunner.OceanRunner, org.junit.runner.notification.Failure)
	 */
	@Override
	public void doAfterEachAssumptionFailedMethod(OceanRunner oceanRunner,
			Failure failure) {
		// TODO Auto-generated method stub

	}

}
