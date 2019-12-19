package indi.mybatis.flying.models;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public class TableName {

	public TableName(TableMapper tableMapper, int index, Map<Class<?>, TableName> map) {
		this.tableMapper = tableMapper;
		this.index = index;
		if (map == null) {
			map = new WeakHashMap<Class<?>, TableName>(1);
		}
		this.map = map;
		this.map.put(this.tableMapper.getClazz(), this);
	}

	private TableMapper tableMapper;

	private int index = 0;

	private Map<Class<?>, TableName> map;

	public Map<Class<?>, TableName> getMap() {
		return map;
	}

	public void setMap(Map<Class<?>, TableName> map) {
		this.map = map;
	}

	public TableMapper getTableMapper() {
		return tableMapper;
	}

	public void setTableMapper(TableMapper tableMapper) {
		this.tableMapper = tableMapper;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public StringBuilder sqlSelect() {
		return new StringBuilder(tableMapper.getTableName()).append(" as ").append(tableMapper.getTableName())
				.append("_").append(index);
	}

	public StringBuilder sqlWhere() {
		return new StringBuilder(tableMapper.getTableName()).append("_").append(index).append(".");
	}
}
