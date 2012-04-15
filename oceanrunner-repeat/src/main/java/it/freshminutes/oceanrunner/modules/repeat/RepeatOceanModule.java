/**
 * 
 */
package it.freshminutes.oceanrunner.modules.repeat;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;

import java.lang.reflect.Method;

/**
 * @author Eric
 * 
 */
public class RepeatOceanModule extends OceanModule {
	

	/** Number of times that this method should be run. 
	 * @throws OceanModuleException */
	@Override
	public long totalNumberOfRepeat(final OceanRunner oceanRunner) throws OceanModuleException {
		
		
		String methodName = oceanRunner.getMethodUnderTest();
		try {
			Method method = oceanRunner.getClassUnderTest().getMethod(methodName);
			OceanRunRepeat repeatAnnotation = method.getAnnotation(OceanRunRepeat.class);
			
			
			if ((repeatAnnotation!= null) && (repeatAnnotation.value() > 1)) {
				return repeatAnnotation.value();
			} else {
				return 1;
			}
			
		} catch (Exception e) {
			throw new OceanModuleException(e);
		} 

	}
}
