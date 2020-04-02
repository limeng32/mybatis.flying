package indi.mybatis.flying.mapper;

import indi.mybatis.flying.pojo.Permission;

public interface PermissionMapper {

	public Permission select(Object id);

	public Permission selectSimple2(Object id);

}
