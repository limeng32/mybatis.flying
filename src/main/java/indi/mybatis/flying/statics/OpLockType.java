package indi.mybatis.flying.statics;

public enum OpLockType {
	/** Version number optimistic lock, suitable for integer fields */
	Version,
	/** Non-optimistic lock field */
	Null
}
