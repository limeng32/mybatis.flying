package indi.mybatis.flying.pojo.condition;

import indi.mybatis.flying.annotations.ConditionMapperAnnotation;
import indi.mybatis.flying.annotations.QueryMapperAnnotation;
import indi.mybatis.flying.models.Conditionable;
import indi.mybatis.flying.models.Limitable;
import indi.mybatis.flying.models.Sortable;
import indi.mybatis.flying.pojo.LoginLogSource2;
import indi.mybatis.flying.statics.ConditionType;

@QueryMapperAnnotation(tableName = "LOGINLOG_")
public class LoginLogSource2Condition extends LoginLogSource2 implements Conditionable {

	private static final long serialVersionUID = 1L;

	private Limitable limiter;

	private Sortable sorter;

	@ConditionMapperAnnotation(dbFieldName = "loginIP", conditionType = ConditionType.HeadLike)
	private String ipLikeFilter;

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

}
