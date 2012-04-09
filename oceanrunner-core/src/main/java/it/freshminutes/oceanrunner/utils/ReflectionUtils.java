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
package it.freshminutes.oceanrunner.utils;

import java.lang.reflect.Method;

/**
 * Easily use reflections.
 * 
 * @author Eric Vialle
 * 
 */
public class ReflectionUtils {
	
	/**
	 * 
	 * @param target an instancied object
	 * @param method the method to call on the target
	 * @return the method, even if it belongs to an inherited class
	 * @throws NoSuchMethodException if this method doesn't exist at all
	 */
	public static Method invokeHeritedMethod(Object target, String method) throws NoSuchMethodException {
		 return invokeHeritedMethod(target, method, target.getClass());
	}

	private static Method invokeHeritedMethod(Object target, String method, Class<?> klass) throws NoSuchMethodException {
		if (klass == null) {
			throw new NoSuchMethodException();
		}

		Method m;
		try {
			m = klass.getDeclaredMethod(method);
			
		} catch (NoSuchMethodException e) {
			return invokeHeritedMethod(target, method, klass.getSuperclass());
		}

		 return m;
	}


}
