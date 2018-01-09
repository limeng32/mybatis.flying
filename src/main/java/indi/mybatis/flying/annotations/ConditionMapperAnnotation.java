package indi.mybatis.flying.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import indi.mybatis.flying.statics.ConditionType;

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
	 * @return ConditionType
	 */
	ConditionType conditionType() default ConditionType.Equal;

	/**
	 * 
	 * 标识此项条件是针对哪个（业务上）子对象的，默认为Void，即是针对自身的；此属性只在或逻辑（Or标签）中起作用
	 * 
	 * @return Class
	 */
	Class<?> subTarget() default Void.class;
}
