package indi.mybatis.flying.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于描述mapper类在缓存中的角色情况
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
	 * 当前类对哪些类作为Observer
	 */
	Class<?>[] ObserverClass();

	/**
	 * 
	 * 当前类对哪些类作为Trigger
	 */
	Class<?>[] TriggerClass();

}
