package indi.mybatis.flying.models;

public class TableName {

	public TableName(String tableName, int index) {
		this.tableName = tableName;
		this.index = index;
	}

	private String tableName;

	private int index = 0;

	public StringBuffer sqlSelect() {
		return new StringBuffer(tableName).append(" as ").append(tableName).append("_").append(index);
	}

	public StringBuffer sqlWhere() {
		return new StringBuffer(tableName).append("_").append(index).append(".");
	}
}
