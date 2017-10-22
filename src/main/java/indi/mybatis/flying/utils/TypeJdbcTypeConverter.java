package indi.mybatis.flying.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.JdbcType;

public class TypeJdbcTypeConverter {

	public static Map<Class<?>, JdbcType> map = new HashMap<>(128);

	static {
		map.put(String.class, JdbcType.VARCHAR);
		map.put(Integer.class, JdbcType.INTEGER);
		map.put(Date.class, JdbcType.TIMESTAMP);
	}

}
