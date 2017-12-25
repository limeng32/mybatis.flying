package indi.mybatis.flying.pojo.condition;

import java.util.Collection;
import java.util.Date;

import indi.mybatis.flying.annotations.ConditionMapperAnnotation;
import indi.mybatis.flying.annotations.ConditionMapperOrAnnotation;
import indi.mybatis.flying.annotations.QueryMapperAnnotation;
import indi.mybatis.flying.models.Conditionable;
import indi.mybatis.flying.models.Limitable;
import indi.mybatis.flying.models.Sortable;
import indi.mybatis.flying.pojo.LoginLog_;
import indi.mybatis.flying.statics.ConditionType;

@QueryMapperAnnotation(tableName = "LOGINLOG_")
public class LoginLog_Condition extends LoginLog_ implements Conditionable {

	private static final long serialVersionUID = 1L;

	private Limitable limiter;

	private Sortable sorter;

	@ConditionMapperAnnotation(dbFieldName = "loginTime", conditionType = ConditionType.GreaterThan)
	private Date loginTimeGreaterThan;

	@ConditionMapperAnnotation(dbFieldName = "loginTime", conditionType = ConditionType.GreaterOrEqual)
	private Date loginTimeGreaterOrEqual;

	@ConditionMapperAnnotation(dbFieldName = "loginTime", conditionType = ConditionType.NotEqual)
	private Date loginTimeNotEqual;

	@ConditionMapperAnnotation(dbFieldName = "ID", conditionType = ConditionType.GreaterThan)
	private Integer idGreaterThan;

	@ConditionMapperAnnotation(dbFieldName = "id", conditionType = ConditionType.GreaterOrEqual)
	private Integer idGreaterOrEqual;

	@ConditionMapperAnnotation(dbFieldName = "id", conditionType = ConditionType.LessThan)
	private Integer idLessThan;

	@ConditionMapperAnnotation(dbFieldName = "id", conditionType = ConditionType.LessOrEqual)
	private Integer idLessOrEqual;

	@ConditionMapperAnnotation(dbFieldName = "ID", conditionType = ConditionType.NotEqual)
	private Integer idNotEqual;

	@ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.In)
	private Collection<String> loginIPIn;

	@ConditionMapperAnnotation(dbFieldName = "lOginTime", conditionType = ConditionType.In)
	private Collection<Date> loginTimeIn;

	@ConditionMapperAnnotation(dbFieldName = "logINIP", conditionType = ConditionType.NotIn)
	private Collection<String> loginIPNotIn;

	@ConditionMapperAnnotation(dbFieldName = "loginTime", conditionType = ConditionType.NotIn)
	private Collection<Date> loginTimeNotIn;

	@ConditionMapperOrAnnotation({
			@ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.Equal),
			@ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.Equal) })
	private Object[] loginIPOr;

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

	public Object[] getLoginIPOr() {
		return loginIPOr;
	}

	public void setLoginIPOr(Object... loginIPOr) {
		this.loginIPOr = loginIPOr;
	}

}
