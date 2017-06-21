package indi.mybatis.flying.mapper2;

import java.util.Collection;

import indi.mybatis.flying.pojo.LoginLogSource2;
import indi.mybatis.flying.pojoHelper.MapperFace;

public interface LoginLogSource2Mapper extends MapperFace<LoginLogSource2> {

	@Override
	public LoginLogSource2 select(Object id);

	@Override
	public Collection<LoginLogSource2> selectAll(LoginLogSource2 t);

	@Override
	public LoginLogSource2 selectOne(LoginLogSource2 t);

	@Override
	public void insert(LoginLogSource2 t);

	@Override
	public int update(LoginLogSource2 t);

	@Override
	public int updatePersistent(LoginLogSource2 t);

	@Override
	public int delete(LoginLogSource2 t);

	@Override
	public int count(LoginLogSource2 t);

}
