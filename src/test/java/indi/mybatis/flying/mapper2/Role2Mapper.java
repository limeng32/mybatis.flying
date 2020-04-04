package indi.mybatis.flying.mapper2;

import java.util.Collection;

import indi.mybatis.flying.pojo.Role2_;
import indi.mybatis.flying.pojoHelper.MapperFace;

public interface Role2Mapper extends MapperFace<Role2_> {

	public Role2_ selectWithoutCache(Object id);

	@Override
	public Role2_ select(Object id);

	@Override
	public Collection<Role2_> selectAll(Role2_ t);

	@Override
	public Role2_ selectOne(Role2_ t);

	@Override
	public void insert(Role2_ t);

	@Override
	public int update(Role2_ t);

	@Override
	public int updatePersistent(Role2_ t);

	@Override
	public int delete(Role2_ t);

	@Override
	public int count(Role2_ t);

}
