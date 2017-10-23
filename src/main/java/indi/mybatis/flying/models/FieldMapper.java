package indi.mybatis.flying.models;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.persistence.Column;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.exception.BuildSqlException;
import indi.mybatis.flying.exception.BuildSqlExceptionEnum;
import indi.mybatis.flying.statics.OpLockType;
import indi.mybatis.flying.utils.TypeJdbcTypeConverter;

/**
 * 字段映射类，用于描述java对象字段和数据库表字段之间的对应关系
 */
public class FieldMapper implements Mapperable {

	private Field field;

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
	private String dbAssociationUniqueKey = "";

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

	/**
	 * 此变量的指定typeHandler的访问路径，默认为null。
	 */
	private String typeHandlerPath;

	private String fmaDbFieldName;

	private String columnFieldName;

	private OpLockType opLockType = OpLockType.Null;

	private String[] ignoreTag = {};

	private FieldMapperAnnotation fieldMapperAnnotation;

	private Column column;

	public void buildMapper() {
		if (fieldMapperAnnotation == null && column == null) {
			throw new BuildSqlException(BuildSqlExceptionEnum.noFieldMapperAnnotationOrColumnAnnotation.toString());
		}
		setFieldName(field.getName());
		if (fieldMapperAnnotation != null) {
			setDbFieldName(fieldMapperAnnotation.dbFieldName());
			setJdbcType(fieldMapperAnnotation.jdbcType());
			setTypeHandlerPath(fieldMapperAnnotation.dbAssociationTypeHandler());
			setOpLockType(fieldMapperAnnotation.opLockType());
			setUniqueKey(fieldMapperAnnotation.isUniqueKey());
			setIgnoreTag(fieldMapperAnnotation.ignoreTag());
			setDbAssociationUniqueKey(fieldMapperAnnotation.dbAssociationUniqueKey());
		} else if (column != null) {
			setDbFieldName(getColumnName(column, field));
			setJdbcType(TypeJdbcTypeConverter.map.get(field.getType()));
		}
	}

	public boolean buildMapper(Field field) {
		Annotation[] fieldAnnotations = field.getDeclaredAnnotations();
		if (fieldAnnotations.length == 0) {
			return false;
		}
		for (Annotation an1 : fieldAnnotations) {
			if ((an1 instanceof FieldMapperAnnotation) || (an1 instanceof Column)) {
				setField(field);
				if (an1 instanceof FieldMapperAnnotation) {
					setFieldMapperAnnotation((FieldMapperAnnotation) an1);
				} else if (an1 instanceof Column) {
					setColumn((Column) an1);
				}
			}
		}
		buildMapper();
		return true;
	}

	public static String getColumnName(Column column, Field field) {
		if (!"".equals(column.name())) {
			return column.name();
		} else {
			return field.getName();
		}
	}

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

	public String getTypeHandlerPath() {
		return typeHandlerPath;
	}

	public void setTypeHandlerPath(Class<?> typeHandler) {
		if (!Void.class.equals(typeHandler)) {
			this.typeHandlerPath = typeHandler.getName();
		}
	}

	public String getFmaDbFieldName() {
		return fmaDbFieldName;
	}

	public void setFmaDbFieldName(String fmaDbFieldName) {
		this.fmaDbFieldName = fmaDbFieldName;
	}

	public String getColumnFieldName() {
		return columnFieldName;
	}

	public void setColumnFieldName(String columnFieldName) {
		this.columnFieldName = columnFieldName;
	}

	public FieldMapperAnnotation getFieldMapperAnnotation() {
		return fieldMapperAnnotation;
	}

	public void setFieldMapperAnnotation(FieldMapperAnnotation fieldMapperAnnotation) {
		this.fieldMapperAnnotation = fieldMapperAnnotation;
	}

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	public OpLockType getOpLockType() {
		return opLockType;
	}

	public void setOpLockType(OpLockType opLockType) {
		this.opLockType = opLockType;
	}

	public String[] getIgnoreTag() {
		return ignoreTag;
	}

	public void setIgnoreTag(String[] ignoreTag) {
		this.ignoreTag = ignoreTag;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

}