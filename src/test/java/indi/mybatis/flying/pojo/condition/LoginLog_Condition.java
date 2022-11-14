package indi.mybatis.flying.pojo.condition;

import java.util.Collection;
import java.util.Date;

import org.apache.ibatis.type.LongTypeHandler;

import indi.mybatis.flying.annotations.ConditionMapperAnnotation;
import indi.mybatis.flying.annotations.Or;
import indi.mybatis.flying.models.Conditionable;
import indi.mybatis.flying.models.Limitable;
import indi.mybatis.flying.models.Sortable;
import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.statics.ConditionType;

public class LoginLog_Condition extends LoginLog_ implements Conditionable {

	private static final long serialVersionUID = 1L;

	private Limitable limiter;

	private Sortable sorter;

	@ConditionMapperAnnotation(dbFieldName = "loginTime", conditionType = ConditionType.GREATER_THAN)
	private Date loginTimeGreaterThan;

	@ConditionMapperAnnotation(dbFieldName = "loginTime", conditionType = ConditionType.GREATER_OR_EQUAL)
	private Date loginTimeGreaterOrEqual;

	@ConditionMapperAnnotation(dbFieldName = "loginTime", conditionType = ConditionType.NOT_EQUAL)
	private Date loginTimeNotEqual;

	@ConditionMapperAnnotation(dbFieldName = "loginTime", conditionType = ConditionType.LESS_THAN)
	private Date loginTimeLessThan;

	@ConditionMapperAnnotation(dbFieldName = "ID", conditionType = ConditionType.GREATER_THAN)
	private Integer idGreaterThan;

	@ConditionMapperAnnotation(dbFieldName = "id", conditionType = ConditionType.GREATER_OR_EQUAL)
	private Integer idGreaterOrEqual;

	@ConditionMapperAnnotation(dbFieldName = "id", conditionType = ConditionType.LESS_THAN)
	private Integer idLessThan;

	@ConditionMapperAnnotation(dbFieldName = "id", conditionType = ConditionType.LESS_OR_EQUAL)
	private Integer idLessOrEqual;

	@ConditionMapperAnnotation(dbFieldName = "ID", conditionType = ConditionType.NOT_EQUAL)
	private Integer idNotEqual;

