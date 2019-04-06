package indi.mybatis.flying.models;

import indi.mybatis.flying.statics.AssociationType;

public class ForeignAssociationMapper {

	public ForeignAssociationMapper(String dbFieldName, String dbAssociationFieldName, AssociationType condition) {
		this.dbFieldName = dbFieldName;
		this.dbAssociationFieldName = dbAssociationFieldName;
		this.condition = condition;
	}

	private String dbFieldName;

	private String dbAssociationFieldName;

	private AssociationType condition;

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

	public AssociationType getCondition() {
		return condition;
	}

	public void setCondition(AssociationType condition) {
		this.condition = condition;
	}

}
