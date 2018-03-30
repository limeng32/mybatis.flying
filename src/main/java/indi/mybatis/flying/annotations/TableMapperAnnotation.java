package indi.mybatis.flying.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes the annotation classes for the relationship between Java objects
 * and database tables
 * 
 * @author huangzipeng
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface TableMapperAnnotation {
	/**
	 * 
	 * Table name of the database table corresponding to
	 * @return String
	 */
	public String tableName();
}
