package com.efan.mybatis.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.efan.module.operation.catagory.CatagoryPagerForm;
import com.efan.mybatis.domain.CatagoryInfo;

public interface CatagoryMapper {
	
	int countAllCatagoriesPagination(@Param("pagerForm") CatagoryPagerForm pagerForm);
	
	List<CatagoryInfo> findAllCatagoriesPagination(@Param("pagerForm") CatagoryPagerForm pagerForm);
	
	void addCatagory(@Param("catagoryInfo") CatagoryInfo catagoryInfo);
	
	void updateCatagory(@Param("catagoryInfo") CatagoryInfo catagoryInfo);
	
	CatagoryInfo getCatagoryById(@Param("id") long id);
	
	void removeCatagoryById(@Param("id") long id, @Param("updateUser") long updateUser);
	
	void removeCatagoryAppRel(@Param("id") long id);

	List<CatagoryInfo> getCatagorysByAppId(@Param("app_id") long app_id);

	List<CatagoryInfo> getAllValidCatagories();
	
	List<CatagoryInfo> getAllCatagories();
 
}
