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
import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.service.LoginLogService;

/**
 * 处理LoginLog_表跨库关联的TypeHandler
 * 
 * @author limeng32
 * 
 */
@MappedTypes({ LoginLog_.class })

public class LoginLogTypeHandler extends BaseTypeHandler<LoginLog_> implements TypeHandler<LoginLog_> {

	@Override
	public LoginLog_ getNullableResult(ResultSet arg0, String arg1) throws SQLException {
		if (arg0.getString(arg1) == null) {
			return null;
		}
		return (getService().select(arg0.getString(arg1)));
	}

	@Override
	public LoginLog_ getNullableResult(ResultSet arg0, int arg1) throws SQLException {
		if (arg0.getString(arg1) == null) {
			return null;
		}
		return (getService().select(arg0.getString(arg1)));
	}

	@Override
	public LoginLog_ getNullableResult(CallableStatement arg0, int arg1) throws SQLException {
		if (arg0.getString(arg1) == null) {
			return null;
		}
		return (getService().select(arg0.getString(arg1)));
	}

	@Override
	public void setNonNullParameter(PreparedStatement arg0, int arg1, LoginLog_ arg2, JdbcType arg3)
			throws SQLException {
		if (arg2 != null) {
			arg0.setString(arg1, arg2.getId().toString());
		}
	}

	private LoginLogService getService() {
		return (LoginLogService) ApplicationContextProvider.getApplicationContext().getBean(LoginLogService.class);
	}
}
