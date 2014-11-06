package com.efan.mybatis.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.efan.module.operation.channel.ChannelPagerForm;
import com.efan.mybatis.domain.ChannelInfo;


public interface ChannelMapper {
	int countAllChannels(
			@Param(value="pagerForm") ChannelPagerForm pagerForm);
	
	List<ChannelInfo> findAllChannels(
			@Param(value="pagerForm") ChannelPagerForm pagerForm);
	
	void addChannel(
			@Param(value="channelInfo") ChannelInfo channelInfo);
	
	ChannelInfo getChannelById(
			@Param(value="id") long id);
	
	void updateChannelById(
			@Param(value="channelInfo") ChannelInfo channelInfo);
	
	void removeChannelById(
		@Param(value="id") long id, @Param(value="updateUser") long updateUser);
	

	void removeChannelAppRel(@Param(value="id") long id);
	
	void updateAppWeightByChannelIdAndAppId( @Param(value="channel_id") long channel_id, @Param(value="app_id") long app_id,@Param(value="app_weight") long app_weight);
	
	void removeAppFromChannel(@Param(value="channel_id") long channel_id, @Param(value="app_id") long app_id);
	void addChannelAppRel(@Param(value="app_id") String app_id, @Param(value="channel_id") String channel_id);
	/*	
//	List<ChannelInfo> getChannelByDevId(
//			@Param(value="devId") String devId);
	
	void activeChannelById(
			@Param(value="id") long id);*/
	
	List<ChannelInfo> getAllValidChannels();
	List<ChannelInfo> getAllChannels();
	
	List<String> getChannelsByAppId(@Param("appId") long appId);
}
