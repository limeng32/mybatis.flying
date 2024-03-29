package indi.mybatis.flying.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indi.mybatis.flying.mapper.AccountMapper;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.Role_;

@Service
public class AccountService {

	@Autowired
	private AccountMapper mapper;

	public Account_ select(Object id) {
		return mapper.select(id);
	}

	public Account_ selectWithIndex(Object id) {
		return mapper.selectWithIndex(id);
	}

	public Account_ selectSimple(Object id) {
		return mapper.selectSimple(id);
	}

	public Account_ selectAsd(Object id) {
		return mapper.selectAsd(id);
	}

	public Account_ selectEverything(Object id) {
		return mapper.selectEverything(id);
	}

	public Account_ selectWithoutRole(Object id) {
		return mapper.selectWithoutRole(id);
	}

	public Account_ selectOne(Account_ t) {
		return mapper.selectOne(t);
	}

	public void insert(Account_ t) {
		mapper.insert(t);
	}
	
	public void insertDirect(Account_ t) {
		mapper.insertDirect(t);
	}

	public void insertBatch(Collection<Account_> t) {
		mapper.insertBatch(t);
	}

	public void insertSnowFlake(Account_ t) {
		mapper.insertSnowFlake(t);
	}

	public void insertSnowFlakeBatch(Collection<Account_> t) {
		mapper.insertSnowFlakeBatch(t);
	}

	public void insertSimpleNoName(Account_ t) {
		mapper.insertSimpleNoName(t);
	}

	public void insertBatchSimpleNoName(Collection<Account_> t) {
		mapper.insertBatchSimpleNoName(t);
	}

	public int update(Account_ t) {
		return mapper.update(t);
	}

	public int updateSimpleNoName(Account_ t) {
		return mapper.updateSimpleNoName(t);
	}

	public List<Account_> selectAll(Account_ t) {
		return mapper.selectAll(t);
	}

	public Collection<Account_> selectAllPrefix(Account_ t) {
		return mapper.selectAllPrefix(t);
	}

	public Collection<Account_> selectAllPrefixIgnore(Account_ t) {
		return mapper.selectAllPrefixIgnore(t);
	}

	public Collection<Account_> selectAllEverything(Account_ t) {
		return mapper.selectAllEverything(t);
	}

	public int updatePersistent(Account_ t) {
		return mapper.updatePersistent(t);
	}

	public int updatePersistentSimpleNoName(Account_ t) {
		return mapper.updatePersistentSimpleNoName(t);
	}

	public int delete(Account_ t) {
		return mapper.delete(t);
	}

	public int count(Account_ t) {
		return mapper.count(t);
	}

	public int countAsd(Account_ t) {
		return mapper.countAsd(t);
	}

	public void loadRole(Role_ role, Account_ account) {
		role.removeAllAccount();
		account.setRole(role);
		role.setAccount(mapper.selectAll(account));
	}

	public void loadRoleDeputy(Role_ roleDeputy, Account_ accountDeputy) {
		roleDeputy.removeAllAccountDeputy();
		accountDeputy.setRoleDeputy(roleDeputy);
		roleDeputy.setAccountDeputy(mapper.selectAll(accountDeputy));
	}

	public Account_ selectDirect(Object id) {
		return mapper.selectDirect(id);
	}

	public Collection<Account_> selectAllDirect(Map<String, Object> map) {
		return mapper.selectAllDirect(map);
	}

	public Collection<Account_> selectAccountByRole(Map<String, Object> map) {
		return mapper.selectAccountByRole(map);
	}

	public Account_ selectWithoutCache(Object id) {
		return mapper.selectWithoutCache(id);
	}

	public int selectCheckHealth() {
		return mapper.selectCheckHealth();
	}

	public int updateBatch(Collection<Account_> t) {
		return mapper.updateBatch(t);
	}

	public List<Account_> selectUseOffset(int limit, int offset) {
		return mapper.selectUseOffset(limit, offset);
	}
}