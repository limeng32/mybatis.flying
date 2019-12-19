package indi.mybatis.flying.statics;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public enum AssociationType {

	LEFT_JOIN(" left join "), RIGHT_JOIN(" right join ");

	private final String value;

	private AssociationType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
