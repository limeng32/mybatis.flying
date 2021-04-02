package indi.mybatis.flying.models;

import java.util.HashSet;
import java.util.Set;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.interceptors.AutoMapperInterceptor;
import indi.mybatis.flying.statics.AssociationType;
import indi.mybatis.flying.statics.ConditionType;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 * @description Conditions of mapping class, is used to describe
 *              ConditionMapperAnnotation annotation object field and
 *              corresponding relation between the SQL
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
	 * If it is a foreign key, it corresponds to the name of the primary key field
	 * of the other table in the database. Blank means it's not a foreign key.
	 */
	private String dbAssociationUniqueKey = "";

	/**
	 * Describes the association between this table and related table, especially
	 * when there are other constraints other than foreign key.
	 */
	private ForeignAssociationMapper[] foreignAssociationMappers;

	/**
	 * This variable corresponds to the foreign key of the database table, the
	 * default value is no.
	 */
	private boolean isForeignKey;

	/**
	 * If this variable corresponds to the foreign key of the database table, the
	 * ForeignFieldName represents the Java object field name of the related table's
	 * primary key, it is null when not foreign key.
	 */
	private String foreignFieldName;

	/**
	 * IgnoreTag's set for this variable, the default is empty.
	 */
	private Set<String> ignoreTagSet = new HashSet<>();

	/**
	 * WhiteListTag's set for this variable, the default is empty.
	 */
	private Set<String> whiteListTagSet = new HashSet<>();

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
	 * Identify the condition for which (business) child objects, by default Void,
	 * are for themselves. This property works only in Or annotation
	 */
	private Class<?> subTarget;

	private AssociationType associationType;

	private FieldMapper[] cryptKeyField;

	private String[] cryptKeyColumn;
	
	private String dbFieldNameForJoin;

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
	public ForeignAssociationMapper[] getForeignAssociationMappers() {
		return foreignAssociationMappers;
	}

	public void setForeignAssociationMappers(ForeignAssociationMapper[] foreignAssociationMappers) {
		this.foreignAssociationMappers = foreignAssociationMappers;
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
	public Set<String> getIgnoreTagSet() {
		return ignoreTagSet;
	}

	public void setIgnoreTagSet(Set<String> ignoreTagSet) {
		this.ignoreTagSet = ignoreTagSet;
	}

	@Override
	public Set<String> getWhiteListTagSet() {
		return whiteListTagSet;
	}

	public void setWhiteListTagSet(Set<String> whiteListTagSet) {
		this.whiteListTagSet = whiteListTagSet;
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

	public AssociationType getAssociationType() {
		return associationType;
	}

	public void setAssociationType(AssociationType associationType) {
		this.associationType = associationType;
	}

	@Override
	public FieldMapper[] getCryptKeyField() {
		// 当前只有数据库为mysql时才认为cryptKeyField有值从而处理加密
		if (AutoMapperInterceptor.isMysql()) {
			return cryptKeyField;
		} else {
			return null;
		}
	}

	public void setCryptKeyField(FieldMapper[] cryptKeyField) {
		this.cryptKeyField = cryptKeyField;
	}

	@Override
	public String[] getCryptKeyColumn() {
		return cryptKeyColumn;
	}

	public void setCryptKeyColumn(String[] cryptKeyColumn) {
		this.cryptKeyColumn = cryptKeyColumn;
	}

	public String getDbFieldNameForJoin() {
		return dbFieldNameForJoin;
	}

	public void setDbFieldNameForJoin(String dbFieldNameForJoin) {
		this.dbFieldNameForJoin = dbFieldNameForJoin;
	}

}