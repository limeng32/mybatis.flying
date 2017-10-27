package indi.mybatis.flying.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.JdbcType;

public enum JdbcTypeEnum {

	ARRAY(JdbcType.ARRAY), 
	BIT(JdbcType.BIT), 
	TINYINT(JdbcType.TINYINT), 
	SMALLINT(JdbcType.SMALLINT), 
	INTEGER(JdbcType.INTEGER), 
	BIGINT(JdbcType.BIGINT), 
	FLOAT(JdbcType.FLOAT), 
	REAL(JdbcType.REAL), 
	DOUBLE(JdbcType.DOUBLE),
	NUMERIC(JdbcType.NUMERIC),
	DECIMAL(JdbcType.DECIMAL),
	CHAR(JdbcType.CHAR),
	VARCHAR(JdbcType.VARCHAR),
	LONGVARCHAR(JdbcType.LONGVARCHAR),
	DATE(JdbcType.DATE),
	TIME(JdbcType.TIME),
	TIMESTAMP(JdbcType.TIMESTAMP),
	BINARY(JdbcType.BINARY),
	VARBINARY(JdbcType.VARBINARY),
	LONGVARBINARY(JdbcType.LONGVARBINARY),
	NULL(JdbcType.NULL),
	OTHER(JdbcType.OTHER),
	BLOB(JdbcType.BLOB),
	CLOB(JdbcType.CLOB),
	BOOLEAN(JdbcType.BOOLEAN),
	CURSOR(JdbcType.CURSOR),
	UNDEFINED(JdbcType.UNDEFINED),
	NVARCHAR(JdbcType.NVARCHAR),
	NCHAR(JdbcType.NCHAR),
	NCLOB(JdbcType.NCLOB),
	STRUCT(JdbcType.STRUCT);

	private final JdbcType jdbcType;

	private JdbcTypeEnum(JdbcType jdbcType) {
		this.jdbcType = jdbcType;
	}
	
	private static Map<String,JdbcType> nameLookup = new HashMap<>();
	
	static {
		for (JdbcTypeEnum jdbcTypeEnum : JdbcTypeEnum.values()) {
			nameLookup.put(jdbcTypeEnum.name(), jdbcTypeEnum.jdbcType);
		}
	}
	
	public static JdbcType forName(String name) {
		return nameLookup.get(name == null ? name : name.toUpperCase());
	}
}
