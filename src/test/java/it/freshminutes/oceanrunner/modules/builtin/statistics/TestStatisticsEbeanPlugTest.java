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
/**
 * 
 */
package it.freshminutes.oceanrunner.modules.builtin.statistics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.modules.builtin.statistics.StatisticsResult.StatusTestResult;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.avaje.ebean.Ebean;
import com.google.common.collect.Lists;

/**
 * @author Eric Vialle
 *
 */
public class TestStatisticsEbeanPlugTest {

	private OceanRunner oceanRunner;
	private StatisticsEbeanPlug sep;
	private static Server server;

	@BeforeClass
	public static void beforeClass() throws SQLException {
		System.out.println("Starting DB server");

		server = Server.createWebServer(null).start();

	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		oceanRunner = new OceanRunner(TestStatisticsEbeanPlugTest.class);
		sep = new StatisticsEbeanPlug(oceanRunner);
	}

	/**
	 * Test method for
	 * {@link it.freshminutes.oceanrunner.modules.builtin.statistics.StatisticsEbeanPlug#storeLastTestStatus(java.util.Collection)}
	 * .
	 */
	@Test
	public void testStoreLastTestStatus() {
		List<StatisticsResult> statisticsResultsList = createStatisticsSample();

		sep.storeLastTestStatus(statisticsResultsList);

		List<StatisticsResult> list = Ebean.find(StatisticsResult.class).findList();
		assertEquals(statisticsResultsList.size(), list.size());
	}

	/**
	 * @return
	 */
	private List<StatisticsResult> createStatisticsSample() {
		List<StatisticsResult> statisticsResultsList = Lists.newArrayList();

		// 1- Results
		StatisticsResult r1 = new StatisticsResult();
		r1.setClassUnderTestName(TestStatisticsEbeanPlugTest.class.getName());
		r1.setComments("great");
		r1.setMethodUnderTestName("testStoreLastTestStatus");
		r1.setStatus(StatusTestResult.SUCCESS);
		r1.setEnvironement("default");
		statisticsResultsList.add(r1);

		StatisticsResult r2 = new StatisticsResult();
		r2.setClassUnderTestName(TestStatisticsEbeanPlugTest.class.getName());
		r2.setComments("great");
		r2.setMethodUnderTestName(TestStatisticsEbeanPlugTest.class.getName());
		r2.setStatus(StatusTestResult.SUCCESS);
		r2.setEnvironement("default");
		statisticsResultsList.add(r2);

		StatisticsResult r3 = new StatisticsResult();
		r3.setClassUnderTestName(TestStatisticsEbeanPlugTest.class.getName());
		r3.setComments("great");
		r3.setMethodUnderTestName("testStoreLastTestStatus");
		r3.setStatus(StatusTestResult.IGNORE);
		r3.setEnvironement("default");
		statisticsResultsList.add(r3);

		StatisticsResult r4 = new StatisticsResult();
		r4.setClassUnderTestName(TestStatisticsEbeanPlugTest.class.getName());
		r4.setComments("great");
		r4.setMethodUnderTestName("testStoreLastTestStatus");
		r4.setStatus(StatusTestResult.IGNORE);
		r4.setEnvironement("default");
		statisticsResultsList.add(r4);

		StatisticsResult r5 = new StatisticsResult();
		r5.setClassUnderTestName(TestStatisticsEbeanPlugTest.class.getName());
		r5.setComments("great");
		r5.setMethodUnderTestName("testStoreLastTestStatus");
		r5.setStatus(StatusTestResult.FAILED);
		r5.setEnvironement("default");
		statisticsResultsList.add(r5);

		StatisticsResult r6 = new StatisticsResult();
		r6.setClassUnderTestName(TestStatisticsEbeanPlugTest.class.getName());
		r6.setComments("great");
		r6.setMethodUnderTestName("testDifferent");
		r6.setStatus(StatusTestResult.FAILED);
		r6.setEnvironement("default");
		statisticsResultsList.add(r6);

		return statisticsResultsList;
	}

	/**
	 * Test method for {@link it.freshminutes.oceanrunner.modules.builtin.statistics.StatisticsEbeanPlug#loadLastTestStatus(java.util.List)}.
	 */
	@Test
	public void testLoadLastTestStatus() {

		Collection<StatisticsResult> statisticsResultsList = createStatisticsSample();
		sep.storeLastTestStatus(statisticsResultsList);

		List<StatisticsResult> list = sep.loadTestStatus("testStoreLastTestStatus");
		assertEquals(4, list.size());
	}


	/**
	 * Test method for {@link it.freshminutes.oceanrunner.modules.builtin.statistics.StatisticsEbeanPlug#StatisticsEbeanPlug(it.freshminutes.oceanrunner.OceanRunner)}.
	 */
	@Test
	public void testStatisticsEbeanPlug() {
		fail("Not yet implemented");
	}

	@AfterClass
	public static void afterClass() {
		System.out.println("Stopping DB server");
		server.stop();
	}

}
