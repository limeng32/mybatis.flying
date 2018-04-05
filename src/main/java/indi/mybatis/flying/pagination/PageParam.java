package indi.mybatis.flying.pagination;

import java.io.Serializable;

import indi.mybatis.flying.models.Limitable;

public class PageParam implements Limitable, Serializable {

	private static final long serialVersionUID = 1L;

	public PageParam() {

	}

	public PageParam(int _pageNo, int _pageSize) {
		this.pageNo = _pageNo;
		this.pageSize = _pageSize;
	}

	private int pageNo;

	private int pageSize;

	/*
	 * The totalCount is not appropriate as part of the cache key, so it is
	 * declared transient.
	 */
	private transient int totalCount;

	/*
	 * The maxPageNum is not appropriate as part of the cache key, so it is
	 * declared transient.
	 */
	private transient int maxPageNum;

	@Override
	public int getPageNo() {
		return pageNo;
	}

	@Override
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

	@Override
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public int getLimitFrom() {
		return (pageNo - 1) * pageSize;
	}

	@Override
	public int getTotalCount() {
		return totalCount;
	}

	@Override
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	@Override
	public int getMaxPageNum() {
		maxPageNum = ((totalCount - 1) / pageSize) + 1;
		return maxPageNum;
	}

}
