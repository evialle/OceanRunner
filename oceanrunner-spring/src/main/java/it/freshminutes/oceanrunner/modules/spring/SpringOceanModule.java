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

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;

import org.springframework.test.context.TestContextManager;

/**
 * SpringOceanModule provides functionality of the <em>Spring TestContext Framework</em> by means
 * of the {@link TestContextManager} and associated support classes and annotations.
 * 
 * Doesn't support: org.springframework.test.annotation.@Repeat and org.springframework.test.annotation.@Timed annotations.
 * 
 * @author Eric Vialle
 */
public class SpringOceanModule extends OceanModule {
	

	private TestContextManager testContextManager;
	
	@Override
	public void doBeforeAllTestedMethods(final OceanRunner oceanRunner, final Class<?> klass) throws OceanModuleException {
		this.testContextManager = createTestContextManager(klass);
	}
	
	@Override
	public void doBeforeEachTestedMethod(final OceanRunner oceanRunner) throws OceanModuleException {
		Object target = oceanRunner.getTarget();
		try {
			getTestContextManager().prepareTestInstance(target);
		} catch (Exception e) {
			throw new OceanModuleException(e);
		}
	}
	
	
	/**
	 * Creates a new {@link TestContextManager} for the supplied test class and
	 * the configured <em>default <code>ContextLoader</code> class name</em>.
	 * Can be overridden by subclasses.
	 * @param clazz the test class to be managed
	 * @see #getDefaultContextLoaderClassName(Class)
	 */
	protected TestContextManager createTestContextManager(Class<?> clazz) {
		return new TestContextManager(clazz, null);
	}
	
	/**
	 * Get the {@link TestContextManager} associated with this runner.
	 */
	protected final TestContextManager getTestContextManager() {
		return this.testContextManager;
	}

}
