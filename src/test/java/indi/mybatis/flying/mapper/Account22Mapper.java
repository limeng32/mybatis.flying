package indi.mybatis.flying.mapper;

import java.util.Collection;

import indi.mybatis.flying.annotations.CacheAnnotation;
import indi.mybatis.flying.annotations.CacheRoleAnnotation;
import indi.mybatis.flying.pojo.Account22;
import indi.mybatis.flying.pojo.Role2_;
import indi.mybatis.flying.pojoHelper.MapperFace;
import indi.mybatis.flying.statics.CacheRoleType;

@CacheRoleAnnotation(observerClass = { Role2_.class }, triggerClass = { Account22.class })
public interface Account22Mapper extends MapperFace<Account22> {

	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Account22 selectWithoutCache(Object id);

	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Account22 select(Object id);

	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Collection<Account22> selectAll(Account22 t);

	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Account22 selectOne(Account22 t);

	@Override
	public void insert(Account22 t);

	@Override
	@CacheAnnotation(role = CacheRoleType.TRIGGER)
	public int update(Account22 t);

	@Override
	@CacheAnnotation(role = CacheRoleType.TRIGGER)
	public int updatePersistent(Account22 t);

	@Override
	@CacheAnnotation(role = CacheRoleType.TRIGGER)
	public int delete(Account22 t);

	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public int count(Account22 t);

	public void loadRole2(Role2_ role2_, Account22 account22);
}
