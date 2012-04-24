/**
 * 
 */
package it.freshminutes.oceanrunner.modules.statistics;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;

/**
 * @author Eric
 * 
 */
public abstract class MyBasicTestCase {

	@Rule
	public TestRule timeout = new Timeout(150);
	
	
	@Rule
    public TemporaryFolder folder = new TemporaryFolder();

	@Before
	public void before() {

	}

	@After
	public void after() {

	}
}
