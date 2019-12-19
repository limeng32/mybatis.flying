package indi.mybatis.flying.mapper2;

import java.util.Collection;

import indi.mybatis.flying.annotations.CacheAnnotation;
import indi.mybatis.flying.annotations.CacheRoleAnnotation;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.LoginLogSource2;
import indi.mybatis.flying.pojoHelper.MapperFace;
import indi.mybatis.flying.statics.CacheRoleType;

@CacheRoleAnnotation(observerClass = { Account_.class }, triggerClass = { LoginLogSource2.class })
public interface LoginLogSource2Mapper extends MapperFace<LoginLogSource2> {

	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public LoginLogSource2 selectWithoutCache(Object id);
	
	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public LoginLogSource2 select(Object id);

	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public LoginLogSource2 selectWithoutAccount(Object id);

	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Collection<LoginLogSource2> selectAll(LoginLogSource2 t);

	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public LoginLogSource2 selectOne(LoginLogSource2 t);

	@Override
	public void insert(LoginLogSource2 t);

	@Override
	@CacheAnnotation(role = CacheRoleType.TRIGGER)
	public int update(LoginLogSource2 t);
	
	public int updateNoFlush(LoginLogSource2 t);

	@Override
	@CacheAnnotation(role = CacheRoleType.TRIGGER)
	public int updatePersistent(LoginLogSource2 t);

	@Override
	@CacheAnnotation(role = CacheRoleType.TRIGGER)
	public int delete(LoginLogSource2 t);

	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public int count(LoginLogSource2 t);

	public void loadAccount(Account_ account, LoginLogSource2 loginLogSource2);
}
