package indi.mybatis.flying.models;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.annotations.FieldMapperAnnotation;
import indi.mybatis.flying.annotations.ForeignAssociation;
import indi.mybatis.flying.exception.BuildSqlException;
import indi.mybatis.flying.exception.BuildSqlExceptionEnum;
import indi.mybatis.flying.statics.AssociationType;
import indi.mybatis.flying.statics.OpLockType;
import indi.mybatis.flying.utils.JdbcTypeEnum;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8。
 * @description Field mapping class that describes the correspondence between a
 *              Java object field and a database table field.
 */
public class FieldMapper implements Mapperable {

	private Field field;

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
	 * If it is a foreign key, it corresponds to the name of the primary key field
	 * of the other table in the database. Blank means it's not foreign key.
	 */
	private String dbAssociationUniqueKey = "";

	/**
	 * Describes the association between this table and related table, especially
	 * when there are other constraints other than foreign key.
	 */
	private ForeignAssociationMapper[] foreignAssociationMappers;

	/**
	 * If it is a cross-source foreign key, it corresponds to the name of the
	 * primary key field for the table of the other database. Blank means it's not
	 * foreign key.
	 */
	private String dbCrossedAssociationUniqueKey = "";

	/**
	 * This variable corresponds to the primary key of the database table. The
	 * default is false.
	 */
	private boolean isUniqueKey;

	/**
	 * This variable corresponds to the foreign key of the database table. The
	 * default is false.
	 */
	private boolean isForeignKey;

	/**
	 * This variable corresponds to the foreign key of the cross-source database
	 * table. The default is false.
	 */
	private boolean isCrossDbForeignKey;

	/**
	 * If this variable corresponds to the foreign key of the database table, the
	 * ForeignFieldName represents the Java object field name of the related table's
	 * primary key.Blank means it's not foreign key.
	 */
	private String foreignFieldName;

	/**
	 * Whether this variable is a version optimistic lock, the default is false.
	 */
	private boolean isOpVersionLock;

	/**
	 * IgnoreTag's set for this variable, the default is empty.
	 */
	private Set<String> ignoreTagSet;

	/**
	 * The access path of the custom typeHandler for this variable, the default is
	 * null.
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

	private Class<?> fieldType;

	private Class<?> subTarget;

	/**
	 * How the tables are related (e.g. left join or right join)
	 */
	private AssociationType associationType;

	public void buildMapper() {
		if (fieldMapperAnnotation == null && column == null) {
			throw new BuildSqlException(
					BuildSqlExceptionEnum.NO_FIELD_MAPPER_ANNOTATION_OR_COLUMN_ANNOTATION.toString());
		}
		setFieldName(field.getName());
		setFieldType(field.getType());
		/*
		 * The Column annotation has the lowest priority, so it is written at the top.
		 */
		if (column != null) {
			setDbFieldName(getColumnName(column, field));
			setJdbcType(getColumnType(column, field));
			setInsertAble(column.insertable());
			setUpdateAble(column.updatable());
		}
		if (fieldMapperAnnotation != null) {
			setDbFieldName(fieldMapperAnnotation.dbFieldName());
			setJdbcType(fieldMapperAnnotation.jdbcType());
			setTypeHandlerPath(fieldMapperAnnotation.customTypeHandler());
			setOpLockType(fieldMapperAnnotation.opLockType());
			setUniqueKey(fieldMapperAnnotation.isUniqueKey());
			setIgnoreTag(fieldMapperAnnotation.ignoreTag());
			setDbAssociationUniqueKey(fieldMapperAnnotation.dbAssociationUniqueKey());
			setAssociationType(fieldMapperAnnotation.associationType());
			setDbCrossedAssociationUniqueKey(fieldMapperAnnotation.dbCrossedAssociationUniqueKey());
			if (fieldMapperAnnotation.associationExtra().length > 0) {
				ForeignAssociation[] fas = fieldMapperAnnotation.associationExtra();
				ForeignAssociationMapper[] fams = new ForeignAssociationMapper[fieldMapperAnnotation
						.associationExtra().length];
				int i = 0;
				for (ForeignAssociation fa : fas) {
					fams[i] = new ForeignAssociationMapper(fa.dbFieldName(), fa.dbAssociationFieldName(),
							fa.condition());
					i++;
				}
				setForeignAssociationMappers(fams);
			}
		}
		/* The Id has the highest priority, so it's written at the end. */
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
			String columnDefinition = column.columnDefinition().indexOf(" ") > -1
					? column.columnDefinition().substring(0, column.columnDefinition().indexOf(" "))
					: column.columnDefinition();
			JdbcType jdbcType = JdbcTypeEnum.forName(columnDefinition);
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

	@Override
	public ForeignAssociationMapper[] getForeignAssociationMappers() {
		return foreignAssociationMappers;
	}

	public void setForeignAssociationMappers(ForeignAssociationMapper[] foreignAssociationMappers) {
		this.foreignAssociationMappers = foreignAssociationMappers;
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
	public Set<String> getIgnoreTagSet() {
		if (ignoreTagSet == null) {
			ignoreTagSet = new HashSet<>();
		}
		return ignoreTagSet;
	}

	public void setIgnoreTagSet(Set<String> ignoreTagSet) {
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

	public AssociationType getAssociationType() {
		return associationType;
	}

	public void setAssociationType(AssociationType associationType) {
		this.associationType = associationType;
	}

}