package indi.mybatis.flying.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import indi.mybatis.flying.models.LoggerDescriptionable;
import indi.mybatis.flying.utils.LogLevel;

public class LoggerDescriptionHandler implements LoggerDescriptionable {

	private static final Map<String, LogLevel> loggerMap = new ConcurrentHashMap<>();

	private Set<String> loggerSet = new HashSet<>(Arrays.asList(getErrorLogger()));

	@Override
	public LogLevel getLogLevel(String methodId) {
		if (loggerMap.containsKey(methodId)) {
			return loggerMap.get(methodId);
		}
		if (loggerSet.contains(methodId)) {
			loggerMap.put(methodId, LogLevel.ERROR);
			return LogLevel.ERROR;
		}
		return LogLevel.NONE;
	}

	public String[] getErrorLogger() {
		return ApplicationContextProvider.getBean(MyValue.class).getError();
	}

}
