package indi.mybatis.flying.models;

import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.ibatis.type.JdbcType;

public interface Mapperable {

	String getFieldName();

	String getDbFieldName();

	boolean isForeignKey();

	String getForeignFieldName();

	JdbcType getJdbcType();

	String getDbAssociationUniqueKey();

	ConcurrentSkipListSet<String> getIgnoreTagSet();
}