package indi.mybatis.flying.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indi.mybatis.flying.mapper2.Role2Mapper;
import indi.mybatis.flying.pojo.Role2_;
import indi.mybatis.flying.pojoHelper.ServiceSupport;

@Service
public class Role2Service extends ServiceSupport<Role2_> implements Role2Mapper {

	@Autowired
	private Role2Mapper mapper;

	@Override
	public Role2_ select(Object id) {
		return supportSelect(mapper, id);
	}

	@Override
	public Role2_ selectOne(Role2_ t) {
		return supportSelectOne(mapper, t);
	}

	@Override
	public void insert(Role2_ t) {
		supportInsert(mapper, t);
	}

	@Override
	public int update(Role2_ t) {
		return supportUpdate(mapper, t);
	}

	@Override
	public Collection<Role2_> selectAll(Role2_ t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public int updatePersistent(Role2_ t) {
		return supportUpdatePersistent(mapper, t);
	}

	@Override
	public int delete(Role2_ t) {
		return supportDelete(mapper, t);
	}

	@Override
	public int count(Role2_ t) {
		return supportCount(mapper, t);
	}

}