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
/**
 * 
 */
package it.freshminutes.oceanrunner.modules.ejb;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.runner.Description;

/**
 * @author Eric Vialle
 * 
 */
public class OpenEjb30OceanModule extends OceanModule {

	private static final ThreadLocal<Context> jeeContextThreadLocal = new ThreadLocal<Context>();

	@Override
	public void doBeforeAllTestedMethods(final OceanRunner oceanRunner, final Class<?> klass) throws OceanModuleException {

	}

	/**
	 * @throws OceanModuleException
	 */
	private static Context createJEEContext() throws OceanModuleException {
		Properties p = new Properties();

		try {
			return new InitialContext(p);
		} catch (NamingException e) {
			throw new OceanModuleException("Can't initialize JavaEE context", e);
		}
	}

	/**
	 * @return the context of the EJB Container.
	 * @throws OceanModuleException
	 *             if the container can't be initialized
	 */
	public static Context getJEEContext() throws OceanModuleException {
		Context ctx = jeeContextThreadLocal.get();

		if (ctx == null) {
			ctx = createJEEContext();
			jeeContextThreadLocal.set(ctx);
		}

		return ctx;
	}

	/**
	 * Lookup the EJB for a given class.
	 * 
	 * @param klass
	 *            EJB Class to find.
	 * @return the Proxy of the EJB
	 * @throws NamingException
	 * @throws OceanModuleException
	 *             if the JEE Context can't be retrieved
	 */
	public static Object lookupEJB(Class<?> klass) throws NamingException, OceanModuleException {
		return getJEEContext().lookup(klass.getName());
	}

	@Override
	public void doAfterEachTestedMethod(final OceanRunner oceanRunner, final Description description) throws OceanModuleException {
		OpenEjbResetAfterEachTest annotation = oceanRunner.getClassUnderTest().getAnnotation(OpenEjbResetAfterEachTest.class);
		if (annotation != null) {
			if (annotation.value()) {
				Context ctx = jeeContextThreadLocal.get();
				try {
					ctx.close();
					ctx = null;
				} catch (NamingException e) {
					// We don't care
				}
				jeeContextThreadLocal.set(null);
			}
		}
	}

}
