/**
 * 
 */
package it.freshminutes.oceanrunner.modules.mockito;

import static org.junit.Assert.*;
import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

/**
 * @author Eric
 *
 */
@RunWith(OceanRunner.class)
@OceanModulesToUse(MockitoOceanModule.class)
public class MockitoOceanModuleTest {

	@Mock
	OceanRunner oceanRunnerMock;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		assertNotNull(oceanRunnerMock);
	}

}
