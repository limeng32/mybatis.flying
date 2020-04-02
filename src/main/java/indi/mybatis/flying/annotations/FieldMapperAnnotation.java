package indi.mybatis.flying.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.statics.AssociationType;
import indi.mybatis.flying.statics.OpLockType;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author david,李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 * @description Annotations that describe the database table fields
 *              corresponding to the Java object fields
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface FieldMapperAnnotation {
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
	 * If it is a foreign key, it corresponds to the name of the primary key field
	 * of the other table in the database (Case is identical), the default is blank
	 * meaning not foreign key.
	 * 
	 * @return String
	 * @since 0.9.0
	 */
	String dbAssociationUniqueKey() default "";

	/**
	 * 
	 * This variable corresponds to the primary key of the database table, the
	 * default is false
	 * 
	 * @return boolean
	 * @since 0.9.0
	 */
	boolean isUniqueKey() default false;

	/**
	 * 
	 * This variable is optimistic locking, the default is "Null" meaning not
	 * 
	 * @return OpLockType
	 * @since 0.9.0
	 */
	OpLockType opLockType() default OpLockType.NULL;

	/**
	 * 
	 * The field USES the JDBC interface to store the data type that the database
	 * needs to set, e.g Integer, Long, Short, Float, Double, String, Date,
	 * Timestamp, Time and so on
	 * 
	 * @return JdbcType
	 * @since 0.9.0
	 */
	JdbcType jdbcType();

	/**
	 * 
	 * Whether or not to have the ignore sign, the default is not.When the flag
	 * "foo" is appeared, the flying expression ends with ":foo" to ignore this
	 * field in the addition, query, or modification statement.
	 * 
	 * @return String[]
	 * @since 0.9.0
	 */
	String[] ignoreTag() default {};

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
	 * When declaring a foreign key relationship, make an extra declaration here if
	 * there are other constraints besides foreign keys (e.g. select * from a left
	 * join b on a.fid = b.id and a.name = b.name)
	 * 
	 * 声明外键关系时，如果除了外键之外还有其它约束，在这里做额外声明（例如 select * from a left join b on a.fid =
	 * b.id and a.name = b.name）
	 * 
	 * @return ForeignAssociation
	 * @since 0.9.7
	 */
	ForeignAssociation[] associationExtra() default {};

	/**
	 * 
	 * How the tables are related (e.g. left join or right join)
	 * 
	 * 与相关表的关联方式（例如左联或右联）
	 * 
	 * @return AssociationType
	 * @since 0.9.7
	 */
	AssociationType associationType() default AssociationType.LEFT_JOIN;
}