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
public enum AssociationCondition {

	Equal("="), GreaterThan(">"), GreaterOrEqual(">="), LessThan("<"), LessOrEqual("<="), NotEqual("<>");

	private final String value;

	private AssociationCondition(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
