package indi.mybatis.flying.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.statics.OpLockType;

/**
 * 用于描述java对象字段对应的数据库表字段的注解（数据库字段名，对应数据库其他表的主键字段的名称，是否为数据库主键，字段对应的jdbc类型）
 * 
 * @author david
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface FieldMapperAnnotation {
	/**
	 * 
	 * 对应数据库表的字段名称
	 */
	String dbFieldName();

	/**
	 * 
	 * 如果是外键，对应数据库其他表的主键字段的名称。默认为空，表示不是外键。
	 */
	String dbAssociationUniqueKey() default "";

	/**
	 * 
	 * 此变量是否对应数据库表的主键。默认为否。
	 */
	boolean isUniqueKey() default false;

	/**
	 * 
	 * 此变量是否是乐观锁，默认不是。
	 */
	OpLockType opLockType() default OpLockType.Null;

	/**
	 * 
	 * 字段用JDBC接口存入数据库需要设置的数据类型,Integer,Long,Short,Float,Double,String,Date
	 * ,Timestamp,Time
	 */
	JdbcType jdbcType();

	/**
	 * 
	 * 是否在select时忽略。默认为false。
	 */
	boolean ignoredSelect() default false;
}
