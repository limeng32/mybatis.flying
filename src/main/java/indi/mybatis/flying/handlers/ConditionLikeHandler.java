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
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 * @description The handler that solves the like '% XXX %' condition in the SQL
 *              query statement.
 */
@MappedTypes({ String.class })
@MappedJdbcTypes({ JdbcType.VARCHAR })
public class ConditionLikeHandler extends BaseTypeHandler<String> implements TypeHandler<String> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String parameter2, JdbcType jdbcType)
			throws SQLException {
		String parameter = cookParameter(parameter2);
		ps.setString(i, new StringBuilder("%").append(parameter).append("%").toString());
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

	protected static String cookParameter(String parameter) {
		String ret = parameter;
		if (ret.indexOf('\\') > -1) {
			ret = ret.replaceAll("\\\\", "\\\\\\\\");
		}
		if (ret.indexOf('%') > -1) {
			ret = ret.replaceAll("%", "\\\\%");
		}
		if (ret.indexOf('_') > -1) {
			ret = ret.replaceAll("_", "\\\\_");
		}
		return ret;
	}
}
