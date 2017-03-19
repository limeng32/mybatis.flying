package indi.mybatis.flying.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import indi.mybatis.flying.statics.CacheRoleType;

/**
 * 用于描述mapper中的方法是用于触发还是用于观察
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
	 * 目标方法是用于触发还是用于观察，前者为trigger，后者为observer。
	 */
	CacheRoleType role();
}
