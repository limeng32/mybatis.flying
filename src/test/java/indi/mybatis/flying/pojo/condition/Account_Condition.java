package indi.mybatis.flying.pojo.condition;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.type.IntegerTypeHandler;
import org.apache.ibatis.type.StringTypeHandler;

import indi.mybatis.flying.annotations.ConditionMapperAnnotation;
import indi.mybatis.flying.annotations.Or;
import indi.mybatis.flying.handler.StoryStatusHandler;
import indi.mybatis.flying.models.Conditionable;
import indi.mybatis.flying.models.Limitable;
import indi.mybatis.flying.models.Queryable;
import indi.mybatis.flying.models.Sortable;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.pojo.StoryStatus_;
import indi.mybatis.flying.statics.ConditionType;

public class Account_Condition extends Account_ implements Conditionable {

	private static final long serialVersionUID = 1L;

	public static final String field_tableName = "account_";

	public static final String field_id = "id";

	public static final String field_name = "name";

	public static final String field_email = "email";

	public static final String field_password = "password";

	public static final String field_activated = "activated";

	public static final String field_activateValue = "activateValue";

	public static final String field_opLock = "opLock";

	public enum Field implements Queryable {
		tableName(field_tableName), id(field_id), name(field_name), email(field_email), password(field_password),
		activated(field_activated), activateValue(field_activateValue);

		private final String value;

		private Field(String value) {
			this.value = value;
		}

		@Override
		public String value() {
			return value;
		}

		@Override
		public String getTableName() {
			return tableName.value;
		}
	}

	private Limitable limiter;

	private Sortable sorter;

	@ConditionMapperAnnotation(dbFieldName = "Email", conditionType = ConditionType.LIKE)
	private String emailLike;

	@ConditionMapperAnnotation(dbFieldName = field_email, conditionType = ConditionType.HEAD_LIKE)
	private String emailHeadLike;

	@ConditionMapperAnnotation(dbFieldName = field_email, conditionType = ConditionType.TAIL_LIKE)
	private String emailTailLike;

	@ConditionMapperAnnotation(dbFieldName = field_email, conditionType = ConditionType.MULTI_LIKE_AND)
	private List<String> multiLike;

	@ConditionMapperAnnotation(dbFieldName = field_email, conditionType = ConditionType.MULTI_LIKE_OR)
	private List<String> multiLikeOR;

	@ConditionMapperAnnotation(dbFieldName = field_name, conditionType = ConditionType.IN, customTypeHandler = StringTypeHandler.class)
	private Collection<String> nameIn;

	@ConditionMapperAnnotation(dbFieldName = field_opLock, conditionType = ConditionType.IN)
	private Collection<Integer> opLockIn;

	@ConditionMapperAnnotation(dbFieldName = field_name, conditionType = ConditionType.NOT_IN)
	private Collection<String> nameNotIn;

	@ConditionMapperAnnotation(dbFieldName = field_opLock, conditionType = ConditionType.NOT_IN)
	private Collection<Integer> opLockNotIn;

	@ConditionMapperAnnotation(dbFieldName = field_email, conditionType = ConditionType.NULL_OR_NOT)
	private Boolean emailIsNull;

	@ConditionMapperAnnotation(dbFieldName = "role_ID", conditionType = ConditionType.NULL_OR_NOT)
	private Boolean roleIsNull;

	@ConditionMapperAnnotation(dbFieldName = "role_ID", conditionType = ConditionType.IN, customTypeHandler = IntegerTypeHandler.class)
	private List<Integer> roleIdIn;

	@ConditionMapperAnnotation(dbFieldName = "role_ID", conditionType = ConditionType.NOT_IN)
	private List<Integer> roleIdNotIn;

