package indi.mybatis.flying.statics;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public enum ActionType {
	/** Number of queries, accept Pojo type parameters. */
	COUNT("count"),
	/** Delete a record, accept Pojo type parameters. */
	DELETE("delete"),
	/** Add a record, accept Pojo type parameters. */
	INSERT("insert"),
	/** Query a record, accept the primary key parameter. */
	SELECT("select"),
	/** Query multiple records, accept Pojo type parameters. */
	SELECT_ALL("selectAll"),
	/** Query a record, accept Pojo type parameters. */
	SELECT_ONE("selectOne"),
	/**
	 * Update a record, accept Pojo type parameters (ignoring attributes that are
	 * null in pojos)
	 */
	UPDATE("update"),
	/**
	 * Completely updating a record, accept Pojo type parameters (does not ignore
	 * the null attribute in the Pojo, which is updated to null in the database.)
	 */
	UPDATE_PERSISTENT("updatePersistent");

	private final String value;

	private ActionType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	private static final Map<String, ActionType> mapForValue = new HashMap<>(8);

	static {
		ActionType[] values = ActionType.values();
		for (ActionType e : values) {
			mapForValue.put(e.value(), e);
		}
	}

	public static ActionType forValue(String value) {
		return mapForValue.get(value);
	}
}
