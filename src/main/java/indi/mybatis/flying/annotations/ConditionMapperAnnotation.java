package indi.mybatis.flying.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import indi.mybatis.flying.statics.ConditionType;
import indi.mybatis.flying.statics.LogicType;

/**
 * 用于描述Pojo类的子类PojoCondition类中，对于pojo中的对象字段的个性化限制条件（例如like）的描述的注解（数据库字段名，
 * 对此字段的限制方式）
 * 
 * @author limeng32
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface ConditionMapperAnnotation {
	/**
	 * 
	 * 对应数据库表的字段名称
	 * 
	 * @return String
	 */
	String dbFieldName();

	/**
	 * 
	 * 标识此条件的类型，默认为“相等”
	 * 
	 * @return LogicType
	 */
	ConditionType conditionType() default ConditionType.Equal;

	/**
	 * 
	 * 标识此条件是参与“与”逻辑运算还是参与“或”逻辑运算，默认为“与”逻辑
	 * 
	 * @return LogicType
	 */
	LogicType logic() default LogicType.AND;
}
