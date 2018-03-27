package indi.mybatis.flying.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to describe the role of the mapper class in the cache
 * 
 * @author limeng32
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface CacheRoleAnnotation {

	/**
	 * 
	 * Which classes are the current classes to Observe
	 * 
	 * @return Class[]
	 * @since 0.9.0
	 */
	Class<?>[] ObserverClass();

	/**
	 * 
	 * Which classes are the Trigger for the current classes
	 * 
	 * @return Class[]
	 * @since 0.9.0
	 */
	Class<?>[] TriggerClass();

}
