package indi.mybatis.flying.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indi.mybatis.flying.mapper.LoginLogMapper;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.pojoHelper.ServiceSupport;

@Service
public class LoginLogService extends ServiceSupport<LoginLog_> implements LoginLogMapper {

	@Autowired
	private LoginLogMapper mapper;

	@Override
	public LoginLog_ select(Object id) {
		return supportSelect(mapper, id);
	}

	@Override
	public LoginLog_ selectOne(LoginLog_ t) {
		return supportSelectOne(mapper, t);
	}

	@Override
	public void insert(LoginLog_ t) {
		supportInsert(mapper, t);
	}

	@Override
	public int update(LoginLog_ t) {
		return supportUpdate(mapper, t);
	}

	@Override
	public Collection<LoginLog_> selectAll(LoginLog_ t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public Collection<LoginLog_> selectAllPrefix(LoginLog_ t) {
		return mapper.selectAllPrefix(t);
	}
	
	@Override
	public Collection<LoginLog_> selectAllPrefixIgnore(LoginLog_ t) {
		return mapper.selectAllPrefixIgnore(t);
	}
	
	@Override
	public Collection<LoginLog_> selectAllPrefixIgnore2(LoginLog_ t) {
		return mapper.selectAllPrefixIgnore2(t);
	}

	@Override
	public int updatePersistent(LoginLog_ t) {
		return supportUpdatePersistent(mapper, t);
	}

	@Override
	public int delete(LoginLog_ t) {
		return supportDelete(mapper, t);
	}

	@Override
	public int count(LoginLog_ t) {
		return supportCount(mapper, t);
	}

	@Override
	public void loadAccount(Account_ account, LoginLog_ loginLog) {
		account.removeAllLoginLog();
		loginLog.setAccount(account);
		account.setLoginLog(mapper.selectAll(loginLog));
	}

	@Override
	public LoginLog_ selectWithoutCache(Object id) {
		return mapper.selectWithoutCache(id);
	}
}