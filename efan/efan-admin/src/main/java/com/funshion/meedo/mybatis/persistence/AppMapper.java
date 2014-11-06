package com.funshion.meedo.mybatis.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.funshion.meedo.module.operation.app.AppPagerForm;
import com.funshion.meedo.mybatis.domain.AppInfo;

public interface AppMapper {
	
	int countAllAppsByChannelId(@Param("pagerForm") AppPagerForm pagerForm ,@Param("channel_id") long channel_id);
	
	int countAllAppsPagination(@Param("pagerForm") AppPagerForm pagerForm);
	
	List<AppInfo> findAllAppsPagination(@Param("pagerForm") AppPagerForm pagerForm);
	
	int countAllAppsNotInChannelPagination(@Param("pagerForm") AppPagerForm pagerForm,@Param("channel_id") long channel_id);
	
	List<AppInfo> findAllAppsNotInChannelPagination(@Param("pagerForm") AppPagerForm pagerForm,@Param("channel_id") long channel_id);
	
	int countAppsInCatagoryPagination(@Param("pagerForm") AppPagerForm pagerForm,@Param("catagory") String catagory,@Param("channel_id") String channel_id);
	
	List<AppInfo> findAppsInCatagoryPagination(@Param("pagerForm") AppPagerForm pagerForm,@Param("catagory") String catagory,@Param("channel_id") String channel_id);
	
	List<AppInfo> findAllAppsByChannelId(@Param("pagerForm") AppPagerForm pagerForm,@Param("channel_id") long channel_id);
	
	void addApp(@Param("appInfo") AppInfo appInfo);
	
	AppInfo getLastestVerApp(@Param("pkgName") String pkgName);
	
	void updateLastestVer(@Param("pkgName") String pkgName, @Param("verCode") long verCode);
	
	void updateApp(@Param("appInfo") AppInfo appInfo);
	
	void publishAppById(@Param("id") long id, @Param("updateUser") long updateUser);
	
	void updateAppPublishState(@Param("appInfo") AppInfo appInfo, @Param("updateUser") long updateUser);
	
	void unpublishAppById(@Param("id") long id, @Param("updateUser") long updateUser);
	
	AppInfo getAppById(@Param("id") long id);
	
	void removeAppById(@Param("id") long id, @Param("updateUser") long updateUser);
	
	void addAppCatagory(@Param("appId") long appId, @Param("catagoryId") long catagoryId);
	void removeAppCatagory(@Param("appId") long appId);
	
	void addAppChannel(@Param("appId") long appId, @Param("channelId") long channelId);
	void removeAppChannel(@Param("appId") long appId);
	
	List<String> getAppCatagories(@Param("appId") long appId);
	List<String> getAppChannels(@Param("appId") long appId);
	String getAppSupportControllers(@Param("appId") long appId);
	
	//修复app url 
	void repaireAppUrl(@Param("appInfo") AppInfo appInfo);
	
	List<AppInfo> getAppAppsIsW();
	
	void updateAppPubStatus(@Param("appId") long appId, @Param("pubStatus") String pubStatus);
}
