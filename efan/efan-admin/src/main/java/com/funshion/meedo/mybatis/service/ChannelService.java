package com.funshion.meedo.mybatis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.funshion.meedo.common.AbstractService;
import com.funshion.meedo.common.GlobalConstant;
import com.funshion.meedo.module.operation.channel.ChannelPagerForm;
import com.funshion.meedo.module.user.UserPagerForm;
import com.funshion.meedo.mybatis.domain.ChannelInfo;
import com.funshion.meedo.mybatis.domain.UserInfo;
import com.funshion.meedo.mybatis.persistence.ChannelMapper;
import com.funshion.meedo.mybatis.persistence.UserMapper;
import com.funshion.meedo.util.AjaxResponseUtil.AjaxResponse;


@Service("channelService")
public class ChannelService extends AbstractService {
	@Autowired
	private ChannelMapper channelMapper;
	
	@Autowired
	private UserMapper userService;
	
	@Autowired
	private AppService appService;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	public List<ChannelInfo> findAllChannels(ChannelPagerForm pagerForm) {
		flushPageInfo(pagerForm, channelMapper.countAllChannels(pagerForm));
		//System.out.println("channelService-----service count");
		//System.out.println(channelMapper.countAllChannels(pagerForm));
		UserPagerForm userpagerForm =  new UserPagerForm();
		List<UserInfo> userList = userService.findAllUser(userpagerForm);
		Map<Long,String> userMap = new HashMap<Long,String>();
		for(int i= 0 ;i<userList.size();i++){
			if(userList.get(i).getDisplayName()!=null){
				userMap.put(userList.get(i).getId(), userList.get(i).getDisplayName());
				//System.out.println(userList.get(i).getId()+":"+ userList.get(i).getDisplayName());
			}else{
				userMap.put(userList.get(i).getId(),"未定义用户");
				//System.out.println(userList.get(i).getId()+":"+"未定义用户");
			}
		}
		if(pagerForm.getTotalCount() > 0) {
			pagerForm.setPageStart(getPageStart(pagerForm));
			List<ChannelInfo> channelList = channelMapper.findAllChannels(pagerForm);
			for(int ii= 0;ii<channelList.size();ii++){
				channelList.get(ii).setCreateUserName(userMap.get(channelList.get(ii).getCreateUser()));
				channelList.get(ii).setUpdateUserName(userMap.get(channelList.get(ii).getUpdateUser()));
			}
			//System.out.println("channel list count:"+channelList.size());
			return channelList;
			
		} else {
			return new ArrayList<ChannelInfo>(0);
		}
	}
	public AjaxResponse addChannel(ChannelInfo channelInfo, long createUser) {
		AjaxResponse ret = new AjaxResponse();
		//TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			if(isExistedChannel(channelInfo.getName())) {
				ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
				ret.setMessage("频道管理-添加频道类失败【该频道已经存在】");
				return ret;
			}
			System.out.println("createUser:"+createUser);
			channelInfo.setUpdateUser(createUser);
			channelInfo.setCreateUser(createUser);
			channelMapper.addChannel(channelInfo);
			
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("频道管理-添加频道成功！");
			
			ret.setCallbackType("closeCurrent");//todo haohao ??
			ret.setNavTabId("channelList");
			ret.setRel("channelList");
			ret.setForwardUrl("channel/list");
			
