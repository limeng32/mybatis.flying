package indi.mybatis.flying.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import indi.mybatis.flying.mapper.PermissionMapper;
import indi.mybatis.flying.pojo.Permission;

@Service
public class PermissionService implements PermissionMapper {

	@Autowired
	private PermissionMapper mapper;

	@Override
	public Permission select(Object id) {
		return mapper.select(id);
	}

}
