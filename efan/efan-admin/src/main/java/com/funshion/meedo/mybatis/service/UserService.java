package com.funshion.meedo.mybatis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.funshion.meedo.module.user.UserPagerForm;
import com.funshion.meedo.common.AbstractService;
import com.funshion.meedo.common.GlobalConstant;
import com.funshion.meedo.mybatis.domain.UserInfo;
import com.funshion.meedo.mybatis.persistence.UserMapper;
import com.funshion.meedo.util.AjaxResponseUtil.AjaxResponse;
import com.funshion.meedo.util.MD5Util;

@Service("userService")
public class UserService extends AbstractService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	public List<UserInfo> findAllUser(UserPagerForm pagerForm) {
		this.flushPageInfo(pagerForm, userMapper.countUserPagination(pagerForm));
		
		if (pagerForm.getTotalCount() > 0) {
			List<UserInfo> list =  userMapper.findAllUser(pagerForm);
			return list;
		} else {
			return new ArrayList<UserInfo>(0);
		}
	}
	
	public AjaxResponse addUser(UserInfo userInfo, long createUser) {
		AjaxResponse ret = new AjaxResponse();
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			userInfo.setCreateUser(createUser);
			userInfo.setPassword(MD5Util.getStringMD5String(userInfo.getPassword()));
			userMapper.addUser(userInfo);		
			
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("用户管理-添加用户成功！");
			
			ret.setCallbackType("closeCurrent");
			ret.setNavTabId("userList");
			ret.setRel("userList");
			ret.setForwardUrl("user/list");
			
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw new RuntimeException(e);
		}
		
		return ret;
	}
	
	public AjaxResponse updateUserById(UserInfo userInfo, long updateUser) {
		AjaxResponse ret = new AjaxResponse();
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			userInfo.setUpdateUser(updateUser);
			userMapper.updateUserById(userInfo);		
			
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("用户管理-更新用户成功！");
			
			ret.setCallbackType("closeCurrent");
			ret.setNavTabId("userList");
			ret.setRel("userList");
			ret.setForwardUrl("user/list");
			
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw new RuntimeException(e);
		}
		
		return ret;
	}
	
	public void checkUser(String userName, String password) {
		
	}
	
	public void deleteUser(String userName) {
		
	}
	
	public UserInfo getUserByLoginName(String userName) {
		return userMapper.getUserByLoginName(userName);
	}
	
	public UserInfo getUserById(long id) {
		return userMapper.getUserById(id);
	}
}
