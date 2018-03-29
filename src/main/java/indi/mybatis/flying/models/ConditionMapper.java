package indi.mybatis.flying.models;

import java.util.HashSet;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.statics.ConditionType;

/**
 * Conditions of mapping class, is used to describe ConditionMapperAnnotation
 * annotation object field and corresponding relation between the SQL
 */
public class ConditionMapper implements Mapperable {

	/**
	 * Java object field name.
	 */
	private String fieldName;

	/**
	 * Database table field name.
	 */
	private String dbFieldName;

	/**
	 * The JDBC type corresponding to the database field.
	 */
	private JdbcType jdbcType;

	/**
	 * Conditions type
	 */
	private ConditionType conditionType;

	/**
	 * If it is a foreign key, it corresponds to the name of the primary key
	 * field of the other table in the database. Blank means it's not a foreign
	 * key.
	 */
	private String dbAssociationUniqueKey = "";

	/**
	 * If it is a cross-source foreign key, the name of the primary key field
	 * for the table of the other database.Blank means it's not a foreign key.
	 */
	private String dbCrossedAssociationUniqueKey = "";

	/**
	 * This variable corresponds to the foreign key of the database table, the
	 * default value is no.
	 */
	private boolean isForeignKey;

	/**
	 * If this variable corresponds to the foreign key of the database table,
	 * the ForeignFieldName represents the Java object field name of the related
	 * table's primary key, it is null when not foreign key.
	 */
	private String foreignFieldName;

	/**
	 * The ignoreTag set for this variable,
	 */
	private HashSet<String> ignoreTagSet;

	/**
	 * This variable specifies the access path for the custom typeHandler, the
	 * default is null.
	 */
	private String typeHandlerPath;

	/**
	 * Field type
	 */
	private Class<?> fieldType;

	/**
	 * Identify the condition for which (business) child objects, by default
	 * Void, are for themselves. This property works only in Or annotation
	 */
	private Class<?> subTarget;

	private boolean isCrossDbForeignKey;

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
	public HashSet<String> getIgnoreTagSet() {
		if (ignoreTagSet == null) {
			ignoreTagSet = new HashSet<>();
		}
		return ignoreTagSet;
	}

	public void setIgnoreTagSet(HashSet<String> ignoreTagSet) {
		this.ignoreTagSet = ignoreTagSet;
	}

	@Override
	public String getTypeHandlerPath() {
		return typeHandlerPath;
	}

	public void setTypeHandlerPath(Class<?> typeHandler) {
		if (!Void.class.equals(typeHandler)) {
			this.typeHandlerPath = typeHandler.getName();
		}
	}

	@Override
	public Class<?> getFieldType() {
		return fieldType;
	}

	public void setFieldType(Class<?> fieldType) {
		this.fieldType = fieldType;
	}

	@Override
	public Class<?> getSubTarget() {
		return subTarget;
	}

	public void setSubTarget(Class<?> subTarget) {
		this.subTarget = subTarget;
	}

	@Override
	public String getDbCrossedAssociationUniqueKey() {
		return dbCrossedAssociationUniqueKey;
	}

	public void setDbCrossedAssociationUniqueKey(String dbCrossedAssociationUniqueKey) {
		this.dbCrossedAssociationUniqueKey = dbCrossedAssociationUniqueKey;
	}

	@Override
	public boolean isCrossDbForeignKey() {
		return isCrossDbForeignKey;
	}

	public void setCrossDbForeignKey(boolean isCrossDbForeignKey) {
		this.isCrossDbForeignKey = isCrossDbForeignKey;
	}
}