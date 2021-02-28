package indi.mybatis.flying.pojo.condition;

import indi.mybatis.flying.annotations.ConditionMapperAnnotation;
import indi.mybatis.flying.handler.ByteArrayHandler;
import indi.mybatis.flying.models.Conditionable;
import indi.mybatis.flying.models.Limitable;
import indi.mybatis.flying.models.Sortable;
import indi.mybatis.flying.pojo.EmpScore;
import indi.mybatis.flying.statics.ConditionType;

public class EmpScoreCondition extends EmpScore implements Conditionable {

	private Limitable limiter;

	private Sortable sorter;

	@ConditionMapperAnnotation(dbFieldName = "secret2", conditionType = ConditionType.LIKE, customTypeHandler = ByteArrayHandler.class)
	private String secret2Like;

	@ConditionMapperAnnotation(dbFieldName = "secret2", conditionType = ConditionType.EQUAL, customTypeHandler = ByteArrayHandler.class)
	private String secret2Equal;

	@Override
	public Limitable getLimiter() {
		return limiter;
	}

	@Override
	public void setLimiter(Limitable limiter) {
		this.limiter = limiter;
	}

	@Override
	public Sortable getSorter() {
		return sorter;
	}

	@Override
	public void setSorter(Sortable sorter) {
		this.sorter = sorter;
	}

	public String getSecret2Like() {
		return secret2Like;
	}

	public void setSecret2Like(String secret2Like) {
		this.secret2Like = secret2Like;
	}

	public String getSecret2Equal() {
		return secret2Equal;
	}

	public void setSecret2Equal(String secret2Equal) {
		this.secret2Equal = secret2Equal;
	}

}
