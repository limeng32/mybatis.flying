package indi.mybatis.flying.statics;

public enum ActionType {
	/** 查询数量，接受Pojo型参数 */
	count,
	/** 删除一条记录，接受Pojo型参数 */
	delete,
	/** 增加一条记录，接受Pojo型参数 */
	insert,
	/** 查询一条记录，接受主键型参数 */
	select,
	/** 查询多条记录，接受Pojo型参数 */
	selectAll,
	/** 查询一条记录，接受Pojo型参数 */
	selectOne,
	/** 更新一条记录，接受Pojo型参数（忽视Pojo中为null的属性） */
	update,
	/** 完全更新一条记录，接受Pojo型参数（不忽视Pojo中为null的属性，将为null属性在数据库中更新为null） */
	updatePersistent
}
