package indi.mybatis.flying.service2;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indi.mybatis.flying.mapper2.Detail2Mapper;
import indi.mybatis.flying.pojo.Detail2_;
import indi.mybatis.flying.pojo.LoginLogSource2;
import indi.mybatis.flying.pojoHelper.ServiceSupport;

@Service
public class Detail2Service extends ServiceSupport<Detail2_> implements Detail2Mapper {

	@Autowired
	private Detail2Mapper mapper;

	@Override
	public Detail2_ select(Object id) {
		return supportSelect(mapper, id);
	}

	@Override
	public Detail2_ selectOne(Detail2_ t) {
		return supportSelectOne(mapper, t);
	}

	@Override
	public void insert(Detail2_ t) {
		supportInsert(mapper, t);
	}

	@Override
	public int update(Detail2_ t) {
		return supportUpdate(mapper, t);
	}

	@Override
	public Collection<Detail2_> selectAll(Detail2_ t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public int updatePersistent(Detail2_ t) {
		return supportUpdatePersistent(mapper, t);
	}

	@Override
	public int delete(Detail2_ t) {
		return supportDelete(mapper, t);
	}

	@Override
	public int count(Detail2_ t) {
		return supportCount(mapper, t);
	}

	@Override
	public void loadLoginLogSource2(LoginLogSource2 loginLogSource2, Detail2_ detail2) {
		loginLogSource2.removeAllDetail2();
		detail2.setLoginLogSource2(loginLogSource2);
		loginLogSource2.setDetail2(mapper.selectAll(detail2));
	}

	@Override
	public void insertWithoutName(Detail2_ t) {
		mapper.insertWithoutName(t);
	}

	@Override
	public void insertWithoutFoo(Detail2_ t) {
		mapper.insertWithoutFoo(t);
	}

	@Override
	public void insertBatchWithoutName(Collection<Detail2_> t) {
		mapper.insertBatchWithoutName(t);
	}

	@Override
	public int updateWithoutName(Detail2_ t) {
		return mapper.updateWithoutName(t);
	}

	@Override
	public int updatePersistentWithoutName(Detail2_ t) {
		return mapper.updatePersistentWithoutName(t);
	}

	@Override
	public Detail2_ selectWithoutCache(Object id) {
		return mapper.selectWithoutCache(id);
	}

}