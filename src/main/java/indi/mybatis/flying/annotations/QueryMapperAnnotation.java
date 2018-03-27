package indi.mybatis.flying.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation classes that describe the relationship between PojoCondition
 * objects and database tables (corresponding database table names)
 * 
 * @author limeng32
 * @deprecated
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Deprecated
public @interface QueryMapperAnnotation {
	/**
	 * 
	 * Table name of the database table corresponding to
	 */
	public String tableName();
}
