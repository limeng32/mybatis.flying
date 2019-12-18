package indi.mybatis.flying.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @Email limeng32@live.cn
 * @version
 * @since JDK 1.8
 * @description Annotation classes that describe the relationship between
 *              PojoCondition objects and database tables (corresponding
 *              database table names).
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
	 * 
	 * @return String
	 */
	public String tableName();
}
