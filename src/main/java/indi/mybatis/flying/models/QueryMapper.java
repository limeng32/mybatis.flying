package indi.mybatis.flying.models;

import java.util.Map;

/**
 * 描述PojoCondition对象的数据库映射信息
 */
public class QueryMapper {

	private Map<String, ConditionMapper> conditionMapperCache;

	public Map<String, ConditionMapper> getConditionMapperCache() {
		return conditionMapperCache;
	}

	public void setConditionMapperCache(Map<String, ConditionMapper> conditionMapperCache) {
		this.conditionMapperCache = conditionMapperCache;
	}

}
