package indi.mybatis.flying.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.JdbcType;

public class TypeJdbcTypeConverter {

	protected final static Map<Class<?>, JdbcType> map = new HashMap<>(128);

	static {
		map.put(String.class, JdbcType.VARCHAR);
		map.put(Integer.class, JdbcType.INTEGER);
		map.put(Date.class, JdbcType.TIMESTAMP);
		map.put(Double.class, JdbcType.DOUBLE);
		map.put(Float.class, JdbcType.FLOAT);
		map.put(Boolean.class, JdbcType.BOOLEAN);
	}

}
