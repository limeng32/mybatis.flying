package indi.mybatis.flying.mapper;

import java.util.Collection;

import indi.mybatis.flying.annotations.CacheAnnotation;
import indi.mybatis.flying.annotations.CacheRoleAnnotation;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.Role_;
import indi.mybatis.flying.pojoHelper.MapperFace;
import indi.mybatis.flying.statics.CacheRoleType;

@CacheRoleAnnotation(ObserverClass = {}, TriggerClass = { Role_.class })
public interface RoleMapper extends MapperFace<Role_> {

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public Role_ select(Object id);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public Collection<Role_> selectAll(Role_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public Role_ selectOne(Role_ t);

	@Override
	public void insert(Role_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int update(Role_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int updatePersistent(Role_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int delete(Role_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public int count(Role_ t);

	public void loadAccount(Role_ role, Account_ account);

	public void loadAccountDeputy(Role_ roleDeputy, Account_ accountDeputy);
}
