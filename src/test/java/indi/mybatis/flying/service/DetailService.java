package indi.mybatis.flying.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indi.mybatis.flying.mapper.DetailMapper;
import indi.mybatis.flying.pojo.Detail_;
import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.pojoHelper.ServiceSupport;

@Service
public class DetailService extends ServiceSupport<Detail_> implements DetailMapper {

	@Autowired
	private DetailMapper mapper;

	@Override
	public Detail_ select(Object id) {
		return supportSelect(mapper, id);
	}

	@Override
	public Detail_ selectOne(Detail_ t) {
		return supportSelectOne(mapper, t);
	}

	@Override
	public void insert(Detail_ t) {
		supportInsert(mapper, t);
	}

	@Override
	public int update(Detail_ t) {
		return supportUpdate(mapper, t);
	}

	@Override
	public Collection<Detail_> selectAll(Detail_ t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public Collection<Detail_> selectAllPrefix(Detail_ t) {
		return mapper.selectAllPrefix(t);
	}
	
	@Override
	public Collection<Detail_> selectAllPrefix2(Detail_ t) {
		return mapper.selectAllPrefix2(t);
	}

	@Override
	public int updatePersistent(Detail_ t) {
		return supportUpdatePersistent(mapper, t);
	}

	@Override
	public int delete(Detail_ t) {
		return supportDelete(mapper, t);
	}

	@Override
	public int count(Detail_ t) {
		return supportCount(mapper, t);
	}

	@Override
	public void loadLoginLog(LoginLog_ loginlog, Detail_ detail) {
		loginlog.removeAllDetail();
		detail.setLoginLog(loginlog);
		loginlog.setDetail(mapper.selectAll(detail));
	}

	@Override
	public Detail_ selectWithoutCache(Object id) {
		return mapper.selectWithoutCache(id);
	}
}