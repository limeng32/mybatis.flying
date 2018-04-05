package indi.mybatis.flying.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.statics.OpLockType;

/**
 * Annotations that describe the database table fields corresponding to the Java
 * object fields
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
	 * The field name of the corresponding database table
	 * 
	 * @return String
	 * @since 0.9.0
	 */
	String dbFieldName();

	/**
	 * 
	 * If it is a foreign key, it corresponds to the name of the primary key
	 * field of the other table in the database (Case is identical), the default
	 * is blank meaning not foreign key.
	 * 
	 * This property is used to solve the table association problem in the same
	 * database, so it should not appear at the same time as the
	 * dbCrossedAssociationUniqueKey.
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
	OpLockType opLockType() default OpLockType.Null;

	/**
	 * 
	 * The field USES the JDBC interface to store the data type that the
	 * database needs to set, e.g Integer, Long, Short, Float, Double, String,
	 * Date, Timestamp, Time and so on
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
	 * Whether to use the specified TypeHandler (highest priority).the default
	 * is "Void" meaning not specified
	 * 
	 * @return Class
	 * @since 0.9.4
	 */
	Class<?> customTypeHandler() default Void.class;

	/**
	 * 
	 * If it is a cross-source foreign key, the name of the primary key field
	 * for the table of the other database(Case is identical), the default is
	 * blank meaning not foreign key.
	 * 
	 * This property is used to solve the cross-source association problem, so
	 * it should not appear at the same time as dbAssociationUniqueKey.
	 * 
	 * @return String
	 * @since 0.9.1
	 */
	String dbCrossedAssociationUniqueKey() default "";
}