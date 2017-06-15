package indi.mybatis.flying.mapper2;

import java.util.Collection;

import indi.mybatis.flying.pojo.LoginLogSource2;
import indi.mybatis.flying.pojoHelper.MapperFace;

//@CacheRoleAnnotation(ObserverClass = { Account_.class }, TriggerClass = { LoginLog_.class })
public interface LoginLogSource2Mapper extends MapperFace<LoginLogSource2> {

	@Override
	// @CacheAnnotation(role = CacheRoleType.Observer)
	public LoginLogSource2 select(Object id);

	@Override
	// @CacheAnnotation(role = CacheRoleType.Observer)
	public Collection<LoginLogSource2> selectAll(LoginLogSource2 t);

	@Override
	// @CacheAnnotation(role = CacheRoleType.Observer)
	public LoginLogSource2 selectOne(LoginLogSource2 t);

	@Override
	public void insert(LoginLogSource2 t);

	@Override
	// @CacheAnnotation(role = CacheRoleType.Trigger)
	public int update(LoginLogSource2 t);

	@Override
	// @CacheAnnotation(role = CacheRoleType.Trigger)
	public int updatePersistent(LoginLogSource2 t);

	@Override
	// @CacheAnnotation(role = CacheRoleType.Trigger)
	public int delete(LoginLogSource2 t);

	@Override
	// @CacheAnnotation(role = CacheRoleType.Observer)
	public int count(LoginLogSource2 t);

	// public void loadDetail(LoginLog_ loginLog, Detail_ detail);
}
