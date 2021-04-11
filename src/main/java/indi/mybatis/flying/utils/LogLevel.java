package indi.mybatis.flying.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public enum LogLevel {

	NONE("none"), TRACE("trace"), DEBUG("debug"), INFO("info"), WARN("warn"), ERROR("error"), FATAL("fatal");

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
