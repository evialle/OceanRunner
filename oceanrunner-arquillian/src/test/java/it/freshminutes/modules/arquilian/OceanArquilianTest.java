/**
 * 
 */
package it.freshminutes.modules.arquilian;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToAddToDefault;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Eric
 *
 */
@RunWith(OceanRunner.class)
@OceanModulesToAddToDefault(OceanArquilianModule.class)
public class OceanArquilianTest {
	
	@Test 
	public void myTest() {
		System.out.println("Hello World");
	}
}
