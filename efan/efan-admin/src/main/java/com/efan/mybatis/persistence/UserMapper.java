package com.efan.mybatis.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.efan.module.user.UserPagerForm;
import com.efan.mybatis.domain.UserInfo;

public interface UserMapper {
	public int countUserPagination(
			@Param(value="pagerForm") UserPagerForm pagerForm);
	
	public List<UserInfo> findAllUser(
			@Param(value="pagerForm") UserPagerForm pagerFrom);
	
	public UserInfo getUserByLoginName(
			@Param(value="loginName") String loginName);
	
	public UserInfo getUserById(
			@Param(value="id") long id);
	
	public void updateUserById(
			@Param(value="userInfo") UserInfo userInfo);
	
	public void addUser(
			@Param(value="userInfo") UserInfo userInfo);
	
}
