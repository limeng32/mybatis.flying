package indi.mybatis.flying.service;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indi.mybatis.flying.mapper.AccountMapper;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.Role_;
import indi.mybatis.flying.pojoHelper.ServiceSupport;

@Service
public class AccountService extends ServiceSupport<Account_> implements AccountMapper {

	@Autowired
	private AccountMapper mapper;

	@Override
	public Account_ select(Object id) {
		return supportSelect(mapper, id);
	}

	@Override
	public Account_ selectEverything(Object id) {
		return mapper.selectEverything(id);
	}

	@Override
	public Account_ selectWithoutRole(Object id) {
		return mapper.selectWithoutRole(id);
	}

	@Override
	public Account_ selectOne(Account_ t) {
		return supportSelectOne(mapper, t);
	}

	@Override
	public void insert(Account_ t) {
		supportInsert(mapper, t);
	}

	@Override
	public void insertSnowFlake(Account_ t) {
		mapper.insertSnowFlake(t);
	}

	@Override
	public int update(Account_ t) {
		return supportUpdate(mapper, t);
	}

	@Override
	public Collection<Account_> selectAll(Account_ t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public Collection<Account_> selectAllEverything(Account_ t) {
		return mapper.selectAllEverything(t);
	}

	@Override
	public int updatePersistent(Account_ t) {
		return supportUpdatePersistent(mapper, t);
	}

	@Override
	public int delete(Account_ t) {
		return supportDelete(mapper, t);
	}

	@Override
	public int count(Account_ t) {
		return supportCount(mapper, t);
	}

	@Override
	public void loadRole(Role_ role, Account_ account) {
		role.removeAllAccount();
		account.setRole(role);
		role.setAccount(mapper.selectAll(account));
	}

	@Override
	public void loadRoleDeputy(Role_ roleDeputy, Account_ accountDeputy) {
		roleDeputy.removeAllAccountDeputy();
		accountDeputy.setRoleDeputy(roleDeputy);
		roleDeputy.setAccountDeputy(mapper.selectAll(accountDeputy));
	}

	@Override
	public Account_ selectDirect(Object id) {
		return mapper.selectDirect(id);
	}

	@Override
	public Collection<Account_> selectAllDirect(Map<String, Object> map) {
		return mapper.selectAllDirect(map);
	}

	@Override
	public Collection<Account_> selectAccountByRole(Map<String, Object> map) {
		return mapper.selectAccountByRole(map);
	}

}