package indi.mybatis.flying.service2;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indi.mybatis.flying.mapper.AccountMapper;
import indi.mybatis.flying.mapper2.Account2Mapper;
import indi.mybatis.flying.pojo.Account2_;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.pojoHelper.ServiceSupport;
import indi.mybatis.flying.service.LoginLogService;

@Service
public class Account2Service extends ServiceSupport<Account2_> implements Account2Mapper {

	@Autowired
	private Account2Mapper mapper;

//	@Autowired
//	private LoginLogService loginLogService;

	@Override
	public Account2_ select(Object id) {
		return supportSelect(mapper, id);
	}

	@Override
	public Account2_ selectOne(Account2_ t) {
		return supportSelectOne(mapper, t);
	}

	@Override
	public void insert(Account2_ t) {
		supportInsert(mapper, t);
	}

	@Override
	public int update(Account2_ t) {
		return supportUpdate(mapper, t);
	}

	@Override
	public Collection<Account2_> selectAll(Account2_ t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public int updatePersistent(Account2_ t) {
		return supportUpdatePersistent(mapper, t);
	}

	@Override
	public int delete(Account2_ t) {
		return supportDelete(mapper, t);
	}

	@Override
	public int count(Account2_ t) {
		return supportCount(mapper, t);
	}

//	@Override
//	public void loadLoginLog(Account_ account, LoginLog_ loginLog) {
//		loginLog.setAccount(account);
//		account.setLoginLog(loginLogService.selectAll(loginLog));
//	}

}