package indi.mybatis.flying.models;

import java.util.WeakHashMap;

public class TableName {

	public TableName(TableMapper tableMapper, int index, WeakHashMap<Class<?>, TableName> map) {
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

	private WeakHashMap<Class<?>, TableName> map;

	public WeakHashMap<Class<?>, TableName> getMap() {
		return map;
	}

	public void setMap(WeakHashMap<Class<?>, TableName> map) {
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

	public StringBuffer sqlSelect() {
		return new StringBuffer(tableMapper.getTableName()).append(" as ").append(tableMapper.getTableName())
				.append("_").append(index);
	}

	public StringBuffer sqlWhere() {
		return new StringBuffer(tableMapper.getTableName()).append("_").append(index).append(".");
	}
}
