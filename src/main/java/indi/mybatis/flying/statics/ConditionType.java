package indi.mybatis.flying.statics;

public enum ConditionType {
	/** Equal */
	Equal,
	/** Like matching */
	Like,
	/** Start like match */
	HeadLike,
	/** End like match */
	TailLike,
	/**
	 * Multiple like condition matching, and relationship, this operation cannot
	 * be used under Or annotation.
	 */
	MultiLikeAND,
	/**
	 * Multiple like condition matching, or relationship, cannot be used under
	 * Or annotation.
	 */
	MultiLikeOR,
	/** Is greater than */
	GreaterThan,
	/** Greater than or equal to */
	GreaterOrEqual,
	/** Less than */
	LessThan,
	/** Less than or equal to */
	LessOrEqual,
	/** Is not equal to */
	NotEqual,
	/** Belong, this condition cannot be used under Or annotation. */
	In,
	/** Nor belong, this condition cannot be used under Or annotation. */
	NotIn,
	/** Null or not null */
	NullOrNot
}
