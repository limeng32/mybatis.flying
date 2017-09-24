package indi.mybatis.flying.models;

import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.statics.ConditionType;

/**
 * 条件映射类，用于描述被ConditionMapperAnnotation标注过的对象字段和sql之间的对应关系
 */
public class ConditionMapper implements Mapperable {

	/**
	 * Java对象字段名
	 */
	private String fieldName;

	/**
	 * 数据库表字段名
	 */
	private String dbFieldName;

	/**
	 * 数据库字段对应的jdbc类型
	 */
	private JdbcType jdbcType;

	/**
	 * 条件类型
	 */
	private ConditionType conditionType;

	/**
	 * 如果是外键，对应数据库其他表的主键字段的名称。不是外键时为null。
	 */
	private String dbAssociationUniqueKey;

	/**
	 * 此变量是否对应数据库表的外键。默认为否。
	 */
	private boolean isForeignKey;

	/**
	 * 如果此变量对应数据库表的外键，ForeignFieldName表示相关表的主键的Java对象字段名。不是外键时为null。
	 */
	private String foreignFieldName;

	/**
	 * 此变量的ignoreTag的set，默认为空。
	 */
	private ConcurrentSkipListSet<String> ignoreTagSet;

	/**
	 * 此变量的指定typeHandler的访问路径，默认为null。
	 */
	private String typeHandlerPath;

	@Override
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public String getDbFieldName() {
		return dbFieldName;
	}

	public void setDbFieldName(String dbFieldName) {
		this.dbFieldName = dbFieldName;
	}

	@Override
	public JdbcType getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(JdbcType jdbcType) {
		this.jdbcType = jdbcType;
	}

	@Override
	public String getDbAssociationUniqueKey() {
		return dbAssociationUniqueKey;
	}

	public void setDbAssociationUniqueKey(String dbAssociationUniqueKey) {
		this.dbAssociationUniqueKey = dbAssociationUniqueKey;
	}

	@Override
	public boolean isForeignKey() {
		return isForeignKey;
	}

	public void setForeignKey(boolean isForeignKey) {
		this.isForeignKey = isForeignKey;
	}

	@Override
	public String getForeignFieldName() {
		return foreignFieldName;
	}

	public void setForeignFieldName(String foreignFieldName) {
		this.foreignFieldName = foreignFieldName;
	}

	public ConditionType getConditionType() {
		return conditionType;
	}

	public void setConditionType(ConditionType conditionType) {
		this.conditionType = conditionType;
	}

	@Override
	public ConcurrentSkipListSet<String> getIgnoreTagSet() {
		if (ignoreTagSet == null) {
			ignoreTagSet = new ConcurrentSkipListSet<>();
		}
		return ignoreTagSet;
	}

	public void setIgnoreTagSet(ConcurrentSkipListSet<String> ignoreTagSet) {
		this.ignoreTagSet = ignoreTagSet;
	}

	public String getTypeHandlerPath() {
		return typeHandlerPath;
	}

	public void setTypeHandlerPath(Class<?> typeHandler) {
		if (!Object.class.equals(typeHandler)) {
			System.out.println(":::" + typeHandler.getName());
			this.typeHandlerPath = typeHandler.getName();
		}
	}
}