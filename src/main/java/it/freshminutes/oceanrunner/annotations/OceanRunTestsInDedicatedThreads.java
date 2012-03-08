package it.freshminutes.oceanrunner.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Eric Vialle
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface OceanRunTestsInDedicatedThreads {
	int threads() default 5;
	boolean value() default true;
}
