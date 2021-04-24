package indi.mybatis.flying.models;

import java.util.HashMap;
import java.util.Map;

public enum AggregateFunction {

	COUNT("count"), SUM("sum"), AVG("avg"), MIN("min"), MAX("max"), NONE("none");

	private final String value;

	private AggregateFunction(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	private static final Map<String, AggregateFunction> mapForValue = new HashMap<>(8);

	static {
		AggregateFunction[] values = AggregateFunction.values();
		for (AggregateFunction e : values) {
			mapForValue.put(e.value(), e);
		}
	}

	public static AggregateFunction forValue(String value) {
		if (value == null) {
			return null;
		}
		return mapForValue.get(value.toLowerCase());
	}
}
