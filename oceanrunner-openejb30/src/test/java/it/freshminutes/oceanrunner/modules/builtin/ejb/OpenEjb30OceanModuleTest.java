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
package it.freshminutes.oceanrunner.modules.builtin.ejb;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;

import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(OceanRunner.class)
@OceanModulesToUse(OpenEjb30OceanModule.class)
public class OpenEjb30OceanModuleTest {
	
	IHelloWorld helloWorldEjb;
	
	@Test
	public void testBasic() throws NamingException, OceanModuleException {
		helloWorldEjb = (IHelloWorld) OpenEjb30OceanModule.lookupEJB(HelloWorldEjb30.class);

		Assert.assertTrue(helloWorldEjb != null);

		int returnValue = helloWorldEjb.returnOne();
		Assert.assertEquals(1, returnValue);
	}

}
