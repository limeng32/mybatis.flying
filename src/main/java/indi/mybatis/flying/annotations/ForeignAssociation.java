package indi.mybatis.flying.annotations;

import indi.mybatis.flying.statics.AssociationCondition;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 * @description Describes the association between this table and related table,
 *              especially when there are other constraints other than foreign
 *              key.
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
	AssociationCondition condition() default AssociationCondition.Equal;
}
