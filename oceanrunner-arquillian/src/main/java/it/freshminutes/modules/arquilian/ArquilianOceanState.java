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
package it.freshminutes.modules.arquilian;

import it.freshminutes.oceanrunner.exceptions.OceanModuleException;
import it.freshminutes.oceanrunner.utils.ReflectionUtils;

import java.lang.reflect.Method;

import org.jboss.arquillian.junit.State;
import org.jboss.arquillian.test.spi.TestRunnerAdaptor;

/**
 * Exposing the default methods of org.jboss.arquillian.junit.State.
 * 
 * @author Eric Vialle
 */
class ArquilianOceanState extends State {
	
	static void runnerStarted() throws OceanModuleException {
		try {
			Method method = ReflectionUtils.invokeStaticMethod(State.class, "runnerStarted");
			method.invoke(null, null);
			
		} catch (Exception e) {
			throw new OceanModuleException(e);
		}
	}
	
	static Integer runnerFinished() throws OceanModuleException {
		try {
			Method method = ReflectionUtils.invokeStaticMethod(State.class, "runnerFinished");
			return (Integer) method.invoke(null, null);
			
		} catch (Exception e) {
			throw new OceanModuleException(e);
		}
	}
	
	static Integer runnerCurrent() throws OceanModuleException {
		try {
			Method method = ReflectionUtils.invokeStaticMethod(State.class, "runnerCurrent");
			return (Integer) method.invoke(null, null);
			
		} catch (Exception e) {
			throw new OceanModuleException(e);
		}
	}
	
	static boolean isLastRunner() throws OceanModuleException {
		try {
			Method method = ReflectionUtils.invokeStaticMethod(State.class, "isLastRunner");
			return (Boolean) method.invoke(null, null);
			
		} catch (Exception e) {
			throw new OceanModuleException(e);
		}
	}
	
	static void testAdaptor(TestRunnerAdaptor adaptor) throws OceanModuleException {
		try {
			Method method = ReflectionUtils.invokeStaticMethod(State.class, "testAdaptor");
			method.invoke(null, adaptor);
			
		} catch (Exception e) {
			throw new OceanModuleException(e);
		}
	}
	
	static boolean hasTestAdaptor() throws OceanModuleException {
		try {
			Method method = ReflectionUtils.invokeStaticMethod(State.class, "hasTestAdaptor");
			return (Boolean) method.invoke(null, null);
			
		} catch (Exception e) {
			throw new OceanModuleException(e);
		}
	}
	
	static TestRunnerAdaptor getTestAdaptor() throws OceanModuleException {
		try {
			Method method = ReflectionUtils.invokeStaticMethod(State.class, "getTestAdaptor");
			return (TestRunnerAdaptor) method.invoke(null, null);
			
		} catch (Exception e) {
			throw new OceanModuleException(e);
		}
	}
	
	static void caughtInitializationException(Throwable throwable) throws OceanModuleException {
		try {
			Method method = ReflectionUtils.invokeStaticMethod(State.class, "caughtInitializationException");
			method.invoke(null, throwable);
			
		} catch (Exception e) {
			throw new OceanModuleException(e);
		}
	}
	
	static boolean hasInitializationException() throws OceanModuleException {
		try {
			Method method = ReflectionUtils.invokeStaticMethod(State.class, "hasInitializationException");
			return (Boolean) method.invoke(null, null);
			
		} catch (Exception e) {
			throw new OceanModuleException(e);
		}
	}
	
	static Throwable getInitializationException() throws OceanModuleException {
		try {
			Method method = ReflectionUtils.invokeStaticMethod(State.class, "getInitializationException");
			return (Throwable) method.invoke(null, null);
			
		} catch (Exception e) {
			throw new OceanModuleException(e);
		}
	}
	
	static void clean() throws OceanModuleException {
		try {
			Method method = ReflectionUtils.invokeStaticMethod(State.class, "clean");
			method.invoke(null, null);
			
		} catch (Exception e) {
			throw new OceanModuleException(e);
		}
	}
}
