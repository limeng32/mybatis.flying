package indi.mybatis.flying.mapper;

import java.util.Collection;

import indi.mybatis.flying.pojo.Account22;
import indi.mybatis.flying.pojo.Role2_;
import indi.mybatis.flying.pojoHelper.MapperFace;

public interface Account22Mapper extends MapperFace<Account22> {

	public Account22 selectWithoutCache(Object id);

	@Override
	public Account22 select(Object id);

	@Override
	public Collection<Account22> selectAll(Account22 t);

	@Override
	public Account22 selectOne(Account22 t);

	@Override
	public void insert(Account22 t);

	@Override
	public int update(Account22 t);

	@Override
	public int updatePersistent(Account22 t);

	@Override
	public int delete(Account22 t);

	@Override
	public int count(Account22 t);

	public void loadRole2(Role2_ role2_, Account22 account22);
}
