package indi.mybatis.flying.statics;

public enum OpLockType {
	/** 版本号型乐观锁，适合整数型字段 */
	Version,
	/** 非乐观锁字段 */
	Null
}
