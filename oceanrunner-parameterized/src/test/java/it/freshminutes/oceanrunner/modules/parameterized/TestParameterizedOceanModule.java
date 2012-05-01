/**
 * 
 */
package it.freshminutes.oceanrunner.modules.parameterized;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToAddToDefault;
import it.freshminutes.oceanrunner.modules.parameterized.annotations.OceanParameterizedVariable;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;


/**
 * @author Eric
 *
 */
@RunWith(OceanRunner.class)
@OceanModulesToAddToDefault(ParameterizedOceanModule.class)
public class TestParameterizedOceanModule {
	
	@OceanParameterizedVariable
	public static String firstValue;
	
	@OceanParameterizedVariable
	public static String secondValue;
	
	
	@Parameters
	public static List<Object[]> parametersList() {
		Object[] first = new Object[] {"Hello1", "World1"};
		Object[] second = new Object[]{"Hello2", "World2"};

		ArrayList<Object[]> toReturn = new ArrayList<Object[]>();
		toReturn.add(first);
		toReturn.add(second);
		
		return toReturn;
	}
	
	@Test
	public void testBasic() {
		System.out.println(firstValue + " - " + secondValue);
		
		Assert.assertNotNull(firstValue);
		Assert.assertNotNull(secondValue);
	}

}
