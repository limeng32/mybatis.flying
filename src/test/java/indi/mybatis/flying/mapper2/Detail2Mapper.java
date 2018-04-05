package indi.mybatis.flying.mapper2;

import java.util.Collection;

import indi.mybatis.flying.annotations.CacheAnnotation;
import indi.mybatis.flying.annotations.CacheRoleAnnotation;
import indi.mybatis.flying.pojo.Detail2_;
import indi.mybatis.flying.pojo.LoginLogSource2;
import indi.mybatis.flying.pojoHelper.MapperFace;
import indi.mybatis.flying.statics.CacheRoleType;

@CacheRoleAnnotation(ObserverClass = { LoginLogSource2.class }, TriggerClass = { Detail2_.class })
public interface Detail2Mapper extends MapperFace<Detail2_> {

	@CacheAnnotation(role = CacheRoleType.Observer)
	public Detail2_ selectWithoutCache(Object id);
	
	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public Detail2_ select(Object id);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public Collection<Detail2_> selectAll(Detail2_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public Detail2_ selectOne(Detail2_ t);

	@Override
	public void insert(Detail2_ t);

	public void insertWithoutName(Detail2_ t);

	public void insertWithoutFoo(Detail2_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int update(Detail2_ t);

	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int updateWithoutName(Detail2_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int updatePersistent(Detail2_ t);

	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int updatePersistentWithoutName(Detail2_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int delete(Detail2_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public int count(Detail2_ t);

	public void loadLoginLogSource2(LoginLogSource2 loginLogSource2, Detail2_ detail2);
}
