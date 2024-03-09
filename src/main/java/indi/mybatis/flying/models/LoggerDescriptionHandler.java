package indi.mybatis.flying.models;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import indi.mybatis.flying.utils.LogLevel;

/**
 * 
 * @date 2024-03-10 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public class LoggerDescriptionHandler implements LoggerDescriptionable {

	private static final Map<String, LogLevel> LOGGER_MAP = new ConcurrentHashMap<>();

	public LogLevel loggerMapPut(String methodId, LogLevel logLevel){
		return LOGGER_MAP.put(methodId, logLevel);
	}

	public boolean contains(Set<String> set, String methodId) {
		boolean ret = false;
		ret = set.contains(methodId);
		if (!ret) {
			for (String s : set) {
				if (s != null && methodId.startsWith(s + ".")) {
					ret = true;
					break;
				}
			}
		}
		return ret;
	}

	@Override
	public LogLevel getLogLevel(String methodId) {
		if (LOGGER_MAP.containsKey(methodId)) {
			return LOGGER_MAP.get(methodId);
		}
		LOGGER_MAP.put(methodId, LogLevel.NONE);
		return LogLevel.NONE;
	}

}
