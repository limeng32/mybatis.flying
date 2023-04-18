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

	public Order(String field, Conditionable.Sequence sequence) {
		this.field = field;
		this.sequence = sequence.toString();
	}

	public Order(String field, Conditionable.Sequence sequence, Object object) {
		this.field = field;
		this.sequence = sequence.toString();
		this.object = object;
	}

	private String field;

	private String sequence;

	private Object object;

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

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public StringBuilder toSql() {
		return new StringBuilder(" ").append(field).append(" ").append(sequence).append(",");
	}
}
