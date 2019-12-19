package indi.mybatis.flying.pagination;

import indi.mybatis.flying.models.Conditionable;
import indi.mybatis.flying.models.Queryable;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public class Order {

	public static final String DOT = ".";

	public Order(String field, Conditionable.Sequence sequence) {
		this.field = field;
		this.sequence = sequence.toString();
	}

	public Order(Queryable queryable, Conditionable.Sequence sequence) {
		this.field = queryable.getTableName() + DOT + queryable.value();
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
