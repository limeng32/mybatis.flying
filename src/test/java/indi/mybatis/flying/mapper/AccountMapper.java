package indi.mybatis.flying.mapper;

import java.util.Collection;
import java.util.Map;

import indi.mybatis.flying.annotations.CacheAnnotation;
import indi.mybatis.flying.annotations.CacheRoleAnnotation;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.Role_;
import indi.mybatis.flying.pojoHelper.MapperFace;
import indi.mybatis.flying.statics.CacheRoleType;

@CacheRoleAnnotation(observerClass = { Role_.class }, triggerClass = { Account_.class })
public interface AccountMapper extends MapperFace<Account_> {

	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Account_ select(Object id);

	public Account_ selectAsd(Object id);

	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Account_ selectWithoutCache(Object id);

	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Account_ selectEverything(Object id);

	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Account_ selectWithoutRole(Object id);

	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Collection<Account_> selectAll(Account_ t);

	public Collection<Account_> selectAllPrefix(Account_ t);

	public Collection<Account_> selectAllPrefixIgnore(Account_ t);

	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Collection<Account_> selectAllEverything(Account_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Account_ selectOne(Account_ t);

	@Override
	public void insert(Account_ t);

	public void insertBatch(Collection<Account_> t);

	public void insertSnowFlake(Account_ t);
	
	public void insertSnowFlakeBatch(Collection<Account_> t);

	@Override
	@CacheAnnotation(role = CacheRoleType.TRIGGER)
	public int update(Account_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.TRIGGER)
	public int updatePersistent(Account_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.TRIGGER)
	public int delete(Account_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public int count(Account_ t);

	public int countAsd(Account_ t);

	public void loadRole(Role_ role, Account_ account);

	public void loadRoleDeputy(Role_ roleDeputy, Account_ accountDeputy);

	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Account_ selectDirect(Object id);

	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Collection<Account_> selectAllDirect(Map<String, Object> map);

	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Collection<Account_> selectAccountByRole(Map<String, Object> map);

	public int selectCheckHealth();
}
