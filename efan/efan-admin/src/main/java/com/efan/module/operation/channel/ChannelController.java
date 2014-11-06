package com.efan.module.operation.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.efan.common.AbstractController;
import com.efan.common.GlobalConstant;
import com.efan.module.operation.app.AppPagerForm;
import com.efan.module.operation.catagory.CatagoryPagerForm;
import com.efan.mybatis.domain.AppInfo;
import com.efan.mybatis.domain.CatagoryInfo;
import com.efan.mybatis.domain.ChannelInfo;
import com.efan.mybatis.service.AppService;
import com.efan.mybatis.service.CatagoryService;
import com.efan.mybatis.service.ChannelService;
import com.efan.util.AjaxResponseUtil;
import com.efan.util.AjaxResponseUtil.AjaxResponse;

@Controller
@RequestMapping(value="/channel")
public class ChannelController extends AbstractController {
	@Autowired
	private ChannelService channelService;
	@Autowired
	private AppService appService;
	@Autowired
	private CatagoryService catagoryService;
	
	
	
	@RequestMapping(value="/list")
	public String listChannel(Map<String, Object> map) {
		ChannelPagerForm pagerForm = new ChannelPagerForm();
		List<ChannelInfo> channelList = channelService.findAllChannels(pagerForm);
		map.put("pagerForm", pagerForm);
		map.put("list", channelList);
		return "/jsp/operation/channel/channel_list.jsp";
	}
	@RequestMapping(value="/search")
	public String searchChannel(ChannelPagerForm pagerForm, Map<String, Object> map) {
		System.out.println("search:"+pagerForm.getName());
		List<ChannelInfo> catagoryList = channelService.findAllChannels(pagerForm);
		map.put("pagerForm", pagerForm);
		map.put("list", catagoryList);
		map.put("searchName", pagerForm.getName());
		
		return "/jsp/operation/channel/channel_list.jsp";
	}
	
	@RequestMapping(value="/listallapp/{channel_id}")
	public String listallapp(@PathVariable("channel_id") long channel_id,Map<String, Object> map) {
		//System.out.println("listallapp-----"+channel_id);
		AppPagerForm appPagerForm = new AppPagerForm();
		//List<AppInfo> appList = appService.findAllAppsPagination(pagerForm);
		List<AppInfo> appList = appService.findAllAppsNotInChannelPagination(appPagerForm,channel_id);	
		for(int ii=0;ii<appList.size();ii++){
			AppInfo appInfo = appList.get(ii);
			appList.get(ii).setCatagories(catagoryService.getCatagoriesByAppId(appInfo.getId()));
		}
		CatagoryPagerForm catagoryPagerForm = new CatagoryPagerForm();
		List<CatagoryInfo> catagorylist = catagoryService.findAllCatagoriesPagination(catagoryPagerForm);	
		System.out.println(appPagerForm+"--listallapp--- "+appPagerForm.getPageNum());
		map.put("pagerForm", appPagerForm);
		map.put("list", appList);
		map.put("catagorylist", catagorylist);
		map.put("channel_id", channel_id);
		map.put("app_name", "");
		map.put("catagory", "0");
		System.out.println("ChannelController--listallapp");
		return "/jsp/operation/channel/add_app_list.jsp";
	}
	
	@RequestMapping(value="/searchapp/{channel_id}")
	public String searchapp(AppPagerForm pagerForm,@PathVariable("channel_id") long channel_id, Map<String, Object> map, HttpServletRequest request) {
//		System.out.println("----------------pagerForm:"+pagerForm+" "+pagerForm.getNumPerPage());
//		System.out.println("searchapp:::::"+pagerForm.getName());
		String catagory = request.getParameter("catagory");
		List<AppInfo> appList = new ArrayList<AppInfo>();
		if("0".equals(catagory)){
			//appList = appService.findAllAppsPagination(pagerForm);
			appList = appService.findAllAppsNotInChannelPagination(pagerForm,channel_id);
		}else{
			appList = appService.findAppsInCatagoryPagination(pagerForm,catagory,channel_id+"");
		}
		for(int ii=0;ii<appList.size();ii++){
			AppInfo appInfo = appList.get(ii);
			appList.get(ii).setCatagories(catagoryService.getCatagoriesByAppId(appInfo.getId()));
		}
		
		CatagoryPagerForm catagoryPagerForm = new CatagoryPagerForm();//分类
		List<CatagoryInfo> catagorylist = catagoryService.findAllCatagoriesPagination(catagoryPagerForm);
		
		map.put("list", appList);
		map.put("catagorylist", catagorylist);
		map.put("pagerForm", pagerForm);
		map.put("channel_id", channel_id);
		map.put("app_name", pagerForm.getName());
		map.put("catagory", catagory);
		pagerForm.setCatagory(Long.valueOf(catagory).longValue());
		map.put("pagerForm", pagerForm);
		return "/jsp/operation/channel/add_app_list.jsp";
		
	}
	
/*	@RequestMapping(value="/search")
	public String searchDevice(ChannelPagerForm pagerForm, Map<String, Object> map) {
		List<AuthDeviceInfo> deviceList = authDeviceService.findAllAuthDevices(pagerForm);
		map.put("pagerForm", pagerForm);
		map.put("list", deviceList);
		return "/jsp/auth_device/device_list.jsp";
	}*/
	
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public String addChannelGet() {
		return "/jsp/operation/channel/channel_add.jsp";
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public void addChannelPost(ChannelInfo channelInfo, HttpServletResponse response, HttpSession session) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			ret = channelService.addChannel(channelInfo, getLoginUserId(session));
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	@RequestMapping(value="/update/{id}", method=RequestMethod.GET)
	public String updateChannelGet(@PathVariable("id") long id, Map<String, Object> map) {
		ChannelInfo authDevice = channelService.getChannelById(id);
		map.put("entity", authDevice);
		return "/jsp/operation/channel/channel_update.jsp";
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public void updateChannelPost(ChannelInfo channelInfo, HttpSession session, HttpServletResponse response) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			ret = channelService.updateChannelById(channelInfo, getLoginUserId(session));
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}	
	}
	
