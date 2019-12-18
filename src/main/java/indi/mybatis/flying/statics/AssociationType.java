package indi.mybatis.flying.statics;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @Email limeng32@live.cn
 * @version
 * @since JDK 1.8
 */
public enum AssociationType {

	LeftJoin(" left join "), RightJoin(" right join ");

	private final String value;

	private AssociationType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
