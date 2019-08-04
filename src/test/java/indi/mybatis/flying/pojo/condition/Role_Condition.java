package indi.mybatis.flying.pojo.condition;

import indi.mybatis.flying.annotations.ConditionMapperAnnotation;
import indi.mybatis.flying.annotations.Or;
import indi.mybatis.flying.models.Conditionable;
import indi.mybatis.flying.models.Limitable;
import indi.mybatis.flying.models.Sortable;
import indi.mybatis.flying.pojo.Role_;
import indi.mybatis.flying.statics.ConditionType;

public class Role_Condition extends Role_ implements Conditionable {

	private static final long serialVersionUID = 1L;

	private Limitable limiter;

	private Sortable sorter;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.Equal),
			@ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.Equal, subTarget = indi.mybatis.flying.pojo.Account_.class) })
	private Object[] nameEqualsOrAccountNameEquals;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.Equal, subTarget = indi.mybatis.flying.pojo.Account_.class) })
	private Object[] accountNameEquals;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.Equal) })
	private Object[] nameEquals;

	@Or({ @ConditionMapperAnnotation(dbFieldName = "name", conditionType = ConditionType.NotEqual) })
	private Object[] nameNotEquals;

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

	public Object[] getNameEqualsOrAccountNameEquals() {
		return nameEqualsOrAccountNameEquals;
	}

	public void setNameEqualsOrAccountNameEquals(Object... nameEqualsOrAccountNameEquals) {
		this.nameEqualsOrAccountNameEquals = nameEqualsOrAccountNameEquals;
	}

	public Object[] getAccountNameEquals() {
		return accountNameEquals;
	}

	public void setAccountNameEquals(Object... accountNameEquals) {
		this.accountNameEquals = accountNameEquals;
	}

	public Object[] getNameEquals() {
		return nameEquals;
	}

	public void setNameEquals(Object... nameEquals) {
		this.nameEquals = nameEquals;
	}

	public Object[] getNameNotEquals() {
		return nameNotEquals;
	}

	public void setNameNotEquals(Object... nameNotEquals) {
		this.nameNotEquals = nameNotEquals;
	}

}
