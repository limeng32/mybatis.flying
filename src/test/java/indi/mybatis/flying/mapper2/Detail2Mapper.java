package indi.mybatis.flying.mapper2;

import java.util.Collection;

import indi.mybatis.flying.pojo.Detail2_;
import indi.mybatis.flying.pojo.LoginLogSource2;
import indi.mybatis.flying.pojoHelper.MapperFace;

public interface Detail2Mapper extends MapperFace<Detail2_> {

	public Detail2_ selectWithoutCache(Object id);

	@Override
	public Detail2_ select(Object id);

	@Override
	public Collection<Detail2_> selectAll(Detail2_ t);

	@Override
	public Detail2_ selectOne(Detail2_ t);

	@Override
	public void insert(Detail2_ t);

	public void insertWithoutName(Detail2_ t);

	public void insertWithoutFoo(Detail2_ t);

	public void insertBatchWithoutName(Collection<Detail2_> t);

	@Override
	public int update(Detail2_ t);

	public int updateWithoutName(Detail2_ t);

	@Override
	public int updatePersistent(Detail2_ t);

	public int updatePersistentWithoutName(Detail2_ t);

	@Override
	public int delete(Detail2_ t);

	@Override
	public int count(Detail2_ t);

	public void loadLoginLogSource2(LoginLogSource2 loginLogSource2, Detail2_ detail2);
}
