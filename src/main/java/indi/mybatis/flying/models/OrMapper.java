package indi.mybatis.flying.models;

import java.lang.reflect.Field;

/**
 * 或条件组映射类，用于描述被Or标注过的对象字段和ConditionMapper之间的对应关系
 */
public class OrMapper {

	private Field field;

	private ConditionMapper[] conditionMappers;

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public ConditionMapper[] getConditionMappers() {
		return conditionMappers;
	}

	public void setConditionMappers(ConditionMapper[] conditionMappers) {
		this.conditionMappers = conditionMappers;
	}

}
