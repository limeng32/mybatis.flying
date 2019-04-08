package indi.mybatis.flying.pojo.condition;

import indi.mybatis.flying.models.Conditionable;
import indi.mybatis.flying.models.Limitable;
import indi.mybatis.flying.models.Sortable;
import indi.mybatis.flying.pojo.Permission;

public class PermissionCondition extends Permission implements Conditionable{
	
	private static final long serialVersionUID = 1L;

	private Limitable limiter;

	private Sortable sorter;
	
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
	
}