			//transactionManager.commit(status);
		} catch (Exception e) {
			//transactionManager.rollback(status);
			throw new RuntimeException(e);
		}
		
		return ret;
	}
	/*
	 * 判断是否存在
	 */
	private boolean isExistedChannel(String name) {
		ChannelPagerForm pagerForm = new ChannelPagerForm();
		pagerForm.setName(name);
		List<ChannelInfo> catagoryList = channelMapper.findAllChannels(pagerForm);
		return catagoryList.size() > 0 ? true : false;
	}
	public ChannelInfo getChannelById(long id) {
		return channelMapper.getChannelById(id);
	}
	
	public AjaxResponse updateChannelById(ChannelInfo channelInfo, long updateUser) {
		AjaxResponse ret = new AjaxResponse();
		//TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			/*
			 * 前段禁用，否则会更改成其他的用户名
			 */
//			if(isExistedChannel(channelInfo.getName())) {
//				ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
//				ret.setMessage("频道管理-添加频道类失败【该频道已经存在】");
//				return ret;
//			}
			channelInfo.setUpdateUser(updateUser);
			channelMapper.updateChannelById(channelInfo);
			
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("频道管理-更新频道成功！");
			
			ret.setCallbackType("closeCurrent");
			ret.setNavTabId("channelList");
			ret.setRel("channelList");
			ret.setForwardUrl("channel/list");
			
			//transactionManager.commit(status);
		} catch (Exception e) {
			//transactionManager.rollback(status);
			throw new RuntimeException(e);
		}
		return ret;
	}
	public AjaxResponse batchAddAppToChannel(String[] appIds, String channel_id) {
		AjaxResponse ret = new AjaxResponse();
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			for(int i=0 ;i< appIds.length;i++){
				System.out.println("batchAddAppToChannel:"+appIds[i]);
				channelMapper.addChannelAppRel(appIds[i],channel_id);
			}
			
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("频道管理--数据管理-批量增加App成功！");
			
			ret.setCallbackType("closeCurrent");
			ret.setNavTabId("managerChannel");
			ret.setRel("managerChannel");
			ret.setForwardUrl("channel/manager/"+channel_id);
			
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw new RuntimeException(e);
		}
		
		return ret;
	}
	
	public AjaxResponse removeChannelById(long id, long updateUser) {
		AjaxResponse ret = new AjaxResponse();
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			channelMapper.removeChannelById(id, updateUser);

			channelMapper.removeChannelAppRel(id);
			
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("频道管理-删除频道成功！");
			
			//ret.setCallbackType("closeCurrent");
			ret.setNavTabId("channelList");
			ret.setRel("channelList");
			ret.setForwardUrl("channel/list");
			
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw new RuntimeException(e);
		}
		
		return ret;
	}
	
	public AjaxResponse removeAppFromChannel(long channel_id,long app_id) {
		AjaxResponse ret = new AjaxResponse();
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			channelMapper.removeAppFromChannel(channel_id, app_id);
			
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("频道-数据管理-删除频道中的应用成功！");
			
			//ret.setCallbackType("closeCurrent");
			ret.setNavTabId("managerChannel");
			ret.setRel("managerChannel");
			ret.setForwardUrl("channel/manager/"+channel_id);//hao
			
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw new RuntimeException(e);
		}
		
		return ret;
	}
	public AjaxResponse updateAppWeightByChannelIdAndAppId(long channel_id,long app_id,long app_weight) {
		//System.out.println("---------updateAppWeightByChannelIdAndAppId:"+channel_id);
		AjaxResponse ret = new AjaxResponse();
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			channelMapper.updateAppWeightByChannelIdAndAppId( channel_id,app_id,app_weight);
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("频道-数据管理-修改app在频道中的权重成功！");
			
			

			ret.setNavTabId("managerChannel");
			ret.setRel("managerChannel");
			ret.setForwardUrl("channel/manager/"+channel_id);
			ret.setCallbackType("closeCurrent");
			System.out.println(ret.getStatusCode());
			System.out.println(ret.getNavTabId());
			System.out.println(ret.getRel());
			System.out.println(ret.getForwardUrl());
			transactionManager.commit(status);
		} catch (Exception e) {
			System.out.println("updateAppWeightByChannelIdAndAppId---error "+e.getMessage());
			transactionManager.rollback(status);
			throw new RuntimeException(e);
		}
		
		return ret;
	}

	public List<ChannelInfo> getAllValidChannels() {
		return channelMapper.getAllValidChannels();
	}
	
	public List<ChannelInfo> getAllChannels() {
		return channelMapper.getAllChannels();
	}
	
	public String getChannelsByAppId(long appId) {
		String channels = "";
		List<String>  channelList = channelMapper.getChannelsByAppId(appId);
		for(int i = 0; i < channelList.size(); i ++) {
			channels += channelList.get(i);
			if(i < channelList.size() - 1) {
				channels += ",";
			}
		}
		return channels;
	}
}
