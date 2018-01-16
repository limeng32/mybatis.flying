package indi.mybatis.flying.models;

/**
 * 或条件组映射类，用于描述被Or标注过的对象字段和ConditionMapper之间的对应关系
 */
public class OrMapper {

	private String fieldName;

	private ConditionMapper[] conditionMappers;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public ConditionMapper[] getConditionMappers() {
		return conditionMappers;
	}

	public void setConditionMappers(ConditionMapper[] conditionMappers) {
		this.conditionMappers = conditionMappers;
	}

}
