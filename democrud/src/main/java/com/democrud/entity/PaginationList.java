package com.democrud.entity;

import java.util.List;

public class PaginationList {

	private Integer page;
	private Integer size;
	private String sortBy;
	private String sortOrder;

	private List<SearchFilter> filters;
	//    private SearchFilter [] searchFilters; //object array banaya hai 


	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public List<SearchFilter> getFilters() {
		return filters;
	}
	public void setFilters(List<SearchFilter> filters) {
		this.filters = filters;
	}
	



}

