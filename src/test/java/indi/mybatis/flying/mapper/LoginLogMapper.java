package indi.mybatis.flying.mapper;

import java.util.Collection;

import indi.mybatis.flying.annotations.CacheAnnotation;
import indi.mybatis.flying.annotations.CacheRoleAnnotation;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.pojoHelper.MapperFace;
import indi.mybatis.flying.statics.CacheRoleType;

@CacheRoleAnnotation(ObserverClass = { Account_.class }, TriggerClass = { LoginLog_.class })
public interface LoginLogMapper extends MapperFace<LoginLog_> {

	@CacheAnnotation(role = CacheRoleType.Observer)
	public LoginLog_ selectWithoutCache(Object id);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public LoginLog_ select(Object id);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public Collection<LoginLog_> selectAll(LoginLog_ t);

	public Collection<LoginLog_> selectAllPrefix(LoginLog_ t);

	public Collection<LoginLog_> selectAllPrefixIgnore(LoginLog_ t);
	
	public Collection<LoginLog_> selectAllPrefixIgnore2(LoginLog_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public LoginLog_ selectOne(LoginLog_ t);

	@Override
	public void insert(LoginLog_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int update(LoginLog_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int updatePersistent(LoginLog_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Trigger)
	public int delete(LoginLog_ t);

	@Override
	@CacheAnnotation(role = CacheRoleType.Observer)
	public int count(LoginLog_ t);

	public void loadAccount(Account_ account, LoginLog_ loginLog);

}
