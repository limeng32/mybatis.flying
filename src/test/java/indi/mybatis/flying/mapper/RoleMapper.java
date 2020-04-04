package indi.mybatis.flying.mapper;

import java.util.Collection;
import java.util.Map;

import indi.mybatis.flying.pojo.Role_;
import indi.mybatis.flying.pojoHelper.MapperFace;

public interface RoleMapper extends MapperFace<Role_> {

	public Role_ selectWithoutCache(Object id);

	@Override
	public Role_ select(Object id);

	public Role_ selectEverything(Object id);

	public Role_ selectNoId(Object id);

	@Override
	public Collection<Role_> selectAll(Role_ t);

	@Override
	public Role_ selectOne(Role_ t);

	@Override
	public void insert(Role_ t);

	@Override
	public int update(Role_ t);

	@Override
	public int updatePersistent(Role_ t);

	@Override
	public int delete(Role_ t);

	@Override
	public int count(Role_ t);

	public int updateDirect(Map<String, Object> m);
}
