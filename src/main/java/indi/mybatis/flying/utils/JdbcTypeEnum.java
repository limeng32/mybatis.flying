package indi.mybatis.flying.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.JdbcType;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @Email limeng32@live.cn
 * @version
 * @since JDK 1.8
 */
public enum JdbcTypeEnum {

	ARRAY(JdbcType.ARRAY), BIT(JdbcType.BIT), TINYINT(JdbcType.TINYINT), SMALLINT(JdbcType.SMALLINT),
	INTEGER(JdbcType.INTEGER), BIGINT(JdbcType.BIGINT), FLOAT(JdbcType.FLOAT), REAL(JdbcType.REAL),
	DOUBLE(JdbcType.DOUBLE), NUMERIC(JdbcType.NUMERIC), DECIMAL(JdbcType.DECIMAL), CHAR(JdbcType.CHAR),
	VARCHAR(JdbcType.VARCHAR), LONGVARCHAR(JdbcType.LONGVARCHAR), DATE(JdbcType.DATE), TIME(JdbcType.TIME),
	TIMESTAMP(JdbcType.TIMESTAMP), BINARY(JdbcType.BINARY), VARBINARY(JdbcType.VARBINARY),
	LONGVARBINARY(JdbcType.LONGVARBINARY), NULL(JdbcType.NULL), OTHER(JdbcType.OTHER), BLOB(JdbcType.BLOB),
	CLOB(JdbcType.CLOB), BOOLEAN(JdbcType.BOOLEAN), CURSOR(JdbcType.CURSOR), UNDEFINED(JdbcType.UNDEFINED),
	NVARCHAR(JdbcType.NVARCHAR), NCHAR(JdbcType.NCHAR), NCLOB(JdbcType.NCLOB), STRUCT(JdbcType.STRUCT),
	JAVA_OBJECT(JdbcType.JAVA_OBJECT), DISTINCT(JdbcType.DISTINCT), REF(JdbcType.REF), DATALINK(JdbcType.DATALINK),
	ROWID(JdbcType.ROWID), LONGNVARCHAR(JdbcType.LONGNVARCHAR), SQLXML(JdbcType.SQLXML),
	DATETIMEOFFSET(JdbcType.DATETIMEOFFSET);

	private final JdbcType jdbcType;

	private JdbcTypeEnum(JdbcType jdbcType) {
		this.jdbcType = jdbcType;
	}

	private static final Map<String, JdbcType> nameLookup = new HashMap<>(64);

	static {
		for (JdbcTypeEnum jdbcTypeEnum : JdbcTypeEnum.values()) {
			nameLookup.put(jdbcTypeEnum.name(), jdbcTypeEnum.jdbcType);
		}
	}

	public static JdbcType forName(String name) {
		return nameLookup.get(name == null ? name : name.toUpperCase());
	}
}
