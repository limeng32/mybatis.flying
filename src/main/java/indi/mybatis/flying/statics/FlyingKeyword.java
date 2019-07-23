package indi.mybatis.flying.statics;

public enum FlyingKeyword {

	ACTION("action"), CONNECTION_CATALOG("connectionCatalog"), DATA_SOURCE("dataSource"), FLYING("flying"),
	IGNORE_TAG("ignoreTag"), KEY_GENERATOR("keyGenerator"), PREFIX("prefix"), PROPERTIES("properties");

	private final String value;

	private FlyingKeyword(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

}
