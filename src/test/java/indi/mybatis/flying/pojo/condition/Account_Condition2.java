package indi.mybatis.flying.pojo.condition;

import indi.mybatis.flying.annotations.ConditionMapperAnnotation;
import indi.mybatis.flying.annotations.Or;
import indi.mybatis.flying.annotations.QueryMapperAnnotation;
import indi.mybatis.flying.statics.ConditionType;

@QueryMapperAnnotation(tableName = "Account_")
public class Account_Condition2 extends Account_Condition {

	private static final long serialVersionUID = 1L;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.Equal),
			@ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.Equal) })
	private Object[] nameEqualsOr;

	public Object[] getNameEqualsOr() {
		return nameEqualsOr;
	}

	public void setNameEqualsOr(Object... nameEqualsOr) {
		this.nameEqualsOr = nameEqualsOr;
	}
}
