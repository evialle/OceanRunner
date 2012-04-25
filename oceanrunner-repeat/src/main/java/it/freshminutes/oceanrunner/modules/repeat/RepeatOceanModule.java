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
package it.freshminutes.oceanrunner.modules.repeat;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;

import java.lang.reflect.Method;

/**
 * OceanModule allowing user to test many time the same method with the annotation @OceanRunRepeat
 * 
 * @author Eric Vialle
 */
public class RepeatOceanModule extends OceanModule {
	

	/** 
	 * Number of times that this method should be run. 
	 * @throws OceanModuleException
	 */
	@Override
	public long totalNumberOfRepeat(final OceanRunner oceanRunner) throws OceanModuleException {
		
		String methodName = oceanRunner.getMethodUnderTest();
		try {
			Method method = oceanRunner.getClassUnderTest().getMethod(methodName);
			OceanRunRepeat repeatAnnotation = method.getAnnotation(OceanRunRepeat.class);
					
			if ((repeatAnnotation != null) && (repeatAnnotation.value() > 1)) {
				return repeatAnnotation.value();
			} else {
				return 1;
			}
			
		} catch (Exception e) {
			throw new OceanModuleException(e);
		} 

	}
}
