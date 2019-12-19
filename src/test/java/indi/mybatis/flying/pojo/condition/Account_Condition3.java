package indi.mybatis.flying.pojo.condition;

import indi.mybatis.flying.annotations.ConditionMapperAnnotation;
import indi.mybatis.flying.models.Conditionable;
import indi.mybatis.flying.models.Limitable;
import indi.mybatis.flying.models.Sortable;
import indi.mybatis.flying.pojo.Account_;
import indi.mybatis.flying.statics.ConditionType;

public class Account_Condition3 extends Account_ implements Conditionable {

	private static final long serialVersionUID = 1L;

	private Limitable limiter;

	private Sortable sorter;

	// 为测试多个condition是否会冲突，故意将变量命名为与实际行为相反
	@ConditionMapperAnnotation(dbFieldName = "email", conditionType = ConditionType.TAIL_LIKE)
	private String emailHeadLike;

	// 为测试多个condition是否会冲突，故意将变量命名为与实际行为相反
	@ConditionMapperAnnotation(dbFieldName = "email", conditionType = ConditionType.HEAD_LIKE)
	private String emailTailLike;

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

}
