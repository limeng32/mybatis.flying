package indi.mybatis.flying.annotations;

import indi.mybatis.flying.statics.AssociationType;

/**
 * 
 * Describes the association between this table and related table, especially
 * when there are other constraints other than foreign key.
 * 
 * 描述本表和相关表之间的关联关系，多用于除外键外还有其它约束时
 * 
 */
public @interface ForeignAssociation {

	/**
	 * 
	 * The name of the field in this table
	 * 
	 * 本表字段名称
	 * 
	 */
	String dbFieldName();

	/**
	 * 
	 * The name of the field in associated table
	 * 
	 * 关联表字段名称
	 * 
	 */
	String dbAssociationFieldName();

	/**
	 * 
	 * The relationship between this table field and the associated table
	 * field，currently supports: Equal GreaterThan GreaterOrEqual LessThan
	 * LessOrEqual NotEqual
	 * 
	 * 本表字段和关联表字段间关系，当前支持：Equal GreaterThan GreaterOrEqual LessThan LessOrEqual
	 * NotEqual
	 * 
	 */
	AssociationType condition() default AssociationType.Equal;
}
