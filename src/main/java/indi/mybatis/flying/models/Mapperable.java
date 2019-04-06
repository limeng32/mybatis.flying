package indi.mybatis.flying.models;

import java.util.Set;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.statics.AssociationType;

public interface Mapperable {

	String getFieldName();

	String getDbFieldName();

	boolean isForeignKey();

	String getForeignFieldName();

	JdbcType getJdbcType();

	String getDbAssociationUniqueKey();

	ForeignAssociationMapper[] getForeignAssociationMappers();

	Set<String> getIgnoreTagSet();

	String getTypeHandlerPath();

	Class<?> getFieldType();

	Class<?> getSubTarget();

	String getDbCrossedAssociationUniqueKey();

	boolean isCrossDbForeignKey();

	AssociationType getAssociationType();
}