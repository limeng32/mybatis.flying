package indi.mybatis.flying.mapper;

import java.util.List;

import indi.mybatis.flying.pojo.Permission;

public interface PermissionMapper {

	public Permission select(Object id);

	public Permission selectSimple2(Object id);

	public int insert(Permission t);

	public int insertBatch(List<Permission> l);

	public int update(Permission t);

	public int updatePersistent(Permission t);

	public int updateBatch(List<Permission> l);
	
	public int insertAes(Permission t);
	
	public Permission selectAes(Permission t);
}
