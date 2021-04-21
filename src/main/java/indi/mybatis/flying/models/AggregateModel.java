package indi.mybatis.flying.models;

public class AggregateModel {

	private String alias;

	private AggregateFunction function;

	private String column;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public AggregateFunction getFunction() {
		return function;
	}

	public void setFunction(AggregateFunction function) {
		this.function = function;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

}
