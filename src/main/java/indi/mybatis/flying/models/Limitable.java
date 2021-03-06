package indi.mybatis.flying.models;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public interface Limitable {

	/* The current number of pages */
	public int getPageNo();

	public void setPageNo(int pageNo);

	/* Capacity of each page */
	public int getPageSize();

	public void setPageSize(int pageSize);

	/*
	 * Total number of records that satisfy the query criteria, This value is
	 * automatically obtained by the plug-in.
	 */
	public int getTotalCount();

	public void setTotalCount(int totalCount);

	/*
	 * The first record of the result set is recorded in the table, this value is
	 * calculated by calculation.
	 */
	public int getLimitFrom();

	/*
	 * The largest number of pages, this value is calculated by calculation.
	 */
	public int getMaxPageNum();
}
