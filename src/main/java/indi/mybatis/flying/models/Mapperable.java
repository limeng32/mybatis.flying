package indi.mybatis.flying.models;

import org.apache.ibatis.type.JdbcType;

public interface Mapperable {

	String getFieldName();

	String getDbFieldName();

	boolean isForeignKey();

	String getForeignFieldName();

	JdbcType getJdbcType();

	String getDbAssociationUniqueKey();

	boolean isIgnoredSelect();
}
