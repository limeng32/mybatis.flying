package indi.mybatis.flying.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import indi.mybatis.flying.models.AggregateFunction;
import indi.mybatis.flying.statics.ConditionType;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 * @description Used to describe the annotation of the description of the
 *              personalization constraints for object fields in pojos, in the
 *              PojoCondition class, e.g "like"
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface ConditionMapperAnnotation {
	/**
	 * 
	 * The field name of the corresponding database table
	 * 
	 * @return String
	 * @since 0.9.0
	 */
	String dbFieldName();

	/**
	 * 
	 * The type that identifies this condition, the default is "equal"
	 * 
	 * @return ConditionType
	 * @since 0.9.0
	 */
	ConditionType conditionType() default ConditionType.EQUAL;

	/**
	 * 
	 * Identify the condition for which (business) child objects, the default is
	 * "Void" meaning themselves.This property works only in Or annotation
	 * 
	 * @return Class
	 * @since 0.9.3
	 */
	Class<?> subTarget() default Void.class;

	/**
	 * 
	 * Whether to use the specified TypeHandler (highest priority).the default is
	 * "Void" meaning not specified
	 * 
	 * @return Class
	 * @since 0.9.4
	 */
	Class<?> customTypeHandler() default Void.class;

	/**
	 * 
	 * 使用何种聚合操作，默认为none即不使用聚合操作。如果使用了聚合操作例如sum，此条件将会出现在having子句中。
	 * 
	 * What kind of aggregation operation to use, the default is none means "not use
	 * aggregation operation". If a non-none aggregation operation such as "sum" is
	 * used, this condition will appear in the "having" clause.
	 * 
	 * @return AggregateFunction
	 * @since 1.1.0
	 */
	AggregateFunction aggregateFunction() default AggregateFunction.NONE;
	
	/**
	 * 
	 * Is a delegate mode or not. In delegate mode you can get the primary key of
	 * the associated objects treated as a normal attributes. The default is false 
	 * meaning delegate mode if off.
	 * 
	 * @return boolean
	 * @since 1.0.16
	 */
	boolean delegate() default false;
}
