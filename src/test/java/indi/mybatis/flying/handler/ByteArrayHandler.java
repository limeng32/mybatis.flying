package indi.mybatis.flying.handler;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class ByteArrayHandler extends BaseTypeHandler<String> implements TypeHandler<String> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setBytes(i, parameter.getBytes());
	}

	@Override
	public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
		byte[] blob = rs.getBytes(columnName);
		String ret = null;
		if (null != blob) {
			ret = new String(blob);
		}
		return ret;
	}

	@Override
	public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		byte[] blob = rs.getBytes(columnIndex);
		String ret = null;
		if (null != blob) {
			ret = new String(blob);
		}
		return ret;
	}

	@Override
	public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		byte[] blob = cs.getBytes(columnIndex);
		String ret = null;
		if (null != blob) {
			ret = new String(blob);
		}
		return ret;
	}

}
