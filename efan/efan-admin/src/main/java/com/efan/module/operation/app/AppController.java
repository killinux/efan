package com.efan.module.operation.app;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.efan.common.AbstractController;
import com.efan.common.GlobalConstant;
import com.efan.common.GlobalConstant.AjaxResponseStatusCode;
import com.efan.mybatis.domain.AppInfo;
import com.efan.mybatis.domain.CatagoryInfo;
import com.efan.mybatis.domain.ChannelInfo;
import com.efan.mybatis.service.AppService;
import com.efan.mybatis.service.CatagoryService;
import com.efan.mybatis.service.ChannelService;
import com.efan.util.AjaxResponseUtil;
import com.efan.util.AjaxResponseUtil.AjaxResponse;
import com.efan.util.ApkInfo;
import com.efan.util.ApkTool;
import com.efan.util.ImgTool;
import com.efan.util.RandomUUID;

@Controller
@RequestMapping(value="/app")
public class AppController extends AbstractController {
	@Autowired
	private AppService appService;
	@Autowired
	private CatagoryService catagoryService;
	@Autowired
	private ChannelService channelService;
	
	@RequestMapping(value="/list")
	public String getAppList(Map<String, Object> map) {
		AppPagerForm pagerForm = new AppPagerForm();
		pagerForm.setOrderField("id");
		generateCommonPage(pagerForm, map);
		return "/jsp/operation/app/app_list.jsp";
	}
	
	@RequestMapping(value="search")
	public String searchApp(AppPagerForm pagerForm, Map<String, Object> map) {
		generateCommonPage(pagerForm, map);
		
		return "/jsp/operation/app/app_list.jsp";
	}
	
	public void generateCommonPage(AppPagerForm pagerForm, Map<String, Object> map) {
		List<AppInfo> appList = appService.findAllAppsPagination(pagerForm);
		
		for(int ii=0;ii<appList.size();ii++){
			AppInfo appInfo = appList.get(ii);
			appList.get(ii).setCatagories(catagoryService.getCatagoriesByAppId(appInfo.getId()));
		}
		
		for(int ii=0;ii<appList.size();ii++){
			AppInfo appInfo = appList.get(ii);
			appList.get(ii).setChannels(channelService.getChannelsByAppId(appInfo.getId()));
		}
		
		List<CatagoryInfo> catagories = catagoryService.getAllCatagories();
		map.put("catagories", catagories);
		
		List<ChannelInfo> channels = channelService.getAllChannels();
		map.put("channels", channels);
		
		map.put("pagerForm", pagerForm);
		map.put("list", appList);
	}
	
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public String addAppGet(Map<String, Object> map){
		List<CatagoryInfo> catagories = catagoryService.getAllValidCatagories();
		List<ChannelInfo> channels = channelService.getAllValidChannels();
		map.put("catagories", catagories);
		map.put("channels", channels);
		return "/jsp/operation/app/app_add.jsp";
	}
	
	/**
	 * 上传APK
	 * @param uploadFile
	 * @param response
	 */
	@RequestMapping(value="/upload-apk", method=RequestMethod.POST)
	public void uploadsAppApk(@RequestParam(value="apk", required=false) CommonsMultipartFile uploadFile, 
				HttpServletResponse response, Map<String, Object> map) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			if(uploadFile != null) {
				String fileName = uploadFile.getOriginalFilename();
				if(!ApkTool.checkApk(fileName)) {
					ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
					ret.setMessage("添加应用-上传应用失败！【请上传APK文件】");
					return;
				}
			}
			
			String apkUrl = saveApkFile(uploadFile);
			if(StringUtils.isBlank(apkUrl)) {
				ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
				ret.setMessage("添加应用-上传应用失败！");
			} else {
				ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
				ret.setMessage("添加应用-上传应用成功!");
				
				ApkInfo apkInfo = ApkTool.getApkInfo(apkUrl);
				apkInfo.setApkUrl(ApkTool.getApkUrlPath() + "/" + apkUrl);
				
				AppInfo appInfo = appService.getLastestVerApp(apkInfo.getPkgName());
				if(appInfo != null) {
					apkInfo.setIsUpdate(true);
				}
				
				ObjectMapper json = new ObjectMapper();
				String apkInfoJson = json.writer().writeValueAsString(apkInfo);
				ret.setData(apkInfoJson);
			}
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	/**
	 * 获取升级信息
	 * @param pkgName
	 * @param response
	 */
	@RequestMapping(value="/get-upgrade-info", method=RequestMethod.POST)
	public void getUpgradeInfo(@RequestParam(value="pkgName") String pkgName, 
					@RequestParam(value="verCode") long verCode, HttpServletResponse response) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			ret = appService.getAppUpgradeInfo(pkgName, verCode);
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	/***
	 * 上传海报
	 * @param uploadFile
	 * @param response
	 */
	@RequestMapping(value="/upload-poster", method=RequestMethod.POST)
 	public void uploadAppPoster(@RequestParam(value="poster", required=false) CommonsMultipartFile uploadFile, HttpServletResponse response) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			if(uploadFile != null) {
				String fileName = uploadFile.getOriginalFilename();
				if(!ApkTool.checkImg(fileName)) {
					ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
					ret.setMessage("添加应用-上传应用失败！【请上传正确的格式图片】");
					return;
				}
			}
			String imgFileName = saveImageFile(uploadFile, "poster");
			if(ImgTool.isPng(imgFileName)) {
				ImgTool.compressPng(ApkTool.getImgSavePath() + File.separator + imgFileName);
			}
			
