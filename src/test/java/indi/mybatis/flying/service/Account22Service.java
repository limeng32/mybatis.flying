package indi.mybatis.flying.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indi.mybatis.flying.mapper.Account22Mapper;
import indi.mybatis.flying.pojo.Account22;
import indi.mybatis.flying.pojo.Role2_;
import indi.mybatis.flying.pojoHelper.ServiceSupport;

@Service
public class Account22Service extends ServiceSupport<Account22> implements Account22Mapper {

	@Autowired
	private Account22Mapper mapper;

	@Override
	public Account22 select(Object id) {
		return supportSelect(mapper, id);
	}

	@Override
	public Account22 selectOne(Account22 t) {
		return supportSelectOne(mapper, t);
	}

	@Override
	public void insert(Account22 t) {
		supportInsert(mapper, t);
	}

	@Override
	public int update(Account22 t) {
		return supportUpdate(mapper, t);
	}

	@Override
	public Collection<Account22> selectAll(Account22 t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public int updatePersistent(Account22 t) {
		return supportUpdatePersistent(mapper, t);
	}

	@Override
	public int delete(Account22 t) {
		return supportDelete(mapper, t);
	}

	@Override
	public int count(Account22 t) {
		return supportCount(mapper, t);
	}

	@Override
	public void loadRole2(Role2_ role2_, Account22 account22) {
		role2_.removeAllAccount2();
		account22.setRole(role2_);
		role2_.setAccount2(mapper.selectAll(account22));
	}

}