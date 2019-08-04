package indi.mybatis.flying.pagination;

import java.util.Collection;

import indi.mybatis.flying.models.Limitable;

public class Page<T> {

	private int pageNo;

	private int maxPageNum;

	private int totalCount;

	private Collection<T> pageItems;

	private int pageSize;

	public Page(Collection<T> items, Limitable limitable) {
		pageItems = items;
		pageNo = limitable.getPageNo();
		maxPageNum = limitable.getMaxPageNum();
		totalCount = limitable.getTotalCount();
		pageSize = limitable.getPageSize();
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getMaxPageNum() {
		return maxPageNum;
	}

	public void setMaxPageNum(int maxPageNum) {
		this.maxPageNum = maxPageNum;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public Collection<T> getPageItems() {
		return pageItems;
	}

	public void setPageItems(Collection<T> pageItems) {
		this.pageItems = pageItems;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
