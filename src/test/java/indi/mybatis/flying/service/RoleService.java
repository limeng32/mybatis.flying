package indi.mybatis.flying.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indi.mybatis.flying.mapper.RoleMapper;
import indi.mybatis.flying.pojo.Role_;
import indi.mybatis.flying.pojoHelper.ServiceSupport;

@Service
public class RoleService extends ServiceSupport<Role_> implements RoleMapper {

	@Autowired
	private RoleMapper mapper;

	@Override
	public Role_ select(Object id) {
		return supportSelect(mapper, id);
	}

	@Override
	public Role_ selectOne(Role_ t) {
		return supportSelectOne(mapper, t);
	}

	@Override
	public void insert(Role_ t) {
		supportInsert(mapper, t);
	}

	@Override
	public int update(Role_ t) {
		return supportUpdate(mapper, t);
	}

	@Override
	public Collection<Role_> selectAll(Role_ t) {
		return supportSelectAll(mapper, t);
	}

	@Override
	public int updatePersistent(Role_ t) {
		return supportUpdatePersistent(mapper, t);
	}

	@Override
	public int delete(Role_ t) {
		return supportDelete(mapper, t);
	}

	@Override
	public int count(Role_ t) {
		return supportCount(mapper, t);
	}

}