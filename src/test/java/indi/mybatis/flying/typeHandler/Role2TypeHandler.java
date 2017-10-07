package indi.mybatis.flying.typeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import indi.mybatis.flying.ApplicationContextProvider;
import indi.mybatis.flying.pojo.Role2_;
import indi.mybatis.flying.service2.Role2Service;

/**
 * 处理Role2_表跨库关联的TypeHandler
 * 
 * @author limeng32
 * 
 */
@MappedTypes({ Role2_.class })

public class Role2TypeHandler extends BaseTypeHandler<Role2_> implements TypeHandler<Role2_> {

	@Override
	public Role2_ getNullableResult(ResultSet arg0, String arg1) throws SQLException {
		if (arg0.getString(arg1) == null) {
			return null;
		}
		return (getService().select(arg0.getString(arg1)));
	}

	@Override
	public Role2_ getNullableResult(ResultSet arg0, int arg1) throws SQLException {
		if (arg0.getString(arg1) == null) {
			return null;
		}
		return (getService().select(arg0.getString(arg1)));
	}

	@Override
	public Role2_ getNullableResult(CallableStatement arg0, int arg1) throws SQLException {
		if (arg0.getString(arg1) == null) {
			return null;
		}
		return (getService().select(arg0.getString(arg1)));
	}

	@Override
	public void setNonNullParameter(PreparedStatement arg0, int arg1, Role2_ arg2, JdbcType arg3) throws SQLException {
		if (arg2 != null) {
			arg0.setString(arg1, arg2.getId().toString());
		}
	}

	private Role2Service getService() {
		return (Role2Service) ApplicationContextProvider.getApplicationContext().getBean(Role2Service.class);
	}
}
