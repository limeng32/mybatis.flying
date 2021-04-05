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
import indi.mybatis.flying.interceptors.AutoMapperInterceptor;
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
	 * The access path of the custom typeHandler for this variable, the default is
	 * null.
	 */
	private String typeHandlerPath;

	private OpLockType opLockType = OpLockType.NULL;

	private String[] ignoreTag = {};

	private String[] whiteListTag = {};

	/**
	 * IgnoreTag's set for this variable, the default is empty.
	 */
	private Set<String> ignoreTagSet = new HashSet<>();

	/**
	 * WhiteListTag's set for this variable, the default is empty.
	 */
	private Set<String> whiteListTagSet = new HashSet<>();

	private FieldMapperAnnotation fieldMapperAnnotation;

	private Column column;

	private Id id;

	private boolean insertAble = true;

	private boolean updateAble = true;

	private Class<?> fieldType;

	private Class<?> subTarget;

	private boolean isDelegate = false;

	/**
	 * How the tables are related (e.g. left join or right join)
	 */
	private AssociationType associationType;

	private FieldMapper delegate;

	private boolean hasDelegate = false;

	private String[] cryptKeyColumn;

	private FieldMapper[] cryptKeyField;

	private String dbFieldNameForJoin;

	private boolean onlyForJoin = false;

	private String cryptKeyAddition;

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
			setWhiteListTag(fieldMapperAnnotation.whiteListTag());
			setDbAssociationUniqueKey(fieldMapperAnnotation.dbAssociationUniqueKey());
			setAssociationType(fieldMapperAnnotation.associationType());
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
			// is in delegate mode or not
			setDelegate(fieldMapperAnnotation.delegate());
			if (fieldMapperAnnotation.cryptKeyColumn().length != 0) {
				setCryptKeyColumn(fieldMapperAnnotation.cryptKeyColumn());
			}
			if (!CryptKeyAdditional.class.equals(fieldMapperAnnotation.cryptKeyAdditional())) {
				try {
					CryptKeyAdditional c = fieldMapperAnnotation.cryptKeyAdditional().newInstance();
					if (c != null && c.getCryptKeyAddition() != null && !"".equals(c.getCryptKeyAddition())) {
						// 此处要将cryptKeyAddition封装为在sql中安全的形式
						String temp = c.getCryptKeyAddition().replaceAll("'", "''");
						setCryptKeyAddition("'" + temp + "'");
					} else {
						setCryptKeyAddition(null);
					}
				} catch (InstantiationException | IllegalAccessException e) {
					setCryptKeyAddition(null);
				}
			}
			if (!"".equals(fieldMapperAnnotation.dbFieldNameForJoinOnly())) {
				onlyForJoin = true;
				setDbFieldNameForJoin(fieldMapperAnnotation.dbFieldNameForJoinOnly());
			} else {
				setDbFieldNameForJoin(fieldMapperAnnotation.dbFieldName());
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
		boolean b = false;
		for (Annotation an1 : fieldAnnotations) {
			if (an1 instanceof Id) {
				setId((Id) an1);
			}
			if ((an1 instanceof FieldMapperAnnotation) || (an1 instanceof Column)) {
				b = true;
				setField(field);
				if (an1 instanceof FieldMapperAnnotation) {
					setFieldMapperAnnotation((FieldMapperAnnotation) an1);
				} else if (an1 instanceof Column) {
					setColumn((Column) an1);
				}
			}
		}
		if (b) {
			buildMapper();
		}
		return b;
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

	public String[] getWhiteListTag() {
		return whiteListTag;
	}

	public void setWhiteListTag(String[] whiteListTag) {
		this.whiteListTag = whiteListTag;
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

	public AssociationType getAssociationType() {
		return associationType;
	}

	public void setAssociationType(AssociationType associationType) {
		this.associationType = associationType;
	}

	public boolean isDelegate() {
		return isDelegate;
	}

	public void setDelegate(boolean isDelegate) {
		this.isDelegate = isDelegate;
	}

	public FieldMapper getDelegate() {
		return delegate;
	}

	public void setDelegate(FieldMapper delegate) {
		this.delegate = delegate;
	}

	public boolean isHasDelegate() {
		return hasDelegate;
	}

	public void setHasDelegate(boolean hasDelegate) {
		this.hasDelegate = hasDelegate;
	}

	public String[] getCryptKeyColumn() {
		return cryptKeyColumn;
	}

	public void setCryptKeyColumn(String[] cryptKeyColumn) {
		this.cryptKeyColumn = cryptKeyColumn;
	}

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

	public String getDbFieldNameForJoin() {
		return dbFieldNameForJoin;
	}

	public void setDbFieldNameForJoin(String dbFieldNameForJoin) {
		this.dbFieldNameForJoin = dbFieldNameForJoin;
	}

	public boolean isOnlyForJoin() {
		return onlyForJoin;
	}

	public void setOnlyForJoin(boolean onlyForJoin) {
		this.onlyForJoin = onlyForJoin;
	}

	public String getCryptKeyAddition() {
		return cryptKeyAddition;
	}

	public void setCryptKeyAddition(String cryptKeyAddition) {
		this.cryptKeyAddition = cryptKeyAddition;
	}

}