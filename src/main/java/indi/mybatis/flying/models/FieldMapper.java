package indi.mybatis.flying.models;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Id;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.exception.BuildSqlException;
import indi.mybatis.flying.exception.BuildSqlExceptionEnum;
import indi.mybatis.flying.statics.OpLockType;
import indi.mybatis.flying.utils.JdbcTypeEnum;
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
	private HashSet<String> ignoreTagSet;

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

	private Id id;

	private boolean insertAble = true;

	private boolean updateAble = true;

	public void buildMapper() {
		if (fieldMapperAnnotation == null && column == null) {
			throw new BuildSqlException(BuildSqlExceptionEnum.noFieldMapperAnnotationOrColumnAnnotation.toString());
		}
		setFieldName(field.getName());
		/* Column标注的优先级最低，所以写在最前 */
		if (column != null) {
			setDbFieldName(getColumnName(column, field));
			setJdbcType(getColumnType(column, field));
			setInsertAble(column.insertable());
			setUpdateAble(column.updatable());
		}
		if (fieldMapperAnnotation != null) {
			setDbFieldName(fieldMapperAnnotation.dbFieldName());
			setJdbcType(fieldMapperAnnotation.jdbcType());
			setTypeHandlerPath(fieldMapperAnnotation.dbAssociationTypeHandler());
			setOpLockType(fieldMapperAnnotation.opLockType());
			setUniqueKey(fieldMapperAnnotation.isUniqueKey());
			setIgnoreTag(fieldMapperAnnotation.ignoreTag());
			setDbAssociationUniqueKey(fieldMapperAnnotation.dbAssociationUniqueKey());
		}
		/* Id标注的优先级最高，所以写在最后 */
		if (id != null) {
			setUniqueKey(true);
		}
	}

	public boolean buildMapper(Field field) {
		Annotation[] fieldAnnotations = field.getDeclaredAnnotations();
		if (fieldAnnotations.length == 0) {
			return false;
		}
		for (Annotation an1 : fieldAnnotations) {
			if (an1 instanceof Id) {
				setId((Id) an1);
			}
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

	public static JdbcType getColumnType(Column column, Field field) {
		if (!"".equals(column.columnDefinition().trim())) {
			String columnDefinition_ = column.columnDefinition().indexOf(" ") > -1
					? column.columnDefinition().substring(0, column.columnDefinition().indexOf(" "))
					: column.columnDefinition();
			JdbcType jdbcType = JdbcTypeEnum.forName(columnDefinition_);
			if (jdbcType != null) {
				return jdbcType;
			}
		}
		if (TypeJdbcTypeConverter.map.get(field.getType()) != null) {
			return TypeJdbcTypeConverter.map.get(field.getType());
		} else {
			return JdbcType.OTHER;
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
	public HashSet<String> getIgnoreTagSet() {
		if (ignoreTagSet == null) {
			ignoreTagSet = new HashSet<>();
		}
		return ignoreTagSet;
	}

	public void setIgnoreTagSet(HashSet<String> ignoreTagSet) {
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

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
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

	public boolean isInsertAble() {
		return insertAble;
	}

	public void setInsertAble(boolean insertAble) {
		this.insertAble = insertAble;
	}

	public boolean isUpdateAble() {
		return updateAble;
	}

	public void setUpdateAble(boolean updateAble) {
		this.updateAble = updateAble;
	}

}