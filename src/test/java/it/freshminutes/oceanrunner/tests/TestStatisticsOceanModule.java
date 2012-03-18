package it.freshminutes.oceanrunner.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;
import it.freshminutes.oceanrunner.modules.builtin.StatisticsOceanModule;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(OceanRunner.class)
@OceanModulesToUse(StatisticsOceanModule.class)
public class TestStatisticsOceanModule {

	@Test
	public void dummyTest() {
		assertTrue(true);
	}

	@Test
	public void dummyFailedAssumptionTest() {
		assertFalse(true);
	}

	@Test
	public void dummyExceptionTest() throws Exception {
		throw new Exception("that works!");
	}
}
