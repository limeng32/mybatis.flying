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
 * @author david,limeng32
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface FieldMapperAnnotation {
	/**
	 * 
	 * 对应数据库表的字段名称
	 * 
	 * @return String
	 */
	String dbFieldName();

	/**
	 * 
	 * 如果是外键，对应数据库其他表的主键字段的名称。默认为空，表示不是外键。此属性用于解决同一数据库内表关联问题，因此不应和dbAssociationTypeHandler同时出现。
	 * 
	 * @return String
	 */
	String dbAssociationUniqueKey() default "";

	/**
	 * 
	 * 此变量是否对应数据库表的主键。默认为否。
	 * 
	 * @return boolean
	 */
	boolean isUniqueKey() default false;

	/**
	 * 
	 * 此变量是否是乐观锁，默认不是。
	 * 
	 * @return OpLockType
	 */
	OpLockType opLockType() default OpLockType.Null;

	/**
	 * 
	 * 字段用JDBC接口存入数据库需要设置的数据类型,Integer,Long,Short,Float,Double,String,Date
	 * ,Timestamp,Time
	 * 
	 * @return JdbcType
	 */
	JdbcType jdbcType();

	/**
	 * 
	 * 是否拥有忽略标志。默认为无。当有忽略标志foo时，flying表达式最后加上“:foo”即可在查询语句中忽略此字段
	 * 
	 * @return String[]
	 */
	String[] ignoreTag() default {};

	/**
	 * 
	 * 跨库相关表是否使用指定TypeHandler处理。默认为Objectclass，表示不指定TypeHandler。此属性用于解决跨库表关联问题，因此不应和dbAssociationUniqueKey同时出现。
	 * 
	 * @return Class<?>
	 */
	Class<?> dbAssociationTypeHandler() default Object.class;
}
