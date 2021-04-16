package indi.mybatis.flying.models;

public class AggregateModel {

	private AggregateFunction function;

	private String param;

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
