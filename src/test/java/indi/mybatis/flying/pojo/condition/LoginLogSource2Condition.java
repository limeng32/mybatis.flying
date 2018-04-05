package indi.mybatis.flying.pojo.condition;

import org.apache.ibatis.type.LongTypeHandler;

import indi.mybatis.flying.annotations.ConditionMapperAnnotation;
import indi.mybatis.flying.annotations.Or;
import indi.mybatis.flying.models.Conditionable;
import indi.mybatis.flying.models.Limitable;
import indi.mybatis.flying.models.Sortable;
import indi.mybatis.flying.pojo.Detail2_;
import indi.mybatis.flying.pojo.LoginLogSource2;
import indi.mybatis.flying.statics.ConditionType;

public class LoginLogSource2Condition extends LoginLogSource2 implements Conditionable {

	private static final long serialVersionUID = 1L;

	private Limitable limiter;

	private Sortable sorter;

	@ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.HeadLike)
	private String ipLikeFilter;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "accountId", conditionType = ConditionType.Equal, customTypeHandler = LongTypeHandler.class),
			@ConditionMapperAnnotation(dbFieldName = "accountId", conditionType = ConditionType.Equal, customTypeHandler = LongTypeHandler.class) })
	private Object[] accountEqualsOr;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "accountId", conditionType = ConditionType.Equal, customTypeHandler = LongTypeHandler.class),
			@ConditionMapperAnnotation(dbFieldName = "accountId", conditionType = ConditionType.Equal, customTypeHandler = LongTypeHandler.class),
			@ConditionMapperAnnotation(dbFieldName = "LOGINIP", conditionType = ConditionType.Equal), })
	private Object[] accountEqualsOr2;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "accountId", conditionType = ConditionType.Equal, customTypeHandler = LongTypeHandler.class),
			@ConditionMapperAnnotation(dbFieldName = "accountId", conditionType = ConditionType.Equal, customTypeHandler = LongTypeHandler.class),
			@ConditionMapperAnnotation(dbFieldName = "LOGINIP", conditionType = ConditionType.Equal),
			@ConditionMapperAnnotation(dbFieldName = "NAME", conditionType = ConditionType.Equal, subTarget = Detail2_.class), })
	private Object[] accountEqualsOr3;

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

	public String getIpLikeFilter() {
		return ipLikeFilter;
	}

	public void setIpLikeFilter(String ipLikeFilter) {
		this.ipLikeFilter = ipLikeFilter;
	}

	public Object[] getAccountEqualsOr() {
		return accountEqualsOr;
	}

	public void setAccountEqualsOr(Object... accountEqualsOr) {
		this.accountEqualsOr = accountEqualsOr;
	}

	public Object[] getAccountEqualsOr2() {
		return accountEqualsOr2;
	}

	public void setAccountEqualsOr2(Object... accountEqualsOr2) {
		this.accountEqualsOr2 = accountEqualsOr2;
	}

	public Object[] getAccountEqualsOr3() {
		return accountEqualsOr3;
	}

	public void setAccountEqualsOr3(Object... accountEqualsOr3) {
		this.accountEqualsOr3 = accountEqualsOr3;
	}

}
