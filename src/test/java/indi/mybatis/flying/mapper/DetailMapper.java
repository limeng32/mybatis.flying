package indi.mybatis.flying.mapper;

import java.util.Collection;

import indi.mybatis.flying.annotations.CacheAnnotation;
import indi.mybatis.flying.annotations.CacheRoleAnnotation;
import indi.mybatis.flying.pojo.Detail_;
import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.pojoHelper.MapperFace;
import indi.mybatis.flying.statics.CacheRoleType;

@CacheRoleAnnotation(ObserverClass = { LoginLog_.class }, TriggerClass = { Detail_.class })
public interface DetailMapper extends MapperFace<Detail_> {

	@CacheAnnotation(role = CacheRoleType.Observer)
	public Detail_ selectWithoutCache(Object id);
	
	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public Detail_ select(Object id);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public Collection<Detail_> selectAll(Detail_ t);
	
	public Collection<Detail_> selectAllPrefix(Detail_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public Detail_ selectOne(Detail_ t);

	@Override
	public void insert(Detail_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int update(Detail_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int updatePersistent(Detail_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int delete(Detail_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public int count(Detail_ t);
	
	public void loadLoginLog(LoginLog_ loginLog, Detail_ detail);
}
