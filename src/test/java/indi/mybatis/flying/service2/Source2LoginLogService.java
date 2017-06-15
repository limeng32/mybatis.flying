package indi.mybatis.flying.service2;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indi.mybatis.flying.mapper2.Source2LoginLogMapper;
import indi.mybatis.flying.pojo.Source2LoginLog_;
import indi.mybatis.flying.pojoHelper.ServiceSupport;

@Service
public class Source2LoginLogService extends ServiceSupport<Source2LoginLog_> implements Source2LoginLogMapper {

	@Autowired
	private Source2LoginLogMapper mapper;

//	@Autowired
//	private DetailService detailService;

	@Override
	public Source2LoginLog_ select(Object id) {
		return supportSelect(mapper, id);
	}

	@Override
	public Source2LoginLog_ selectOne(Source2LoginLog_ t) {
		return supportSelectOne(mapper, t);
	}

	@Override
	public void insert(Source2LoginLog_ t) {
		supportInsert(mapper, t);
	}

	@Override
	public int update(Source2LoginLog_ t) {
		return supportUpdate(mapper, t);
	}

	@Override
	public Collection<Source2LoginLog_> selectAll(Source2LoginLog_ t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public int updatePersistent(Source2LoginLog_ t) {
		return supportUpdatePersistent(mapper, t);
	}

	@Override
	public int delete(Source2LoginLog_ t) {
		return supportDelete(mapper, t);
	}

	@Override
	public int count(Source2LoginLog_ t) {
		return supportCount(mapper, t);
	}

//	@Override
//	public void loadDetail(LoginLog_ loginlog, Detail_ detail) {
//		detail.setLoginLog(loginlog);
//		loginlog.setDetail(detailService.selectAll(detail));
//	}
}