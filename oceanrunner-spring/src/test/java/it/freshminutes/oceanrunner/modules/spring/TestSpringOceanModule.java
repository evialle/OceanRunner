/**
 * 
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
