package indi.mybatis.flying.mapper2;

import java.util.Collection;

import indi.mybatis.flying.annotations.CacheAnnotation;
import indi.mybatis.flying.annotations.CacheRoleAnnotation;
import indi.mybatis.flying.pojo.Account2_;
import indi.mybatis.flying.pojo.Role2_;
import indi.mybatis.flying.pojoHelper.MapperFace;
import indi.mybatis.flying.statics.CacheRoleType;

@CacheRoleAnnotation(observerClass = { Role2_.class }, triggerClass = { Account2_.class })
public interface Account2Mapper extends MapperFace<Account2_> {

	@CacheAnnotation(role = CacheRoleType.Observer)
	public Account2_ selectWithoutCache(Object id);
	
	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public Account2_ select(Object id);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public Collection<Account2_> selectAll(Account2_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public Account2_ selectOne(Account2_ t);

	@Override
	public void insert(Account2_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int update(Account2_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int updatePersistent(Account2_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int delete(Account2_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public int count(Account2_ t);

	public void loadRole2(Role2_ role2_, Account2_ account2_);
}
