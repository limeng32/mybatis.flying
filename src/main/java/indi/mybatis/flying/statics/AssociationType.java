package indi.mybatis.flying.statics;

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