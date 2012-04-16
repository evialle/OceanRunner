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
package it.freshminutes.oceanrunner.modules.statistics;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.modules.concurrent.OceanRunTestsInDedicatedThreads;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Eric Vialle
 *
 */
@RunWith(OceanRunner.class)
@OceanRunTestsInDedicatedThreads()
public class Test2StatisticsOceanModuleWithEBean {

	private static Server server;

	//@BeforeClass
	public static void startDb() throws SQLException {
		System.out.println("Starting DB server");

		server = Server.createTcpServer(null).start();
	}

	@Test
	public void successfulTest() {
		Assert.assertTrue(true);
	}

	@Test
	public void assertionFailedTest() {
		Assert.assertEquals("la", "et non!");
	}

	@Test
	@Ignore
	public void ignoreTest() {
		Assert.assertTrue("assertTrue with false", false);
	}

	@Test
	public void exceptionFailedTest() throws Exception {
		throw new Exception("marche pas");
	}

	//@AfterClass
	public static void stopDb() throws InterruptedException {
		System.out.println("Stopping DB server");
		server.stop();
	}

}
