/**
 * 
 */
package it.freshminutes.oceanrunner.annotations;

import it.freshminutes.oceanrunner.modules.engine.OceanModule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Eric
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface OceanModulesToUse {

	Class<? extends OceanModule>[] value();
}
