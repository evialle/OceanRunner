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
package it.freshminutes.oceanrunner.modules.spring;

import static org.junit.Assert.*;
import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.annotations.OceanModulesToUse;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


/**
 * @author Eric Vialle
 */
@RunWith(OceanRunner.class)
@OceanModulesToUse(SpringOceanModule.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class TestSpringOceanModule {

    @Autowired
    TestComponent testComponent;
	
	@Test
	public void basicTest() {
		int value = testComponent.mustReturnOne();
		assertEquals(1, value);
	}
	
	@Ignore
	public void ignoreTest() {
		Assert.fail("This test is marked as ignored");
	}
}
