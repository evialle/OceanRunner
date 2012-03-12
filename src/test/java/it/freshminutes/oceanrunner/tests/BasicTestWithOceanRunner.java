/**
 * 
 */
package it.freshminutes.oceanrunner.tests;

import static org.junit.Assert.assertTrue;
import it.freshminutes.oceanrunner.OceanRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Basic demo of OceanRunner
 * 
 * @author Eric Vialle
 */
@RunWith(OceanRunner.class)
public class BasicTestWithOceanRunner {

	@Test
	public final void thisIsATest() {
		assertTrue(true);
	}

}
