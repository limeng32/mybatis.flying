package indi.mybatis.flying.mapper;

import java.util.Collection;
import java.util.Map;

import indi.mybatis.flying.annotations.CacheAnnotation;
import indi.mybatis.flying.annotations.CacheRoleAnnotation;
import indi.mybatis.flying.pojo.Role_;
import indi.mybatis.flying.pojoHelper.MapperFace;
import indi.mybatis.flying.statics.CacheRoleType;

@CacheRoleAnnotation(observerClass = {}, triggerClass = { Role_.class })
public interface RoleMapper extends MapperFace<Role_> {

	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Role_ selectWithoutCache(Object id);
	
	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Role_ select(Object id);

	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Role_ selectEverything(Object id);

	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Role_ selectNoId(Object id);

	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Collection<Role_> selectAll(Role_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public Role_ selectOne(Role_ t);

	@Override
	public void insert(Role_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.TRIGGER)
	public int update(Role_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.TRIGGER)
	public int updatePersistent(Role_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.TRIGGER)
	public int delete(Role_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.OBSERVER)
	public int count(Role_ t);

	@CacheAnnotation(role = CacheRoleType.TRIGGER)
	public int updateDirect(Map<String, Object> m);
}
