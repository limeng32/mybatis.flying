package indi.mybatis.flying.exception;

public enum BuildSqlExceptionEnum {

	noTableMapperAnnotation(
			"The Class and all its parents has no 'TableMapperAnnotation', which has the database table information:"), ambiguousCondition(
					"Sorry, I cannot to build sql for an ambiguous condition"), nullObject(
							"Sorry, I cannot to build sql for a null object"), nullField(
									"Sorry, I cannot to build sql for a class which all fields are null"), updateUniqueKeyIsNull(
											"Unique key can't be null, build update sql failed:"), updatePersistentUniqueKeyIsNull(
													"Unique key can't be null, build updatePersistent sql failed:"), deleteUniqueKeyIsNull(
															"Unique key can't be null, build delete sql failed:");

	private final String description;

	private BuildSqlExceptionEnum(String description) {
		this.description = description;
	}

	public String description() {
		return description;
	}

	public String toString() {
		return description;
	}
}
