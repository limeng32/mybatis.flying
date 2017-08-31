package indi.mybatis.flying.models;

import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.ibatis.type.JdbcType;

/**
 * 字段映射类，用于描述java对象字段和数据库表字段之间的对应关系
 */
public class FieldMapper implements Mapperable {
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
	 * 如果是外键，对应数据库其他表的主键字段的名称。不是外键时为null。
	 */
	private String dbAssociationUniqueKey;

	/**
	 * 此变量是否对应数据库表的主键。默认为否。
	 */
	private boolean isUniqueKey;

	/**
	 * 此变量是否对应数据库表的外键。默认为否。
	 */
	private boolean isForeignKey;

	/**
	 * 如果此变量对应数据库表的外键，ForeignFieldName表示相关表的主键的Java对象字段名。不是外键时为null。
	 */
	private String foreignFieldName;

	/**
	 * 此变量是否为version型乐观锁。默认为否。
	 */
	private boolean isOpVersionLock;

	/**
	 * 此变量的ignoreTag的set，默认为空。
	 */
	private ConcurrentSkipListSet<String> ignoreTagSet;

	@Override
	public String getDbFieldName() {
		return dbFieldName;
	}

	public void setDbFieldName(String dbFieldName) {
		this.dbFieldName = dbFieldName;
	}

	@Override
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
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

	public boolean isUniqueKey() {
		return isUniqueKey;
	}

	public void setUniqueKey(boolean isUniqueKey) {
		this.isUniqueKey = isUniqueKey;
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

	public boolean isOpVersionLock() {
		return isOpVersionLock;
	}

	public void setOpVersionLock(boolean isOpVersionLock) {
		this.isOpVersionLock = isOpVersionLock;
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

}