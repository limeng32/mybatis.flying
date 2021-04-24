package indi.mybatis.flying.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @date 2021年4月24日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface HavingOr {

	/**
	 * 仅作用于having子句
	 * 
	 * Acts only on the having clause
	 * 
	 * @since 1.1.0
	 * @return ConditionMapperAnnotation
	 */
	ConditionMapperAnnotation[] value();

}
