package indi.mybatis.flying.mapper2;

import java.util.Collection;

import indi.mybatis.flying.pojo.Source2LoginLog_;
import indi.mybatis.flying.pojoHelper.MapperFace;

//@CacheRoleAnnotation(ObserverClass = { Account_.class }, TriggerClass = { LoginLog_.class })
public interface Source2LoginLogMapper extends MapperFace<Source2LoginLog_> {

	@Override
	// @CacheAnnotation(role = CacheRoleType.Observer)
	public Source2LoginLog_ select(Object id);

	@Override
	// @CacheAnnotation(role = CacheRoleType.Observer)
	public Collection<Source2LoginLog_> selectAll(Source2LoginLog_ t);

	@Override
	// @CacheAnnotation(role = CacheRoleType.Observer)
	public Source2LoginLog_ selectOne(Source2LoginLog_ t);

	@Override
	public void insert(Source2LoginLog_ t);

	@Override
	// @CacheAnnotation(role = CacheRoleType.Trigger)
	public int update(Source2LoginLog_ t);

	@Override
	// @CacheAnnotation(role = CacheRoleType.Trigger)
	public int updatePersistent(Source2LoginLog_ t);

	@Override
	// @CacheAnnotation(role = CacheRoleType.Trigger)
	public int delete(Source2LoginLog_ t);

	@Override
	// @CacheAnnotation(role = CacheRoleType.Observer)
	public int count(Source2LoginLog_ t);

	// public void loadDetail(LoginLog_ loginLog, Detail_ detail);
}
