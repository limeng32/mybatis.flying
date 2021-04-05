package indi.mybatis.flying.models;

import java.util.Set;

import org.apache.ibatis.type.JdbcType;

import indi.mybatis.flying.statics.AssociationType;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public interface Mapperable {

	String getFieldName();

	String getDbFieldName();

	boolean isForeignKey();

	String getForeignFieldName();

	JdbcType getJdbcType();

	String getDbAssociationUniqueKey();

	ForeignAssociationMapper[] getForeignAssociationMappers();

	Set<String> getIgnoreTagSet();

	Set<String> getWhiteListTagSet();

	String getTypeHandlerPath();

	Class<?> getFieldType();

	Class<?> getSubTarget();

	AssociationType getAssociationType();

	String[] getCryptKeyColumn();

	FieldMapper[] getCryptKeyField();

	String getDbFieldNameForJoin();

	String getCryptKeyAddition();
}