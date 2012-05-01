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
package it.freshminutes.oceanrunner.modules.parameterized;

import it.freshminutes.oceanrunner.OceanRunner;
import it.freshminutes.oceanrunner.exceptions.OceanModuleException;
import it.freshminutes.oceanrunner.modules.engine.OceanModule;
import it.freshminutes.oceanrunner.modules.parameterized.annotations.OceanParameterizedVariable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import com.google.common.collect.Lists;

/**
 * Parameterized Runner like.
 * 
 * @author Eric Vialle
 */
public class ParameterizedOceanModule extends OceanModule {

	/** List of parameters. */
	private List<Object[]> fParameterList;

	@Override
	public void doBeforeAllTestedMethods(final OceanRunner oceanRunner,
			final Class<?> klass) throws OceanModuleException {
		try {
			this.fParameterList = getParametersList(oceanRunner.getTestClass());
		} catch (Throwable e) {
			throw new OceanModuleException(e);
		}
	}

	@Override
	public void doBeforeEachTestedMethod(final OceanRunner oceanRunner)
			throws OceanModuleException {
		//Get the index of the parameters to test
		long index = this.nbOfRepeatModulo(oceanRunner);
		
		if (index <= Long.MAX_VALUE) {
			//Retrieve the parameters and put them in the tests
			Object[] valuesArray = fParameterList.get((int) index);
			List<Field> variableList = getOceanVariableList(oceanRunner.getTestClass());
			Object value;
			//manage the fact that the variableList is bigger that the 
			for (int i=0; i < variableList.size(); i++) {
				if (valuesArray.length > i) {
					value = valuesArray[i];
				} else {
					value = null;
				}
				Field variableField = variableList.get(i);

				try {
					variableField.setAccessible(true);
					variableField.set(oceanRunner.getTarget(), value);
				} catch (Exception e) {
					throw new OceanModuleException(e);
				} 
			}		
		} else {
			throw new OceanModuleException("Too many tests kill the tests");
		}
		
	}
	

	/**
	 * List of variables to populate.
	 * 
	 * @param klass
	 * @return
	 */
	private List<Field> getOceanVariableList(TestClass klass) {
		List<Field> fieldsListToReturn = Lists.newArrayList();
		List<FrameworkField> fieldsList = klass.getAnnotatedFields(OceanParameterizedVariable.class);
		for (FrameworkField frameworkField : fieldsList) {
			fieldsListToReturn.add(frameworkField.getField());
		}
		return fieldsListToReturn;
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> getParametersList(TestClass klass) throws Throwable {
		return (List<Object[]>) getParametersMethod(klass).invokeExplosively(null);
	}

	private FrameworkMethod getParametersMethod(TestClass testClass)
			throws Exception {
		List<FrameworkMethod> methods = testClass.getAnnotatedMethods(Parameters.class);
		for (FrameworkMethod each : methods) {
			int modifiers = each.getMethod().getModifiers();
			if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
				return each;
			}
		}

		throw new Exception("No public static parameters method on class " + testClass.getName());
	}
	
	@Override
	public long totalNumberOfRepeat(final OceanRunner oceanRunner) {
		return fParameterList.size();
	}

}
