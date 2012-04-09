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
package it.freshminutes.oceanrunner.modules.concurrent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define the settings for parallelized tests. Use it with
 * @ConcurrentOceanModule.
 * 
 * <ul>
 * <li>threads: number of threads to use (for methods not marked by
 * OceanRunConcurrencyForbidden). The number of threads is defined
 * automatically, you do not need to use that value</li>
 * <li>value: enabling the parallelized tests</li>
 * </ul>
 * 
 * @author Eric Vialle
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface OceanRunTestsInDedicatedThreads {

	/** Number of threads to use. */
	int threads() default -1;

	/** Is multi threading activated for this class? */
	boolean value() default true;
}
