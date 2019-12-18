package indi.mybatis.flying.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @Email limeng32@live.cn
 * @version
 * @since JDK 1.8
 * @description Used to describe the role of the mapper class in the cache
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
	Class<?>[] observerClass();

	/**
	 * 
	 * Which classes are the Trigger for the current classes
	 * 
	 * @return Class[]
	 * @since 0.9.0
	 */
	Class<?>[] triggerClass();

}
