package indi.mybatis.flying.statics;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public enum OpLockType {
	/** Version number optimistic lock, suitable for integer fields */
	VERSION,
	/** Non-optimistic lock field */
	NULL
}
