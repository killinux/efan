package com.funshion.meedo.common;

public abstract class AbstractPagerForm {
	/** 当前第几页 */
	private int pageNum = 1;
	/** 每页多少行 */
	private int numPerPage = 50;
	/** 一共多少条数据 */
	private int totalCount = 0;
	/** 最多多少个可以快捷点击的页数 */
	private int pageNumShown = 5;
	/** 总共多少页 */
	private int totalPageCount = 0;
	/** 排序的字段 */
	private String orderField = "";
	
	/** 在分页查询时，用于limit的参数: limit #{pageStart}, #{numPerPage} */
	private int pageStart = 0;
	/**  排序的方向 */
	private String orderDirection = GlobalConstant.OrderDirection.DESC;
	
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getNumPerPage() {
		return numPerPage;
	}
	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getPageNumShown() {
		return pageNumShown;
	}
	public void setPageNumShown(int pageNumShown) {
		this.pageNumShown = pageNumShown;
	}
	public int getTotalPageCount() {
		return totalPageCount;
	}
	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	public String getOrderField() {
		return orderField;
	}
	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}
	public int getPageStart() {
		return pageStart;
	}
	public void setPageStart(int pageStart) {
		this.pageStart = pageStart;
	}
	public String getOrderDirection() {
		return orderDirection;
	}
	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}
}
