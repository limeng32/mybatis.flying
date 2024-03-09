package indi.mybatis.flying.models;

import indi.mybatis.flying.utils.LogLevel;

/**
 * 
 * @date 2024-03-09 20:55:08
 *
 * @author david, 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 * @description Generate SQL through annotations.
 */
public interface LoggerDescriptionable {
	/**
	 * Get LogLevel by mybatis method id
	 * 
	 * @param methodId mybatis method id
	 * @return LogLevel
	 */
	public LogLevel getLogLevel(String methodId);

	/**
	 * Put methodId and logLevel in the loggerMap
	 * 
	 * @param methodId mybatis method id
	 * @param logLevel logLevel of the mothod
	 * @return LogLevel
	 */
	public LogLevel loggerMapPut(String methodId, LogLevel logLevel);
}
