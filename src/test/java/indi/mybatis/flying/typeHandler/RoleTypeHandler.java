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
import indi.mybatis.flying.pojo.Role_;
import indi.mybatis.flying.service.RoleService;

/**
 * 处理Role_表跨库关联的TypeHandler
 * 
 * @author limeng32
 * 
 */
@MappedTypes({ Role_.class })

public class RoleTypeHandler extends BaseTypeHandler<Role_> implements TypeHandler<Role_> {

	@Override
	public Role_ getNullableResult(ResultSet arg0, String arg1) throws SQLException {
		if (arg0.getString(arg1) == null) {
			return null;
		}
		return (getService().select(arg0.getString(arg1)));
	}

	@Override
	public Role_ getNullableResult(ResultSet arg0, int arg1) throws SQLException {
		if (arg0.getString(arg1) == null) {
			return null;
		}
		return (getService().select(arg0.getString(arg1)));
	}

	@Override
	public Role_ getNullableResult(CallableStatement arg0, int arg1) throws SQLException {
		if (arg0.getString(arg1) == null) {
			return null;
		}
		return (getService().select(arg0.getString(arg1)));
	}

	@Override
	public void setNonNullParameter(PreparedStatement arg0, int arg1, Role_ arg2, JdbcType arg3) throws SQLException {
		if (arg2 != null) {
			arg0.setString(arg1, arg2.getId().toString());
		}
	}

	private RoleService getService() {
		return (RoleService) ApplicationContextProvider.getApplicationContext().getBean(RoleService.class);
	}
}
