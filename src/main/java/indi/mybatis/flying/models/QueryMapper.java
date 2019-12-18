package indi.mybatis.flying.models;

import java.util.Map;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 * @description Describes the database mapping information for PojoCondition
 *              objects.
 */
public class QueryMapper {

	private Map<String, ConditionMapper> conditionMapperCache;

	private Map<String, OrMapper> OrMapperCache;

	public Map<String, ConditionMapper> getConditionMapperCache() {
		return conditionMapperCache;
	}

	public void setConditionMapperCache(Map<String, ConditionMapper> conditionMapperCache) {
		this.conditionMapperCache = conditionMapperCache;
	}

	public Map<String, OrMapper> getOrMapperCache() {
		return OrMapperCache;
	}

	public void setOrMapperCache(Map<String, OrMapper> orMapperCache) {
		OrMapperCache = orMapperCache;
	}

}
