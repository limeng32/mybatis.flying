package indi.mybatis.flying.exception;

public enum AutoMapperExceptionEnum {

	parameterObjectIsNull("ParameterObject is null"), dialectPropertyCannotFound(
			"Dialect property is not found"), noTypeHandlerSuitable("There was no TypeHandler found for parameter ");

	private final String description;

	private AutoMapperExceptionEnum(String description) {
		this.description = description;
	}

	public String description() {
		return description;
	}

	public String toString() {
		return description;
	}
}
