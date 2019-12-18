package indi.mybatis.flying.exception;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public enum BuildSqlExceptionEnum {

	noTableMapperAnnotation(
			"The Class and all its parents has no 'TableMapperAnnotation', which has the database table information:"),
	ambiguousCondition("Sorry, I cannot to build sql for an ambiguous condition"),
	nullObject("Sorry, I cannot to build sql for a null object"),
	nullField("Sorry, I cannot to build sql for a class which all fields are null"),
	updateUniqueKeyIsNull("Unique key can't be null, build update sql failed:"),
	updatePersistentUniqueKeyIsNull("Unique key can't be null, build updatePersistent sql failed:"),
	deleteUniqueKeyIsNull("Unique key can't be null, build delete sql failed:"),
	noFieldMapperAnnotationOrColumnAnnotation("The Field have no fieldMapperAnnotation or columnAnnotation"),
	ThisConditionNotSupportOr("The condition you use is not support OR"),
	unkownConditionForBatchProcess("condition that cannot be processed when batch processing is encountered");

	private final String description;

	private BuildSqlExceptionEnum(String description) {
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
