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

	NO_TABLE_MAPPER_ANNOTATION(
			"The Class and all its parents has no 'TableMapperAnnotation', which has the database table information:"),
	AMBIGUOUS_CONDITION("Sorry, I cannot to build sql for an ambiguous condition"),
	NULL_OBJECT("Sorry, I cannot to build sql for a null object"),
	NULL_FIELD("Sorry, I cannot to build sql for a class which all fields are null"),
	UPDATE_UNIQUE_KEY_IS_NULL("Unique key can't be null, build update sql failed:"),
	UPDATE_PERSISTENT_UNIQUE_KEY_IS_NULL("Unique key can't be null, build updatePersistent sql failed:"),
	DELETE_UNIQUE_KEY_IS_NULL("Unique key can't be null, build delete sql failed:"),
	NO_FIELD_MAPPER_ANNOTATION_OR_COLUMN_ANNOTATION("The Field have no fieldMapperAnnotation or columnAnnotation"),
	THIS_CONDITION_NOT_SUPPORT_OR("The condition you use is not support OR"),
	UNKOWN_CONDITION_FOR_BATCH_PROCESS("condition that cannot be processed when batch processing is encountered");

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
