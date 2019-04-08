package indi.mybatis.flying.statics;

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