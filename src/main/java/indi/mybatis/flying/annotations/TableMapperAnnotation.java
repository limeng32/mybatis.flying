package indi.mybatis.flying.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述Java对象与数据库表映射关系的注解类（对应的数据库表名、唯一键类型、唯一键名称）
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
	 * @return Dto对应的数据库表的表名
	 */
	public String tableName();
}