	// Conditions designed for batch update
	@ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.EQUAL)
	private String nameEqual;

	// Conditions designed for batch update
	@ConditionMapperAnnotation(dbFieldName = "activateValue", conditionType = ConditionType.EQUAL)
	private String activateValueEqual;

	// Conditions designed for batch update
	@ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.LESS_OR_EQUAL)
	private String nameLessOrEqual;

	// Conditions designed for batch update
	@ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.LESS_THAN)
	private String nameLessThan;

	// Conditions designed for batch update
	@ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.GREATER_OR_EQUAL)
	private String nameGreaterOrEqual;

	// Conditions designed for batch update
	@ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.GREATER_THAN)
	private String nameGreaterThan;

	// Conditions designed for batch update
	@ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.LIKE)
	private String nameLike;

	// Conditions designed for batch update
	@ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.HEAD_LIKE)
	private String nameHeadLike;

	// Conditions designed for batch update
	@ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.TAIL_LIKE)
	private String nameTailLike;

	// Conditions designed for batch update
	@ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.NOT_EQUAL)
	private String nameNotEqual;

	// Conditions designed for batch update
	@ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.NULL_OR_NOT)
	private Boolean nameIsNull;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.EQUAL),
			@ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.EQUAL),
			@ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.EQUAL, subTarget = indi.mybatis.flying.pojo.LoginLog_.class) })
	private Object[] nameEqualsOrLoginlogIpEquals;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "id", conditionType = ConditionType.EQUAL),
			@ConditionMapperAnnotation(dbFieldName = "id", conditionType = ConditionType.EQUAL) })
	private Object[] idEqualsOr;

	@ConditionMapperAnnotation(dbFieldName = "status", conditionType = ConditionType.EQUAL, customTypeHandler = StoryStatusHandler.class)
	private StoryStatus_ statusEquals;

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

	public String getEmailLike() {
		return emailLike;
	}

	public void setEmailLike(String emailLike) {
		this.emailLike = emailLike;
	}

	public String getEmailHeadLike() {
		return emailHeadLike;
	}

	public void setEmailHeadLike(String emailHeadLike) {
		this.emailHeadLike = emailHeadLike;
	}

	public String getEmailTailLike() {
		return emailTailLike;
	}

	public void setEmailTailLike(String emailTailLike) {
		this.emailTailLike = emailTailLike;
	}

	public List<String> getMultiLike() {
		return multiLike;
	}

	public void setMultiLike(List<String> multiLike) {
		this.multiLike = multiLike;
	}

	public List<String> getMultiLikeOR() {
		return multiLikeOR;
	}

	public void setMultiLikeOR(List<String> multiLikeOR) {
		this.multiLikeOR = multiLikeOR;
	}

	public Collection<String> getNameIn() {
		return nameIn;
	}

	public void setNameIn(Collection<String> nameIn) {
		this.nameIn = nameIn;
	}

	public Collection<Integer> getOpLockIn() {
		return opLockIn;
	}

	public void setOpLockIn(Collection<Integer> opLockIn) {
		this.opLockIn = opLockIn;
	}

	public Collection<String> getNameNotIn() {
		return nameNotIn;
	}

	public void setNameNotIn(Collection<String> nameNotIn) {
		this.nameNotIn = nameNotIn;
	}

	public Collection<Integer> getOpLockNotIn() {
		return opLockNotIn;
	}

	public void setOpLockNotIn(Collection<Integer> opLockNotIn) {
		this.opLockNotIn = opLockNotIn;
	}

	public Boolean getEmailIsNull() {
		return emailIsNull;
	}

	public void setEmailIsNull(Boolean emailIsNull) {
		this.emailIsNull = emailIsNull;
	}

	public Boolean getRoleIsNull() {
		return roleIsNull;
	}

	public void setRoleIsNull(Boolean roleIsNull) {
		this.roleIsNull = roleIsNull;
	}

	public Object[] getNameEqualsOrLoginlogIpEquals() {
		return nameEqualsOrLoginlogIpEquals;
	}

	public void setNameEqualsOrLoginlogIpEquals(Object... nameEqualsOrLoginlogIpEquals) {
		this.nameEqualsOrLoginlogIpEquals = nameEqualsOrLoginlogIpEquals;
	}

	public Object[] getIdEqualsOr() {
		return idEqualsOr;
	}

	public void setIdEqualsOr(Object... idEqualsOr) {
		this.idEqualsOr = idEqualsOr;
	}

	public List<Integer> getRoleIdIn() {
		return roleIdIn;
	}

	public void setRoleIdIn(List<Integer> roleIdIn) {
		this.roleIdIn = roleIdIn;
	}

	public List<Integer> getRoleIdNotIn() {
		return roleIdNotIn;
	}

	public void setRoleIdNotIn(List<Integer> roleIdNotIn) {
		this.roleIdNotIn = roleIdNotIn;
	}

	public String getNameEqual() {
		return nameEqual;
	}

	public void setNameEqual(String nameEqual) {
		this.nameEqual = nameEqual;
	}

	public String getActivateValueEqual() {
		return activateValueEqual;
	}

	public void setActivateValueEqual(String activateValueEqual) {
		this.activateValueEqual = activateValueEqual;
	}

	public String getNameLessOrEqual() {
		return nameLessOrEqual;
	}

	public void setNameLessOrEqual(String nameLessOrEqual) {
		this.nameLessOrEqual = nameLessOrEqual;
	}

	public String getNameLessThan() {
		return nameLessThan;
	}

	public void setNameLessThan(String nameLessThan) {
		this.nameLessThan = nameLessThan;
	}

	public String getNameGreaterOrEqual() {
		return nameGreaterOrEqual;
	}

	public void setNameGreaterOrEqual(String nameGreaterOrEqual) {
		this.nameGreaterOrEqual = nameGreaterOrEqual;
	}

	public String getNameGreaterThan() {
		return nameGreaterThan;
	}

	public void setNameGreaterThan(String nameGreaterThan) {
		this.nameGreaterThan = nameGreaterThan;
	}

	public String getNameLike() {
		return nameLike;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public String getNameHeadLike() {
		return nameHeadLike;
	}

	public void setNameHeadLike(String nameHeadLike) {
		this.nameHeadLike = nameHeadLike;
	}

	public String getNameTailLike() {
		return nameTailLike;
	}

	public void setNameTailLike(String nameTailLike) {
		this.nameTailLike = nameTailLike;
	}

	public String getNameNotEqual() {
		return nameNotEqual;
	}

	public void setNameNotEqual(String nameNotEqual) {
		this.nameNotEqual = nameNotEqual;
	}

	public Boolean getNameIsNull() {
		return nameIsNull;
	}

	public void setNameIsNull(Boolean nameIsNull) {
		this.nameIsNull = nameIsNull;
	}

	public StoryStatus_ getStatusEquals() {
		return statusEquals;
	}

	public void setStatusEquals(StoryStatus_ statusEquals) {
		this.statusEquals = statusEquals;
	}

}