package indi.mybatis.flying.models;

/**
 * Or conditional group mapping class,Used to describe the corresponding
 * relationship between the object field and ConditionMapper that is marked by
 * Or.
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
