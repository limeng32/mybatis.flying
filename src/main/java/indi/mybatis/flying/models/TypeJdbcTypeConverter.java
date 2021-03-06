package indi.mybatis.flying.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.JdbcType;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public class TypeJdbcTypeConverter {

	private TypeJdbcTypeConverter() {

	}

	protected static final Map<Class<?>, JdbcType> map = new HashMap<>(8);

	static {
		map.put(String.class, JdbcType.VARCHAR);
		map.put(Integer.class, JdbcType.INTEGER);
		map.put(Date.class, JdbcType.TIMESTAMP);
		map.put(Double.class, JdbcType.DOUBLE);
		map.put(Float.class, JdbcType.FLOAT);
		map.put(Boolean.class, JdbcType.BOOLEAN);
		map.put(Long.class, JdbcType.BIGINT);
	}

}
