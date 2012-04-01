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
package it.freshminutes.oceanrunner.modules.builtin.ejb;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;

/**
 * @author Eric Vialle
 * 
 */
public class OpenEjbOceanModule extends OceanModule {

	public static EJBContainer ejbContainer;

	private boolean runOnlyAtTheFirstTime;

	@Override
	public void doBeforeAllTestedMethods(final OceanRunner oceanRunner, final Class<?> klass) throws OceanModuleException {
		Properties p = new Properties();
		// Initialise la fabrique de contexte initial.
		p.put("java.naming.factory.initial", "org.apache.openejb.client.LocalInitialContextFactory");
		// Définit le format des noms JNDI (exemple: HelloWorldBean/Local).
		p.put("openejb.jndiname.format", "{ejbName}/{interfaceType.annotationName}");
		// Flags pour tracer toutes les actions liées au démarrage du conteneur.
		p.put("openejb.descriptors.output", "true");
		 p.put("openejb.validation.output.level", "verbose");

		p.setProperty("java.naming.factory.initial", "org.apache.openejb.client.LocalInitialContextFactory");
		p.setProperty("openejb.deployments.classpath.include", ".*");

		ejbContainer = EJBContainer.createEJBContainer(p);

		runOnlyAtTheFirstTime = true;
	}

	@Override
	public void doBeforeEachTestedMethod(final OceanRunner oceanRunner) throws OceanModuleException {
		if (runOnlyAtTheFirstTime) {
			runOnlyAtTheFirstTime = false;

			// Injection des ressources.
			try {
				Object classUnderTest = oceanRunner.getTarget();
				ejbContainer.getContext().bind("inject", classUnderTest);
			} catch (NamingException e) {
				throw new OceanModuleException(e);
			}
		}
	}

	@Override
	public void doAfterAllTestedMethods(final OceanRunner oceanRunner, final Class<?> klass) throws OceanModuleException {
		try {
			ejbContainer.close();
		} catch (Throwable t) {
			throw new OceanModuleException(t);
		}
	}

}
