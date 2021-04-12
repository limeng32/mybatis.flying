package indi.mybatis.flying.exception;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public enum AutoMapperExceptionEnum {

	PARAMETER_OBJECT_IS_NULL("ParameterObject is null"), DIALECT_PROPERTY_CANNOT_FOUND("Dialect property is not found"),
	INSERT_BATCH_PARAMETER_OBJECT_IS_EMPTY("Insert batch parameterObject is empty"),
	UPDATE_BATCH_PARAMETER_OBJECT_IS_EMPTY("Update batch parameterObject is empty"),
	NO_TYPE_HANDLER_SUITABLE("There was no TypeHandler found for parameter "),
	WRONG_KEY_GENERATOR_TYPE("Find an error KeyGeneratorType in "),
	WRONG_CUSTOM_KEY_GENERATOR("Find an error custom key generator in "),
	WRONG_LOGGER_DESCRIPTION("Find an error logger description in "),
	CANNOT_FIND_ASSIGNED_DATA_SOURCE_IN_CONTEXT("Can not find the dataSource bean assigned in the context"),
	CANNOT_FIND_APPLICATION_CONTEXT_PROVIDER("Can not find the ApplicationContextProvider bean in the context");

	private final String description;

	private AutoMapperExceptionEnum(String description) {
		this.description = description;
	}

	public String description() {
		return description;
	}

	@Override
	public String toString() {
		return description;
	}
}
