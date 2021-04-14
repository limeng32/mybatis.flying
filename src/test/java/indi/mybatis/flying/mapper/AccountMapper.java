package indi.mybatis.flying.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.Role_;
import indi.mybatis.flying.pojoHelper.MapperFace;

public interface AccountMapper extends MapperFace<Account_> {

	@Override
	public Account_ select(Object id);

	public Account_ selectAsd(Object id);

	public Account_ selectWithIndex(Object id);

	public Account_ selectSimple(Object id);

	public Account_ selectWithoutCache(Object id);

	public Account_ selectEverything(Object id);

	public Account_ selectWithoutRole(Object id);

	@Override
	public Collection<Account_> selectAll(Account_ t);

	public Collection<Account_> selectAllPrefix(Account_ t);

	public Collection<Account_> selectAllPrefixIgnore(Account_ t);

	public Collection<Account_> selectAllEverything(Account_ t);

	@Override
	public Account_ selectOne(Account_ t);

	@Override
	public void insert(Account_ t);

	public void insertBatch(Collection<Account_> t);

	public void insertSnowFlake(Account_ t);

	public void insertSnowFlakeBatch(Collection<Account_> t);

	public void insertSimpleNoName(Account_ t);

	public void insertBatchSimpleNoName(Collection<Account_> t);

	@Override
	public int update(Account_ t);

	public int updateSimpleNoName(Account_ t);

	@Override
	public int updatePersistent(Account_ t);

	public int updatePersistentSimpleNoName(Account_ t);

	@Override
	public int delete(Account_ t);

	@Override
	public int count(Account_ t);

	public int countAsd(Account_ t);

	public void loadRole(Role_ role, Account_ account);

	public void loadRoleDeputy(Role_ roleDeputy, Account_ accountDeputy);

	public Account_ selectDirect(Object id);

	public Collection<Account_> selectAllDirect(Map<String, Object> map);

	public Collection<Account_> selectAccountByRole(Map<String, Object> map);

	public int selectCheckHealth();

	public int updateBatch(Collection<Account_> t);

	public List<Map<String, Object>> selectGroupBy2();

	public Collection<Account_> selectAllDirect2(@Param("name") String name, @Param("email") String email);
	
	public Collection<Account_> selectAllDirect3(Account_ t);
}
