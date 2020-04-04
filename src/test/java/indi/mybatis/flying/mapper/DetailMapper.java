package indi.mybatis.flying.mapper;

import java.util.Collection;

import indi.mybatis.flying.pojo.Detail_;
import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.pojoHelper.MapperFace;

public interface DetailMapper extends MapperFace<Detail_> {

	public Detail_ selectWithoutCache(Object id);

	@Override
	public Detail_ select(Object id);

	public Detail_ selectPrefixIgnore(Object id);

	@Override
	public Collection<Detail_> selectAll(Detail_ t);

	public Collection<Detail_> selectAllPrefix(Detail_ t);

	public Collection<Detail_> selectAllPrefix2(Detail_ t);

	public Collection<Detail_> selectAllPrefixIgnore(Detail_ t);

	public Collection<Detail_> selectAllPrefixIgnore2(Detail_ t);

	public Collection<Detail_> selectAllPrefixIgnore3(Detail_ t);

	@Override
	public Detail_ selectOne(Detail_ t);

	public Detail_ selectOnePrefixIgnore(Detail_ t);

	@Override
	public void insert(Detail_ t);

	@Override
	public int update(Detail_ t);

	@Override
	public int updatePersistent(Detail_ t);

	@Override
	public int delete(Detail_ t);

	@Override
	public int count(Detail_ t);

	public void loadLoginLog(LoginLog_ loginLog, Detail_ detail);
}
