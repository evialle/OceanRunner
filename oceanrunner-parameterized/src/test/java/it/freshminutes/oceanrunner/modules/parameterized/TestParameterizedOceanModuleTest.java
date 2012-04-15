/**
 * 
 */
package it.freshminutes.oceanrunner.modules.parameterized;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;


/**
 * @author Eric
 *
 */
@RunWith(OceanRunner.class)
@OceanModulesToUse(ParameterizedOceanModule.class)
public class TestParameterizedOceanModuleTest {

	 String validEmail;
	
	@Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {"a@a.pl" /*more params here*/}, {"exmaple@example.com", /* more params here*/},
        });
    }
    
    @Test
    public void testIsValidEmail() throws Exception {
        Assert.assertNotNull(validEmail);
    }
}
