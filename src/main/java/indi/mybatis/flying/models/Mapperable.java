package indi.mybatis.flying.models;

import java.util.Set;

import org.apache.ibatis.type.JdbcType;

public interface Mapperable {

	String getFieldName();

	String getDbFieldName();

	boolean isForeignKey();

	String getForeignFieldName();

	JdbcType getJdbcType();

	String getDbAssociationUniqueKey();

	ForeignAssociationMapper[] getForeignAssociationMappers();

	Set<String> getIgnoreTagSet();

	public String getTypeHandlerPath();

	Class<?> getFieldType();

	Class<?> getSubTarget();

	String getDbCrossedAssociationUniqueKey();

	public boolean isCrossDbForeignKey();
}