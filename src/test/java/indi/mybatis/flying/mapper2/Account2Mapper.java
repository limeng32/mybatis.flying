package indi.mybatis.flying.mapper2;

import java.util.Collection;

import indi.mybatis.flying.pojo.Account2_;
import indi.mybatis.flying.pojo.Role2_;
import indi.mybatis.flying.pojoHelper.MapperFace;

public interface Account2Mapper extends MapperFace<Account2_> {

	public Account2_ selectWithoutCache(Object id);

	@Override
	public Account2_ select(Object id);

	@Override
	public Collection<Account2_> selectAll(Account2_ t);

	@Override
	public Account2_ selectOne(Account2_ t);

	@Override
	public void insert(Account2_ t);

	@Override
	public int update(Account2_ t);

	@Override
	public int updatePersistent(Account2_ t);

	@Override
	public int delete(Account2_ t);

	@Override
	public int count(Account2_ t);

	public void loadRole2(Role2_ role2_, Account2_ account2_);
}
