package indi.mybatis.flying.models;

public class AggregateModel {

	private String alias;

	private AggregateFunction function;

	private String param;

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

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

}