			if(StringUtils.isBlank(imgFileName)) {
				ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
				ret.setMessage("添加应用-上传海报失败！");
			} else {
				ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
				ret.setMessage("添加用用-上传海报成功!");
				ret.setData(ApkTool.getImgUrlPath() + "/" + imgFileName);
			}
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	/**
	 * 上传图标
	 * @param uploadFile
	 * @param response
	 */
//	@RequestMapping(value="/upload-icon", method=RequestMethod.POST)
	public void uploadAppIcon(@RequestParam(value="icon", required=false) CommonsMultipartFile uploadFile, HttpServletResponse response) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			String imgUrl = saveImageFile(uploadFile, "icon");
			if(StringUtils.isBlank(imgUrl)) {
				ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
				ret.setMessage("添加应用-上传图标失败！");
			} else {
				ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
				ret.setMessage("添加用用-上传图标成功!");
				ret.setData(ApkTool.getImgUrlPath() + imgUrl);
			}
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}	
	
	/**
	 * 上传截图
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/upload-snapshots", method=RequestMethod.POST)
	public void uploadAppSnapshots(MultipartHttpServletRequest request, HttpServletResponse response) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			Iterator<String> filesName = request.getFileNames();
			
			if(filesName.hasNext()) {
				String fileName = filesName.next();
				List<MultipartFile> files = request.getFiles(fileName);
				
				if(files.size() < 3 || files.size() > 5) {
					ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
					ret.setMessage("添加应用-上传截图失败！【最少上传3张，最多上传5张！】");
					return;
				}
				
				for(int i = 0; i < files.size(); i ++) {
					String imgFileName = files.get(i).getOriginalFilename();
					if(!ApkTool.checkImg(imgFileName)) {
						ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
						ret.setMessage("添加应用-上传截图失败！【请上传正确的格式图片】");
						return;
					}
				}
				
				StringBuilder snapshotsUrls = new StringBuilder();
				for(int i = 0; i < files.size(); i ++) {
					MultipartFile file = files.get(i);
					String imgFileName = saveImageFile(file, "snapshots");
					if(ImgTool.isPng(imgFileName)) {
						ImgTool.compressPng(ApkTool.getImgSavePath() + File.separator + imgFileName);
					}
					
					snapshotsUrls.append(ApkTool.getImgUrlPath() + "/" + imgFileName);
					if(i < files.size() - 1) {
						snapshotsUrls.append(",");
					}
				}
				
				if(snapshotsUrls.length() > 0) {
					ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
					ret.setMessage("添加应用-上传截图成功！");
					ret.setData(snapshotsUrls.toString());
				} else {
					ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
					ret.setMessage("添加应用-上传截图失败！");
				}
			} else {
				
			}
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	/**
	 * 添加APP
	 * @param appInfo
	 * @param session
	 * @param response
	 */
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public void addAppPost(AppInfo appInfo, long []catagories, long []channels,
				HttpSession session, HttpServletResponse response) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			if(appInfo.getId() > 0) {
				ret = appService.upgradeApp(appInfo, catagories, channels, getLoginUserId(session));
			} else {
				ret = appService.addApp(appInfo, catagories, channels, getLoginUserId(session));
			}
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}	
	}
	
	/**
	 * 删除APP
	 * @param id
	 * @param session
	 * @param response
	 */
	@RequestMapping(value="/remove/{id}")
	public void removeApp(@PathVariable("id") long id, HttpSession session, HttpServletResponse response) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			ret = appService.removeAppById(id, getLoginUserId(session));
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}	
	}
	
	/**
	 * 发布应用
	 * @param id
	 * @param session
	 * @param response
	 */
	@RequestMapping(value="/publish/{id}")
	public void publishApp(@PathVariable("id") long id, HttpSession session, HttpServletResponse response) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			ret = appService.publishAppById(id, getLoginUserId(session));
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	/**
	 * 下架应用
	 * @param id
	 * @param session
	 * @param response
	 */
	@RequestMapping(value="/unpublish/{id}")
	public void unpublishApp(@PathVariable("id") long id, HttpSession session, HttpServletResponse response) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			ret = appService.unpublishAppById(id, getLoginUserId(session));
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	/**
	 * 获取更新应用页面
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/update/{id}", method=RequestMethod.GET)
	public String updateAppGet(@PathVariable("id") long id, Map<String, Object> map) {
		AppInfo app = appService.getAppById(id);
		app.setBrief(app.getBrief().trim());
		app.setUpdateContent(app.getUpdateContent());
		map.put("entity", app);
		
		String []snapshotsUrls = app.getSnapshotsUrls().replace(";", ",").split(",");
		ArrayList<String> snapshotsUrlsList = new ArrayList<String>(5);
		for(int i = 0; i < 5; i ++) {
			if(i < snapshotsUrls.length && StringUtils.isNotBlank(snapshotsUrls[i])) {
				snapshotsUrlsList.add(snapshotsUrls[i]);
			} else {
				snapshotsUrlsList.add("");
			}
		}
		
		map.put("appSnapshotsUrls", snapshotsUrlsList);
		
		List<CatagoryInfo> catagories = catagoryService.getAllValidCatagories();
		map.put("catagories", catagories);

		List<ChannelInfo> channels = channelService.getAllValidChannels();
		map.put("channels", channels);
		
		List<String> appCatagories = appService.getAppCatagories(id);
		map.put("appCatagories", appCatagories);
		
		List<String> appChannels = appService.getAppChannels(id);
		map.put("appChannels", appChannels);
		
		List<String> appSupportControllers = appService.getAppSupportControllers(id);
		map.put("appSupportControllers", appSupportControllers);
		
		return "/jsp/operation/app/app_update.jsp";
	}
	
	/**
	 * 更新应用
	 * @param appInfo
	 * @param catagories
	 * @param channels
	 * @param session
	 * @param response
	 */
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public void updateAppPost(AppInfo appInfo, long []catagories, long []channels, 
			HttpSession session, HttpServletResponse response) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			ret = appService.updateApp(appInfo, catagories, channels, getLoginUserId(session));
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}

	@RequestMapping(value="/repair/{key}", method=RequestMethod.GET)
	public void repairUrl(@PathVariable("key") String key, HttpServletResponse response) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		if("fun123".equalsIgnoreCase(key)) {
			appService.repairUrl();
			
			ret.setStatusCode(AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("修复URL成功！【其它和http:开头的不做处理】");
		} else {
			ret.setStatusCode(AjaxResponseStatusCode.FAIL);
			ret.setMessage("key错误！");
		}
		AjaxResponseUtil.responseJsonObject(response, ret);
	}
	
	/**
	 * 保存图片文件
	 * @param file
	 * @param type
	 * @return
	 */
	private String saveImageFile(MultipartFile file, String type) {
		try {
			if(file != null && !file.isEmpty()) {
				String basePath = ApkTool.getImgSavePath();
				
				String fileName = file.getOriginalFilename();
				String fileId = RandomUUID.genUUID();
				String finalFileName = type + "_" + fileId + "_" + fileName;
				String filePath = basePath + File.separator + finalFileName;
	
				DataOutputStream out = new DataOutputStream(new FileOutputStream(filePath));
				InputStream is = null;
				try {
					is = file.getInputStream();
					byte[] b=new byte[is.available()];
					is.read(b);
					out.write(b);
					return finalFileName;
				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					if (is != null) {
						is.close();
					}
					if (out != null) {
						out.close();
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
	/**
	 * 保存apk文件
	 * @param file
	 * @return
	 */
	private String saveApkFile(MultipartFile file) {
		try {
			if(file != null && !file.isEmpty()) {
				String basePath = ApkTool.getApkSavePath();
				
				String fileName = file.getOriginalFilename();
				String fileId = RandomUUID.genUUID();
				String finalFileName = "apk_" + fileId + "_" + fileName;
				String filePath = basePath + File.separator + finalFileName;
	
				DataOutputStream out = new DataOutputStream(new FileOutputStream(filePath));
				InputStream is = null;
				try {
					is = file.getInputStream();
					byte[] b=new byte[is.available()];
					is.read(b);
					out.write(b);
					return finalFileName;
				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					if (is != null) {
						is.close();
					}
					if (out != null) {
						out.close();
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
}
