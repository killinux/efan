package com.efan.mybatis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.efan.common.AbstractService;
import com.efan.common.GlobalConstant;
import com.efan.module.operation.catagory.CatagoryPagerForm;
import com.efan.mybatis.domain.CatagoryInfo;
import com.efan.mybatis.domain.UserInfo;
import com.efan.mybatis.persistence.CatagoryMapper;
import com.efan.util.AjaxResponseUtil.AjaxResponse;

@Service("catagoryService")
public class CatagoryService extends AbstractService {
	@Autowired
	private CatagoryMapper catagoryMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	/*
	 * 根据app_id查询分类，显示
	 */
	public String getCatagoriesByAppId(long app_id){
		String catagories = "";
		List<CatagoryInfo> catagoryList = catagoryMapper.getCatagorysByAppId(app_id);
		for(int i = 0;i<catagoryList.size();i++){
			catagories += catagoryList.get(i).getName();
			if(i < catagoryList.size() - 1) {
				catagories += ",";
			}
		}
		return catagories;
	}
	
	public List<CatagoryInfo> findAllCatagoriesPagination(CatagoryPagerForm pagerForm) {
		flushPageInfo(pagerForm, catagoryMapper.countAllCatagoriesPagination(pagerForm));
		if(pagerForm.getTotalCount() > 0) {
			pagerForm.setPageStart(getPageStart(pagerForm));
			List<CatagoryInfo> catagoryList = catagoryMapper.findAllCatagoriesPagination(pagerForm);
			for(CatagoryInfo catagory : catagoryList) {
				UserInfo createUserInfo = userService.getUserById(catagory.getCreateUser());
				if(createUserInfo != null) {
					catagory.setCreateUserName(createUserInfo.getDisplayName());
				}
				UserInfo updateUserInfo = userService.getUserById(catagory.getUpdateUser());
				if(updateUserInfo != null) {
					catagory.setUpdateUserName(updateUserInfo.getDisplayName());
				}
			}
			return catagoryList;
		} else {
			return new ArrayList<CatagoryInfo>();
		}
	}
	
	public AjaxResponse addCatagory(CatagoryInfo catagoryInfo, long createUser) {
		AjaxResponse ret = new AjaxResponse();
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			if(isExistedCatagory(catagoryInfo.getName())) {
				ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
				ret.setMessage("商品分类-添加商品分类失败【该分类已经存在】");
				return ret;
			}
			
			catagoryInfo.setCreateUser(createUser);
			catagoryMapper.addCatagory(catagoryInfo);
			
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("商品分类-添加商品分类成功！");
			
			ret.setCallbackType("closeCurrent");
			ret.setNavTabId("catagoryList");
			ret.setRel("catagoryList");
			ret.setForwardUrl("catagory/list");
			
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw new RuntimeException(e);
		}
		
		return ret;
	}
	
	public CatagoryInfo getCatagoryById(long id) {
		return catagoryMapper.getCatagoryById(id);
	}
	
	public AjaxResponse updateCatagory(CatagoryInfo catagoryInfo, long updateUser) {
		AjaxResponse ret = new AjaxResponse();
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			catagoryInfo.setUpdateUser(updateUser);
			catagoryMapper.updateCatagory(catagoryInfo);
			
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("商品分类-更新商品分类成功！");
			
			ret.setCallbackType("closeCurrent");
			ret.setNavTabId("catagoryList");
			ret.setRel("catagoryList");
			ret.setForwardUrl("catagory/list");
			
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw new RuntimeException(e);
		}
		
		return ret;	
	}
	
	public AjaxResponse removeCatagoryById(long id, long updateUser) {
		AjaxResponse ret = new AjaxResponse();
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			catagoryMapper.removeCatagoryById(id, updateUser);
			
			catagoryMapper.removeCatagoryAppRel(id);
			
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("商品分类-删除商品分类成功！");
			
			ret.setNavTabId("catagoryList");
			
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw new RuntimeException(e);
		}
		
		return ret;		
	}
	
	private boolean isExistedCatagory(String name) {
		CatagoryPagerForm pagerForm = new CatagoryPagerForm();
		pagerForm.setName(name);
		List<CatagoryInfo> catagoryList = catagoryMapper.findAllCatagoriesPagination(pagerForm);
		return catagoryList.size() > 0 ? true : false;
	}
	
	public List<CatagoryInfo> getAllValidCatagories() {
		return catagoryMapper.getAllValidCatagories();
	}
	
	public List<CatagoryInfo> getAllCatagories() {
		return catagoryMapper.getAllCatagories();
	}
}
