/**
 * 
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
