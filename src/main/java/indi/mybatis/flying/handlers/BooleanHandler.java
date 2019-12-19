package indi.mybatis.flying.handlers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 * @description The handler for the Boolean type object.
 */
@MappedTypes({ Boolean.class })
public class BooleanHandler extends BaseTypeHandler<Boolean> implements TypeHandler<Boolean> {

	@Override
	public Boolean getNullableResult(ResultSet arg0, String arg1) throws SQLException {
		return arg0.getInt(arg1) > 0;
	}

	@Override
	public Boolean getNullableResult(ResultSet arg0, int arg1) throws SQLException {
		return arg0.getInt(arg1) > 0;
	}

	@Override
	public Boolean getNullableResult(CallableStatement arg0, int arg1) throws SQLException {
		return arg0.getInt(arg1) > 0;
	}

	@Override
	public void setNonNullParameter(PreparedStatement arg0, int arg1, Boolean arg2, JdbcType arg3) throws SQLException {
		if (arg2) {
			arg0.setInt(arg1, 1);
		} else {
			arg0.setInt(arg1, 0);
		}
	}

}
