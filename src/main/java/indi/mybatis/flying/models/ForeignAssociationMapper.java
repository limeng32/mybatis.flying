package indi.mybatis.flying.models;

import indi.mybatis.flying.statics.AssociationCondition;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public class ForeignAssociationMapper {

	public ForeignAssociationMapper(String dbFieldName, String dbAssociationFieldName, AssociationCondition condition) {
		this.dbFieldName = dbFieldName;
		this.dbAssociationFieldName = dbAssociationFieldName;
		this.condition = condition;
	}

	private String dbFieldName;

	private String dbAssociationFieldName;

	private AssociationCondition condition;

	public String getDbFieldName() {
		return dbFieldName;
	}

	public void setDbFieldName(String dbFieldName) {
		this.dbFieldName = dbFieldName;
	}

	public String getDbAssociationFieldName() {
		return dbAssociationFieldName;
	}

	public void setDbAssociationFieldName(String dbAssociationFieldName) {
		this.dbAssociationFieldName = dbAssociationFieldName;
	}

	public AssociationCondition getCondition() {
		return condition;
	}

	public void setCondition(AssociationCondition condition) {
		this.condition = condition;
	}

}
