package indi.mybatis.flying.service2;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indi.mybatis.flying.mapper2.Account2Mapper;
import indi.mybatis.flying.pojo.Account2_;
import indi.mybatis.flying.pojo.Role2_;
import indi.mybatis.flying.pojoHelper.ServiceSupport;

@Service
public class Account2Service extends ServiceSupport<Account2_> implements Account2Mapper {

	@Autowired
	private Account2Mapper mapper;

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

	@Override
	public void loadRole2(Role2_ role2_, Account2_ account2_) {
		role2_.removeAllAccount();
		account2_.setRole(role2_);
		role2_.setAccount(mapper.selectAll(account2_));
	}

	@Override
	public Account2_ selectWithoutCache(Object id) {
		return mapper.selectWithoutCache(id);
	}

}