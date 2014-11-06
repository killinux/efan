package com.funshion.meedo.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	/** 得到分页的开始位置 */
	protected int getPageStart(AbstractPagerForm pagerForm){
		return (pagerForm.getPageNum() - 1) * pagerForm.getNumPerPage();
	}
	
	/** 刷新分页相关信息 */
	protected void flushPageInfo(AbstractPagerForm pagerForm, int totalCount) {
		pagerForm.setTotalCount(totalCount);
		int totalPageCount = totalCount / pagerForm.getNumPerPage();
		if (totalCount % pagerForm.getNumPerPage() != 0) {
			totalPageCount++;
		}
		pagerForm.setTotalPageCount(totalPageCount);
		
		if (pagerForm.getPageNum() > totalPageCount) {
			pagerForm.setPageNum(totalPageCount);
		}
		
		if (pagerForm.getPageNum() < 1) {
			pagerForm.setPageNum(1);
		}
	}	
}
