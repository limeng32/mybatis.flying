package indi.mybatis.flying.handlers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

/**
 * The handler that solves the like '% XXX %' condition in the SQL query
 * statement.
 * 
 */
@MappedTypes({ String.class })
@MappedJdbcTypes({ JdbcType.VARCHAR })
public class ConditionLikeHandler extends BaseTypeHandler<String> implements TypeHandler<String> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
			throws SQLException {
		if (parameter.indexOf("%") > -1) {
			parameter = parameter.replaceAll("%", "\\\\%");
		}
		if (parameter.indexOf("_") > -1) {
			parameter = parameter.replaceAll("_", "\\\\_");
		}
		ps.setString(i, "%" + parameter + "%");
	}

	@Override
	public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return rs.getString(columnName);
	}

	@Override
	public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getString(columnIndex);
	}

	@Override
	public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return cs.getString(columnIndex);
	}

}