	@RequestMapping(value="/remove/{id}")
	public void removeChannel(@PathVariable("id") long id, HttpSession session, HttpServletResponse response) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			ret = channelService.removeChannelById(id, getLoginUserId(session));
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}	
	}
	
	@RequestMapping(value="/manager/{id}")
	public String managerDataGet(AppPagerForm pagerForm,@PathVariable("id") long id, Map<String, Object> map) {
//		ChannelInfo authDevice = channelService.getChannelById(id);
		//AppPagerForm pagerForm = new AppPagerForm();
		List<AppInfo> appList = appService.findAppByChannelId(pagerForm,id);
		for(int ii=0;ii<appList.size();ii++){
			AppInfo appInfo = appList.get(ii);
			appList.get(ii).setCatagories(catagoryService.getCatagoriesByAppId(appInfo.getId()));
		}
		String channel_name = channelService.getChannelById(id).getName();
		map.put("pagerForm", pagerForm);
		map.put("channel_id", id);
		map.put("channel_name", channel_name);
		map.put("list", appList);
		return "/jsp/operation/channel/manage_data_list.jsp";
	}
	
	
	@RequestMapping(value="/managerUpdateAppWeight/{channel_id}/{app_id}/{app_weight}", method=RequestMethod.GET)
	public String managerUpdateAppWeight(@PathVariable("channel_id") long channel_id,@PathVariable("app_id") long app_id,@PathVariable("app_weight") long app_weight,  Map<String, Object> map) {
		AppInfo app = appService.getAppById(app_id);
		map.put("entity", app);
		map.put("channel_id", channel_id);
		map.put("app_weight", app_weight);
		
		return "/jsp/operation/channel/channel_update_app_weight.jsp";
	}
	/*
	 * 修改权重 
	 */
	@RequestMapping(value="/managerUpdateAppWeight" , method=RequestMethod.POST)
	public void managerUpdateAppWeight(AppInfo appInfo, HttpSession session, HttpServletResponse response) {
		System.out.println("-ChannelController---managerUpdateAppWeight");
		System.out.println("appInfo.getName():"+appInfo.getName());
		System.out.println("appInfo.getId():"+appInfo.getId());
		System.out.println("appInfo.getOrder():"+appInfo.getWeight());
		long channel_id = Long.valueOf(appInfo.getName()).longValue();//todo这里要验证不是long的情况，或者前段验证
		long app_id = appInfo.getId();
		long app_weight = appInfo.getWeight();
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			ret = channelService.updateAppWeightByChannelIdAndAppId( channel_id,app_id, app_weight);
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
//			System.out.println("---------managerUpdateAppWeight-----------");
//			System.out.println(ret.getStatusCode());
//			System.out.println(ret.getNavTabId());
//			
//			System.out.println(ret.getRel());
//			System.out.println(ret.getForwardUrl());
			AjaxResponseUtil.responseJsonObject(response, ret);
		}	
	}
	@RequestMapping(value="/removeAppFromChannel/{channel_id}/{app_id}")
	public void removeAppFromChannel(@PathVariable("channel_id") long channel_id,@PathVariable("app_id") long app_id, HttpSession session, HttpServletResponse response) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			ret = channelService.removeAppFromChannel(channel_id,app_id);
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}	
	}
	@RequestMapping(value="/batchAddAppToChannel", method=RequestMethod.POST)
	public void batchAddAppToChannel( HttpSession session, HttpServletResponse response, HttpServletRequest request) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		System.out.println("-ChannelController---batchaddAppToChannel");
		String[] appIds = request.getParameterValues("app_add_id");
		String channel_id = request.getParameter("channel_id");
		if(appIds==null){
			ret = new AjaxResponse();
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
			ret.setMessage("用户管理--数据管理--添加商品--请您选择App！");
			//ret.setNavTabId("managerChannel");
			ret.setRel("listAllAppForChannel");
			ret.setForwardUrl("channel/listallapp/"+channel_id);
			
			AjaxResponseUtil.responseJsonObject(response, ret);
		}else{
			for(int i=0 ;i< appIds.length;i++){
				System.out.println(appIds[i]);
			}
			
			System.out.println("channel_id:"+channel_id);

			try {
				ret = channelService.batchAddAppToChannel( appIds, channel_id);
			} catch (Exception e) {
				this.genAjaxErrorInfo(ret, e);
			} finally {
				AjaxResponseUtil.responseJsonObject(response, ret);
			}	
		}
		
	}
}