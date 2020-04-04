package indi.mybatis.flying.mapper2;

import java.util.Collection;

import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.LoginLogSource2;
import indi.mybatis.flying.pojoHelper.MapperFace;

public interface LoginLogSource2Mapper extends MapperFace<LoginLogSource2> {

	public LoginLogSource2 selectWithoutCache(Object id);

	@Override
	public LoginLogSource2 select(Object id);

	public LoginLogSource2 selectWithoutAccount(Object id);

	@Override
	public Collection<LoginLogSource2> selectAll(LoginLogSource2 t);

	@Override
	public LoginLogSource2 selectOne(LoginLogSource2 t);

	@Override
	public void insert(LoginLogSource2 t);

	@Override
	public int update(LoginLogSource2 t);

	public int updateNoFlush(LoginLogSource2 t);

	@Override
	public int updatePersistent(LoginLogSource2 t);

	@Override
	public int delete(LoginLogSource2 t);

	@Override
	public int count(LoginLogSource2 t);

	public void loadAccount(Account_ account, LoginLogSource2 loginLogSource2);
}
