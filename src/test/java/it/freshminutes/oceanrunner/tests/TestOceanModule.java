/**
 * 
 */
package it.freshminutes.oceanrunner.tests;

import static org.junit.Assert.assertTrue;
import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;
import it.freshminutes.oceanrunner.modules.builtin.DemoOceanModule;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Demo of OceanModule
 * 
 * @author Eric Vialle
 */
@RunWith(OceanRunner.class)
@OceanModulesToUse(DemoOceanModule.class)
public class TestOceanModule {

	@Test
	public void myTest() {
		assertTrue(true);
	}

	@Test
	public void myTestAgain() {
		assertTrue(true);
	}
}
