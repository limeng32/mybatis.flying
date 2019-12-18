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

	parameterObjectIsNull("ParameterObject is null"), dialectPropertyCannotFound("Dialect property is not found"),
	noTypeHandlerSuitable("There was no TypeHandler found for parameter "),
	wrongKeyGeneratorType("Find an error KeyGeneratorType in "),
	wrongCustomKeyGenerator("Find an error custom key generator in "),
	cannotFindAssignedDataSourceInContext("Can not find the dataSource bean assigned in the context"),
	cannotFindApplicationContextProvider("Can not find the ApplicationContextProvider bean in the context");

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
