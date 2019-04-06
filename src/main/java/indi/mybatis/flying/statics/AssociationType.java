package indi.mybatis.flying.statics;

public enum AssociationType {

	Equal("="), GreaterThan(">"), GreaterOrEqual(">="), LessThan("<"), LessOrEqual("<="), NotEqual("<>");

	private final String value;

	private AssociationType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
