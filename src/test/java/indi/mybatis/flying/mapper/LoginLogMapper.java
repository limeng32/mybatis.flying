package indi.mybatis.flying.mapper;

import java.util.Collection;

import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.pojoHelper.MapperFace;

public interface LoginLogMapper extends MapperFace<LoginLog_> {

	public LoginLog_ selectWithoutCache(Object id);

	@Override
	public LoginLog_ select(Object id);

	public LoginLog_ selectPrefix(Object id);

	@Override
	public Collection<LoginLog_> selectAll(LoginLog_ t);

	public Collection<LoginLog_> selectAllPrefix(LoginLog_ t);

	public Collection<LoginLog_> selectAllPrefixIgnore(LoginLog_ t);

	public Collection<LoginLog_> selectAllPrefixIgnore2(LoginLog_ t);

	@Override
	public LoginLog_ selectOne(LoginLog_ t);

	public LoginLog_ selectOnePrefix(LoginLog_ t);

	public LoginLog_ selectOneSimple(LoginLog_ t);

	@Override
	public void insert(LoginLog_ t);

	@Override
	public int update(LoginLog_ t);

	@Override
	public int updatePersistent(LoginLog_ t);

	@Override
	public int delete(LoginLog_ t);

	@Override
	public int count(LoginLog_ t);

	public void loadAccount(Account_ account, LoginLog_ loginLog);

}
