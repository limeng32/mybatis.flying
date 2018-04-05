package indi.mybatis.flying.statics;

public enum ActionType {
	/** Number of queries, accept Pojo type parameters. */
	count,
	/** Delete a record, accept Pojo type parameters. */
	delete,
	/** Add a record, accept Pojo type parameters. */
	insert,
	/** Query a record, accept the primary key parameter. */
	select,
	/** Query multiple records, accept Pojo type parameters. */
	selectAll,
	/** Query a record, accept Pojo type parameters. */
	selectOne,
	/**
	 * Update a record, accept Pojo type parameters (ignoring attributes that
	 * are null in pojos)
	 */
	update,
	/**
	 * Completely updating a record, accept Pojo type parameters (does not
	 * ignore the null attribute in the Pojo, which is updated to null in the
	 * database.)
	 */
	updatePersistent
}
