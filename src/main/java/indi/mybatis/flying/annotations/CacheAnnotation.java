package indi.mybatis.flying.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import indi.mybatis.flying.statics.CacheRoleType;

/**
 * describe the method in mapper is used to trigger or to observe
 * 
 * @author limeng32
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface CacheAnnotation {
	/**
	 * 
	 * The target method is used to trigger or to observe, the former is trigger
	 * and the latter is observer
	 * 
	 * @return CacheRoleType
	 * @since 0.9.0
	 */
	CacheRoleType role();
}
