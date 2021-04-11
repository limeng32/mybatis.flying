package indi.mybatis.flying.models;

import indi.mybatis.flying.utils.LogLevel;

public interface LoggerDescriptionable {

	public LogLevel getLogLevel(String method);
	
}
