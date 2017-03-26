package indi.mybatis.flying.pagination;

import indi.mybatis.flying.models.Conditionable;
import indi.mybatis.flying.models.Queryable;

public class Order {

	public Order(String field, Conditionable.Sequence sequence) {
		this.field = field;
		this.sequence = sequence.toString();
	}

	public Order(Queryable queryable, Conditionable.Sequence sequence) {
		this.field = queryable.getTableName() + Conditionable.dot + queryable.value();
		this.sequence = sequence.toString();
	}

	private String field;

	private String sequence;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String toSql() {
		return new StringBuffer().append(" ").append(field).append(" ").append(sequence).append(",").toString();
	}
}
