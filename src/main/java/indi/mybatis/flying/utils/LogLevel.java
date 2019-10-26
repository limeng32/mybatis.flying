package indi.mybatis.flying.utils;

import java.util.HashMap;
import java.util.Map;

public enum LogLevel {

	NONE("none"), TRACE("trace"), DEBUG("debug"), WARN("warn"), ERROR("error");

	private final String value;

	private LogLevel(String value) {
		this.value = value;
	}

	private static final Map<String, LogLevel> nameLookup = new HashMap<>(8);

	static {
		for (LogLevel e : LogLevel.values()) {
			nameLookup.put(e.value, e);
		}
	}

	public static LogLevel forValue(String value) {
		return nameLookup.get(value);
	}
}