	@ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.IN)
	private Collection<String> loginIPIn;

	@ConditionMapperAnnotation(dbFieldName = "lOginTime", conditionType = ConditionType.IN)
	private Collection<Date> loginTimeIn;

	@ConditionMapperAnnotation(dbFieldName = "logINIP", conditionType = ConditionType.NOT_IN)
	private Collection<String> loginIPNotIn;

	@ConditionMapperAnnotation(dbFieldName = "loginTime", conditionType = ConditionType.NOT_IN)
	private Collection<Date> loginTimeNotIn;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.HEAD_LIKE),
			@ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.HEAD_LIKE) })
	private Object[] loginIPHeadLikeOr;

	@ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.TAIL_LIKE)
	private String ipLikeFilter;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.LIKE),
			@ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.LIKE) })
	private Object[] loginIPLikeOr;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.HEAD_LIKE),
			@ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.TAIL_LIKE) })
	private Object[] loginIPHeadLikeOrTailLike;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.EQUAL),
			@ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.EQUAL) })
	private Object[] loginIPEqualsOr;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "num", conditionType = ConditionType.EQUAL),
			@ConditionMapperAnnotation(dbFieldName = "num", conditionType = ConditionType.EQUAL) })
	private Object[] numEqualsOr;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "num", conditionType = ConditionType.EQUAL),
			@ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.LIKE) })
	private Object[] numEqualsOrLoginIPLike;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "loginTime", conditionType = ConditionType.EQUAL),
			@ConditionMapperAnnotation(dbFieldName = "loginTime", conditionType = ConditionType.EQUAL) })
	private Object[] loginTimeEqualsOr;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "status", conditionType = ConditionType.EQUAL),
			@ConditionMapperAnnotation(dbFieldName = "status", conditionType = ConditionType.EQUAL) })
	private Object[] statusEqualsOr;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.NOT_EQUAL),
			@ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.NOT_EQUAL) })
	private Object[] loginIPNotEqualsOr;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "num", conditionType = ConditionType.GREATER_THAN),
			@ConditionMapperAnnotation(dbFieldName = "num", conditionType = ConditionType.LESS_THAN) })
	private Object[] numGtOrLt;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "num", conditionType = ConditionType.GREATER_OR_EQUAL),
			@ConditionMapperAnnotation(dbFieldName = "num", conditionType = ConditionType.LESS_OR_EQUAL) })
	private Object[] numGeOrLe;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "num", conditionType = ConditionType.GREATER_THAN),
			@ConditionMapperAnnotation(dbFieldName = "num", conditionType = ConditionType.LESS_OR_EQUAL) })
	private Object[] numGtOrLe;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "num", conditionType = ConditionType.GREATER_OR_EQUAL),
			@ConditionMapperAnnotation(dbFieldName = "num", conditionType = ConditionType.LESS_THAN) })
	private Object[] numGeOrLt;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "status", conditionType = ConditionType.NULL_OR_NOT),
			@ConditionMapperAnnotation(dbFieldName = "status", conditionType = ConditionType.NULL_OR_NOT) })
	private Object[] statusIsNullOr;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "status", conditionType = ConditionType.NULL_OR_NOT),
			@ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.EQUAL),
			@ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.EQUAL) })
	private Object[] statusIsNullOrLoginIPEquals;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "accountId", conditionType = ConditionType.EQUAL, customTypeHandler = LongTypeHandler.class, delegate = true),
			@ConditionMapperAnnotation(dbFieldName = "accountId", conditionType = ConditionType.EQUAL, customTypeHandler = LongTypeHandler.class, delegate = true),
			@ConditionMapperAnnotation(dbFieldName = "accountId", conditionType = ConditionType.GREATER_THAN, customTypeHandler = LongTypeHandler.class, delegate = true),
			@ConditionMapperAnnotation(dbFieldName = "accountId", conditionType = ConditionType.LIKE, customTypeHandler = LongTypeHandler.class, delegate = true),
			@ConditionMapperAnnotation(dbFieldName = "accountId", conditionType = ConditionType.NULL_OR_NOT), })
	private Object[] accountEqualsOr;

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

	public Date getLoginTimeGreaterThan() {
		return loginTimeGreaterThan;
	}

	public void setLoginTimeGreaterThan(Date loginTimeGreaterThan) {
		this.loginTimeGreaterThan = loginTimeGreaterThan;
	}

	public Integer getIdGreaterThan() {
		return idGreaterThan;
	}

	public void setIdGreaterThan(Integer idGreaterThan) {
		this.idGreaterThan = idGreaterThan;
	}

	public Date getLoginTimeGreaterOrEqual() {
		return loginTimeGreaterOrEqual;
	}

	public void setLoginTimeGreaterOrEqual(Date loginTimeGreaterOrEqual) {
		this.loginTimeGreaterOrEqual = loginTimeGreaterOrEqual;
	}

	public Date getLoginTimeNotEqual() {
		return loginTimeNotEqual;
	}

	public void setLoginTimeNotEqual(Date loginTimeNotEqual) {
		this.loginTimeNotEqual = loginTimeNotEqual;
	}

	public Integer getIdGreaterOrEqual() {
		return idGreaterOrEqual;
	}

	public void setIdGreaterOrEqual(Integer idGreaterOrEqual) {
		this.idGreaterOrEqual = idGreaterOrEqual;
	}

	public Integer getIdLessThan() {
		return idLessThan;
	}

	public void setIdLessThan(Integer idLessThan) {
		this.idLessThan = idLessThan;
	}

	public Integer getIdLessOrEqual() {
		return idLessOrEqual;
	}

	public void setIdLessOrEqual(Integer idLessOrEqual) {
		this.idLessOrEqual = idLessOrEqual;
	}

	public Integer getIdNotEqual() {
		return idNotEqual;
	}

	public void setIdNotEqual(Integer idNotEqual) {
		this.idNotEqual = idNotEqual;
	}

	public Collection<String> getLoginIPIn() {
		return loginIPIn;
	}

	public void setLoginIPIn(Collection<String> loginIPIn) {
		this.loginIPIn = loginIPIn;
	}

	public Collection<Date> getLoginTimeIn() {
		return loginTimeIn;
	}

	public void setLoginTimeIn(Collection<Date> loginTimeIn) {
		this.loginTimeIn = loginTimeIn;
	}

	public Collection<String> getLoginIPNotIn() {
		return loginIPNotIn;
	}

	public void setLoginIPNotIn(Collection<String> loginIPNotIn) {
		this.loginIPNotIn = loginIPNotIn;
	}

	public Collection<Date> getLoginTimeNotIn() {
		return loginTimeNotIn;
	}

	public void setLoginTimeNotIn(Collection<Date> loginTimeNotIn) {
		this.loginTimeNotIn = loginTimeNotIn;
	}

	public Object[] getLoginIPHeadLikeOr() {
		return loginIPHeadLikeOr;
	}

	public void setLoginIPHeadLikeOr(Object... loginIPHeadLikeOr) {
		this.loginIPHeadLikeOr = loginIPHeadLikeOr;
	}

	public String getIpLikeFilter() {
		return ipLikeFilter;
	}

	public void setIpLikeFilter(String ipLikeFilter) {
		this.ipLikeFilter = ipLikeFilter;
	}

	public Object[] getLoginIPLikeOr() {
		return loginIPLikeOr;
	}

	public void setLoginIPLikeOr(Object... loginIPLikeOr) {
		this.loginIPLikeOr = loginIPLikeOr;
	}

	public Object[] getLoginIPHeadLikeOrTailLike() {
		return loginIPHeadLikeOrTailLike;
	}

	public void setLoginIPHeadLikeOrTailLike(Object... loginIPHeadLikeOrTailLike) {
		this.loginIPHeadLikeOrTailLike = loginIPHeadLikeOrTailLike;
	}

	public Object[] getLoginIPEqualsOr() {
		return loginIPEqualsOr;
	}

	public void setLoginIPEqualsOr(Object... loginIPEqualsOr) {
		this.loginIPEqualsOr = loginIPEqualsOr;
	}

	public Object[] getNumEqualsOr() {
		return numEqualsOr;
	}

	public void setNumEqualsOr(Object... numEqualsOr) {
		this.numEqualsOr = numEqualsOr;
	}

	public Object[] getNumEqualsOrLoginIPLike() {
		return numEqualsOrLoginIPLike;
	}

	public void setNumEqualsOrLoginIPLike(Object... numEqualsOrLoginIPLike) {
		this.numEqualsOrLoginIPLike = numEqualsOrLoginIPLike;
	}

	public Date getLoginTimeLessThan() {
		return loginTimeLessThan;
	}

	public void setLoginTimeLessThan(Date loginTimeLessThan) {
		this.loginTimeLessThan = loginTimeLessThan;
	}

	public Object[] getLoginTimeEqualsOr() {
		return loginTimeEqualsOr;
	}

	public void setLoginTimeEqualsOr(Object... loginTimeEqualsOr) {
		this.loginTimeEqualsOr = loginTimeEqualsOr;
	}

	public Object[] getStatusEqualsOr() {
		return statusEqualsOr;
	}

	public void setStatusEqualsOr(Object... statusEqualsOr) {
		this.statusEqualsOr = statusEqualsOr;
	}

	public Object[] getLoginIPNotEqualsOr() {
		return loginIPNotEqualsOr;
	}

	public void setLoginIPNotEqualsOr(Object... loginIPNotEqualsOr) {
		this.loginIPNotEqualsOr = loginIPNotEqualsOr;
	}

	public Object[] getNumGtOrLt() {
		return numGtOrLt;
	}

	public void setNumGtOrLt(Object... numGtOrLt) {
		this.numGtOrLt = numGtOrLt;
	}

	public Object[] getNumGeOrLe() {
		return numGeOrLe;
	}

	public void setNumGeOrLe(Object... numGeOrLe) {
		this.numGeOrLe = numGeOrLe;
	}

	public Object[] getNumGtOrLe() {
		return numGtOrLe;
	}

	public void setNumGtOrLe(Object... numGtOrLe) {
		this.numGtOrLe = numGtOrLe;
	}

	public Object[] getNumGeOrLt() {
		return numGeOrLt;
	}

	public void setNumGeOrLt(Object... numGeOrLt) {
		this.numGeOrLt = numGeOrLt;
	}

	public Object[] getStatusIsNullOr() {
		return statusIsNullOr;
	}

	public void setStatusIsNullOr(Object... statusIsNullOr) {
		this.statusIsNullOr = statusIsNullOr;
	}

	public Object[] getStatusIsNullOrLoginIPEquals() {
		return statusIsNullOrLoginIPEquals;
	}

	public void setStatusIsNullOrLoginIPEquals(Object... statusIsNullOrLoginIPEquals) {
		this.statusIsNullOrLoginIPEquals = statusIsNullOrLoginIPEquals;
	}

	public Object[] getAccountEqualsOr() {
		return accountEqualsOr;
	}

	public void setAccountEqualsOr(Object... accountEqualsOr) {
		this.accountEqualsOr = accountEqualsOr;
	}

}
