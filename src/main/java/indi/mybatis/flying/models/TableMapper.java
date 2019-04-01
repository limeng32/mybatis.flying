package indi.mybatis.flying.models;

import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Table;

import indi.mybatis.flying.annotations.TableMapperAnnotation;

/**
 * Description of database mapping information for Java objects (mapping of
 * database tables, mapping of fields)
 */
public class TableMapper {

	private TableMapperAnnotation tableMapperAnnotation;

	private Table table;

	private ConcurrentHashMap<String, FieldMapper> fieldMapperCache;

	private FieldMapper[] uniqueKeyNames;

	private FieldMapper[] opVersionLocks;

	private Class<?> clazz;

	private String tableName;

	public TableMapperAnnotation getTableMapperAnnotation() {
		return tableMapperAnnotation;
	}

	public void setTableMapperAnnotation(TableMapperAnnotation tableMapperAnnotation) {
		this.tableMapperAnnotation = tableMapperAnnotation;
	}

	public ConcurrentHashMap<String, FieldMapper> getFieldMapperCache() {
		return fieldMapperCache;
	}

	public void setFieldMapperCache(ConcurrentHashMap<String, FieldMapper> fieldMapperCache) {
		this.fieldMapperCache = fieldMapperCache;
	}

	public FieldMapper[] getUniqueKeyNames() {
		return uniqueKeyNames;
	}

	public void setUniqueKeyNames(FieldMapper[] uniqueKeyNames) {
		this.uniqueKeyNames = uniqueKeyNames;
	}

	public FieldMapper[] getOpVersionLocks() {
		return opVersionLocks;
	}

	public void setOpVersionLocks(FieldMapper[] opVersionLocks) {
		this.opVersionLocks = opVersionLocks;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public void buildTableName() {
		if (tableMapperAnnotation != null) {
			tableName = tableMapperAnnotation.tableName();
		} else if (table != null) {
			if ("".equals(table.name())) {
				tableName = clazz.getSimpleName();
			} else {
				tableName = table.name();
			}
		}
	}

	public String getTableName() {
		return tableName;
	}
}
