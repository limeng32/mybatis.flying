package indi.mybatis.flying.models;

public interface Limitable {

	/* 当前页数 */
	public int getPageNo();

	public void setPageNo(int pageNo);

	/* 每页容量 */
	public int getPageSize();

	public void setPageSize(int pageSize);

	/* 所有满足查询条件的记录总数，此数值建议由插件自动获取。 */
	public int getTotalCount();

	public void setTotalCount(int totalCount);

	/* 待取结果集中第一条记录在表中所处位置，此数值经计算得出 */
	public int getLimitFrom();

	/* 最大页数，此数值经计算得出 */
	public int getMaxPageNum();
}
