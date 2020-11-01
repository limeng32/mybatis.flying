package indi.mybatis.flying.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import indi.mybatis.flying.pojo.StoryStatus_;

public class StoryStatusHandler extends BaseTypeHandler<StoryStatus_> implements TypeHandler<StoryStatus_> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, StoryStatus_ parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, parameter.getValue());
	}

	@Override
	public StoryStatus_ getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return StoryStatus_.forValue(rs.getString(columnName));
	}

	@Override
	public StoryStatus_ getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return StoryStatus_.forValue(rs.getString(columnIndex));
	}

	@Override
	public StoryStatus_ getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return StoryStatus_.forValue(cs.getString(columnIndex));
	}

}
