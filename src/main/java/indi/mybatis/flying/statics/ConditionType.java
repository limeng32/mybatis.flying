package indi.mybatis.flying.statics;

public enum ConditionType {
	/** 相等 */
	Equal,
	/** like匹配 */
	Like,
	/** 开头like匹配 */
	HeadLike,
	/** 结尾like匹配 */
	TailLike,
	/** 多like条件匹配 ，与关系，此操作无法在Or标签下使用 */
	MultiLikeAND,
	/** 多like条件匹配 ，或关系，此操作无法在Or标签下使用 */
	MultiLikeOR,
	/** 大于 */
	GreaterThan,
	/** 大于或等于 */
	GreaterOrEqual,
	/** 小于 */
	LessThan,
	/** 小于或等于 */
	LessOrEqual,
	/** 不等于 */
	NotEqual,
	/** 属于，此操作无法在Or标签下使用 */
	In,
	/** 不属于，此操作无法在Or标签下使用 */
	NotIn,
	/** 为null或不为null */
	NullOrNot
}
