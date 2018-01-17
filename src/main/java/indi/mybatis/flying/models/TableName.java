package indi.mybatis.flying.models;

import java.util.HashMap;

public class TableName {

	public TableName(TableMapper tableMapper, int index, HashMap<Class<?>, TableName> map) {
		this.tableMapper = tableMapper;
		this.index = index;
		if (map == null) {
			map = new HashMap<Class<?>, TableName>();
		}
		this.map = map;
		this.map.put(this.tableMapper.getClazz(), this);
	}

	private TableMapper tableMapper;

	private int index = 0;

	private HashMap<Class<?>, TableName> map;

	public HashMap<Class<?>, TableName> getMap() {
		return map;
	}

	public void setMap(HashMap<Class<?>, TableName> map) {
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
