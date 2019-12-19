package indi.mybatis.flying.mapper2;

import java.util.Collection;

import indi.mybatis.flying.annotations.CacheAnnotation;
import indi.mybatis.flying.annotations.CacheRoleAnnotation;
import indi.mybatis.flying.pojo.Role2_;
import indi.mybatis.flying.pojoHelper.MapperFace;
import indi.mybatis.flying.statics.CacheRoleType;

@CacheRoleAnnotation(observerClass = {}, triggerClass = { Role2_.class })
public interface Role2Mapper extends MapperFace<Role2_> {

	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Role2_ selectWithoutCache(Object id);
	
	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Role2_ select(Object id);

	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Collection<Role2_> selectAll(Role2_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Role2_ selectOne(Role2_ t);

	@Override
	public void insert(Role2_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.TRIGGER)
	public int update(Role2_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.TRIGGER)
	public int updatePersistent(Role2_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.TRIGGER)
	public int delete(Role2_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public int count(Role2_ t);

}
