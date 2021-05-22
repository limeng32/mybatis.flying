package indi.mybatis.flying.statics;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public enum ConditionType {
	/** Equal */
	EQUAL,
	/** Like matching '%XXX%' */
	LIKE,
	/** Like match 'XXX%' */
	HEAD_LIKE,
	/** Like match '%XXX' */
	TAIL_LIKE,
	/** Not Like matching */
	NOT_LIKE,
	/** Not Like match 'XXX%' */
	NOT_HEAD_LIKE,
	/** Not Like match '%XXX' */
	NOT_TAIL_LIKE,
	/**
	 * Multiple like condition matching, and relationship, this operation cannot be
	 * used under Or annotation.
	 */
	MULTI_LIKE_AND,
	/**
	 * Multiple like condition matching, or relationship, cannot be used under Or
	 * annotation.
	 */
	MULTI_LIKE_OR,
	/** Is greater than */
	GREATER_THAN,
	/** Greater than or equal to */
	GREATER_OR_EQUAL,
	/** Less than */
	LESS_THAN,
	/** Less than or equal to */
	LESS_OR_EQUAL,
	/** Is not equal to */
	NOT_EQUAL,
	/** Belong, this condition cannot be used under Or annotation. */
	IN,
	/** Nor belong, this condition cannot be used under Or annotation. */
	NOT_IN,
	/** Null or not null */
	NULL_OR_NOT
}
