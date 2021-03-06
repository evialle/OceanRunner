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
package it.freshminutes.oceanrunner.exceptions;

/**
 * Exception to be used by OceanModule.
 * 
 * @author Eric Vialle
 */
public class OceanModuleException extends Exception {

	private static final long serialVersionUID = 465178879784554689L;

	/**
	 * Constructor.
	 * 
	 * @param t
	 *            root throwable
	 */
	public OceanModuleException(Throwable t) {
		super(t);
	}

	/**
	 * Constructor.
	 * 
	 * @param s
	 *            root explanation of the exception.
	 */
	public OceanModuleException(String s) {
		super(s);
	}

	/**
	 * Constructor.
	 * 
	 * @param s
	 *            root explanation of the exception.
	 */
	public OceanModuleException(String s, Throwable t) {
		super(s, t);
	}

}
