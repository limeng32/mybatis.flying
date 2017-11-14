package indi.mybatis.flying.statics;

public enum KeyGenerationType {
	/** 以UUID方式生成 */
	uuid,
	/** 精确到毫秒的时间戳 */
	millisecond,
	/** 精确到微秒的时间戳 */
	microsecond
}
