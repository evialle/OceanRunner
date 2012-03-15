package it.freshminutes.oceanrunner.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define the settings for parallelized tests. Use it with @ConcurrentOceanModule
 * 
 * <ul>
 * <li>threads: number of threads to use</li>
 * <li>value: enabling the parallelized tests</li>
 * </ul>
 * 
 * @author Eric Vialle
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface OceanRunTestsInDedicatedThreads {
	int threads() default 5;
	boolean value() default true;
}
