package indi.mybatis.flying.mapper;

import java.util.Collection;

import indi.mybatis.flying.annotations.CacheAnnotation;
import indi.mybatis.flying.annotations.CacheRoleAnnotation;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.Role_;
import indi.mybatis.flying.pojoHelper.MapperFace;
import indi.mybatis.flying.statics.CacheRoleType;

@CacheRoleAnnotation(ObserverClass = { Role_.class }, TriggerClass = { Account_.class })
public interface AccountMapper extends MapperFace<Account_> {

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public Account_ select(Object id);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public Collection<Account_> selectAll(Account_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public Account_ selectOne(Account_ t);

	@Override
	public void insert(Account_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int update(Account_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int updatePersistent(Account_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int delete(Account_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public int count(Account_ t);

	public void loadRole(Role_ role, Account_ account);

	public void loadRoleDeputy(Role_ roleDeputy, Account_ accountDeputy);
}
