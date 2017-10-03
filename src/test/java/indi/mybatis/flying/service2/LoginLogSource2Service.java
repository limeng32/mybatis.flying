package indi.mybatis.flying.service2;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indi.mybatis.flying.mapper2.LoginLogSource2Mapper;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.LoginLogSource2;
import indi.mybatis.flying.pojoHelper.ServiceSupport;

@Service
public class LoginLogSource2Service extends ServiceSupport<LoginLogSource2> implements LoginLogSource2Mapper {

	@Autowired
	private LoginLogSource2Mapper mapper;

	// @Autowired
	// private DetailService detailService;

	@Override
	public LoginLogSource2 select(Object id) {
		return supportSelect(mapper, id);
	}

	@Override
	public LoginLogSource2 selectOne(LoginLogSource2 t) {
		return supportSelectOne(mapper, t);
	}

	@Override
	public void insert(LoginLogSource2 t) {
		supportInsert(mapper, t);
	}

	@Override
	public int update(LoginLogSource2 t) {
		return supportUpdate(mapper, t);
	}

	@Override
	public Collection<LoginLogSource2> selectAll(LoginLogSource2 t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public int updatePersistent(LoginLogSource2 t) {
		return supportUpdatePersistent(mapper, t);
	}

	@Override
	public int delete(LoginLogSource2 t) {
		return supportDelete(mapper, t);
	}

	@Override
	public int count(LoginLogSource2 t) {
		return supportCount(mapper, t);
	}

	@Override
	public void loadAccount(Account_ account, LoginLogSource2 loginLogSource2) {
		account.removeAllLoginLogSource2();
		loginLogSource2.setAccount(account);
		account.setLoginLogSource2(mapper.selectAll(loginLogSource2));
	}
}