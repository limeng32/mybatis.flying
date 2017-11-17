package indi.mybatis.flying.statics;

public enum KeyGeneratorType {
	/** 以UUID方式生成 */
	uuid,
	/** 以不带横线的UUID方式生成 */
	uuid_no_line,
	/** 精确到毫秒的时间戳 */
	millisecond,
	/** 精确到微秒的时间戳 */
	microsecond
}
