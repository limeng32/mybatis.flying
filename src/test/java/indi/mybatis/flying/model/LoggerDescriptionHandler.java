package indi.mybatis.flying.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import indi.mybatis.flying.models.LoggerDescriptionable;
import indi.mybatis.flying.utils.LogLevel;

public class LoggerDescriptionHandler implements LoggerDescriptionable {

	private static final Map<String, LogLevel> loggerMap = new ConcurrentHashMap<>();

	private Set<String> fatalLoggerSet = getSet(ApplicationContextProvider.getBean(MyValue.class).getFatal());

	private Set<String> errorLoggerSet = getSet(ApplicationContextProvider.getBean(MyValue.class).getError());

	private Set<String> warnLoggerSet = getSet(ApplicationContextProvider.getBean(MyValue.class).getWarn());

	private Set<String> infoLoggerSet = getSet(ApplicationContextProvider.getBean(MyValue.class).getInfo());

	private Set<String> debugLoggerSet = getSet(ApplicationContextProvider.getBean(MyValue.class).getDebug());

	private Set<String> traceLoggerSet = getSet(ApplicationContextProvider.getBean(MyValue.class).getTrace());

	private Set<String> getSet(String[] array) {
		return array == null ? Collections.emptySet() : new HashSet<>(Arrays.asList(array));
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
		if (loggerMap.containsKey(methodId)) {
			return loggerMap.get(methodId);
		}
		if (contains(fatalLoggerSet, methodId)) {
			loggerMap.put(methodId, LogLevel.FATAL);
			return LogLevel.FATAL;
		} else if (contains(errorLoggerSet, methodId)) {
			loggerMap.put(methodId, LogLevel.ERROR);
			return LogLevel.ERROR;
		} else if (contains(warnLoggerSet, methodId)) {
			loggerMap.put(methodId, LogLevel.WARN);
			return LogLevel.WARN;
		} else if (contains(infoLoggerSet, methodId)) {
			loggerMap.put(methodId, LogLevel.INFO);
			return LogLevel.INFO;
		} else if (contains(debugLoggerSet, methodId)) {
			loggerMap.put(methodId, LogLevel.DEBUG);
			return LogLevel.DEBUG;
		} else if (contains(traceLoggerSet, methodId)) {
			loggerMap.put(methodId, LogLevel.TRACE);
			return LogLevel.TRACE;
		}
		loggerMap.put(methodId, LogLevel.NONE);
		return LogLevel.NONE;
	}

}
