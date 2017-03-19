package indi.mybatis.flying.models;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 描述java对象的数据库映射信息（数据库表的映射、字段的映射）
 */
public class TableMapper {

	private Annotation tableMapperAnnotation;

	private Map<String, FieldMapper> fieldMapperCache;

	private FieldMapper[] uniqueKeyNames;

	private FieldMapper[] opVersionLocks;

	public Annotation getTableMapperAnnotation() {
		return tableMapperAnnotation;
	}

	public void setTableMapperAnnotation(Annotation tableMapperAnnotation) {
		this.tableMapperAnnotation = tableMapperAnnotation;
	}

	public Map<String, FieldMapper> getFieldMapperCache() {
		return fieldMapperCache;
	}

	public void setFieldMapperCache(Map<String, FieldMapper> fieldMapperCache) {
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

}
