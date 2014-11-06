package com.efan.mybatis.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.efan.common.AbstractService;
import com.efan.common.GlobalConstant;
import com.efan.module.operation.app.AppPagerForm;
import com.efan.mybatis.domain.AppInfo;
import com.efan.mybatis.domain.UserInfo;
import com.efan.mybatis.persistence.AppMapper;
import com.efan.util.AjaxResponseUtil.AjaxResponse;
import com.efan.util.ApkTool;
import com.efan.util.MD5Util;

@Service("appService")
public class AppService extends AbstractService {
	@Autowired
	private AppMapper appMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private CatagoryService catagoryService;
	@Autowired
	private ChannelService channelService;

	public  List<AppInfo>  findAppByChannelId(AppPagerForm pagerForm ,long channel_id){
		flushPageInfo(pagerForm, appMapper.countAllAppsByChannelId(pagerForm,channel_id));		
		if(pagerForm.getTotalCount() > 0) {
			pagerForm.setPageStart(getPageStart(pagerForm));
			List<AppInfo> appList = appMapper.findAllAppsByChannelId(pagerForm, channel_id);		
			return appList;
		} else {
			return new ArrayList<AppInfo>();
		}
	}

	@Autowired
	private DataSourceTransactionManager transactionManager; 
	
	public List<AppInfo> findAllAppsPagination(AppPagerForm pagerForm) {
		flushPageInfo(pagerForm, appMapper.countAllAppsPagination(pagerForm));
		if(pagerForm.getTotalCount() > 0) {
			pagerForm.setPageStart(getPageStart(pagerForm));
			List<AppInfo> appList = appMapper.findAllAppsPagination(pagerForm);	
			for(AppInfo appInfo : appList) {
				UserInfo createUserInfo = userService.getUserById(appInfo.getCreateUser());
				if(createUserInfo != null) {
					appInfo.setCreateUserName(createUserInfo.getDisplayName());
				}
				
				UserInfo updateUserInfo = userService.getUserById(appInfo.getUpdateUser());
				if(updateUserInfo != null) {
					appInfo.setUpdateUserName(updateUserInfo.getDisplayName());
				}
			}
			return appList;
		} else {
			return new ArrayList<AppInfo>();
		}
	}
	
	public List<AppInfo> findAllAppsNotInChannelPagination(AppPagerForm pagerForm,long channel_id) {
		System.out.println("findAllAppsNotInChannelPagination--count:"+appMapper.countAllAppsNotInChannelPagination(pagerForm,channel_id));
		flushPageInfo(pagerForm, appMapper.countAllAppsNotInChannelPagination(pagerForm,channel_id));
		if(pagerForm.getTotalCount() > 0) {
			pagerForm.setPageStart(getPageStart(pagerForm));
			List<AppInfo> appList = appMapper.findAllAppsNotInChannelPagination(pagerForm,channel_id);
			
			return appList;
		} else {
			return new ArrayList<AppInfo>();
		}
	}
	public List<AppInfo> findAppsInCatagoryPagination(AppPagerForm pagerForm,String catagory,String channel_id) {
		flushPageInfo(pagerForm, appMapper.countAppsInCatagoryPagination(pagerForm,catagory,channel_id));
		if(pagerForm.getTotalCount() > 0) {
			pagerForm.setPageStart(getPageStart(pagerForm));
			List<AppInfo> appList = appMapper.findAppsInCatagoryPagination(pagerForm,catagory, channel_id);
			
			return appList;
		} else {
			return new ArrayList<AppInfo>();
		}
	}
	
	private String ListToString(List<String> stringList) {
		if (stringList==null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
	}
	
	public AjaxResponse getAppUpgradeInfo(String pkgName, long verCode) throws Exception {
		AjaxResponse ret = new AjaxResponse();
		
		AppInfo appInfo = getLastestVerApp(pkgName);
		if(Long.valueOf(appInfo.getVerCode()) >= verCode) {
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
			ret.setMessage("应用-添加应用失败【版本号低于已上传最新版本，请更换应用包】");
			return ret;
		} 
		
		List<String> catagories = getAppCatagories(appInfo.getId());
		String appCatagories = ListToString(catagories);
		appInfo.setCatagories(appCatagories);
			
		List<String> channels = getAppChannels(appInfo.getId());
		String appChannels = ListToString(channels);
		appInfo.setChannels(appChannels);
		
		ObjectMapper json = new ObjectMapper();
		String apkInfoJson = json.writer().writeValueAsString(appInfo);
		ret.setData(apkInfoJson);
		
		return ret;
	}
	
	private boolean catagoryIsNotExisted(long catagoryId) {
		return catagoryService.getCatagoryById(catagoryId) == null ? true : false;
	}
	
	private boolean channelIsNotExisted(long channelId) {
		return (channelService.getChannelById(channelId) == null) ? true : false;	
	}
	
	public AjaxResponse upgradeApp(AppInfo appInfo, long []catagories, long []channels, long createUser){
		AjaxResponse ret = new AjaxResponse();
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			for(long catagoryId : catagories) {
				if(catagoryIsNotExisted(catagoryId)) {
					ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
					ret.setMessage("应用-添加应用失败【所添加分类已删除】");
					return ret;
				}
			}
			
			for(long channelId : channels) {
				if(channelIsNotExisted(channelId)) {
					ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
					ret.setMessage("应用-添加应用失败【所添加频道已删除】");
					return ret;
				}
			}
			
			//海报压缩和上传cdn
			//图片路径：md5(pkgName + verCode)
			//获取老版本信息如果ID不等于0,表示升级
			AppInfo oldAppInfo = getAppById(appInfo.getId());
			if(oldAppInfo != null) {
				updateAppImgUrlInfo(appInfo);
				
				
				//apk路径上报
				//apk路径：md5(pkgName + verCode + id)
				String apkDest = notifyCDNApkReady(appInfo.getPkgName(), appInfo.getVerCode(), appInfo.getApkUrl());
				appInfo.setApkUrl(apkDest.replace("\\", "/"));
				appInfo.setCreateUser(createUser);
				appInfo.setBrief(appInfo.getBrief().trim());
				appInfo.setUpdateContent(appInfo.getUpdateContent().trim());
				appMapper.addApp(appInfo);
				
//				appMapper.updateLastestVer(appInfo.getPkgName(), Long.valueOf(appInfo.getVerCode()));
				
				appMapper.removeAppCatagory(appInfo.getId());
				for(long catagoryId : catagories) {
					appMapper.addAppCatagory(appInfo.getId(), catagoryId);
				}
				
				appMapper.removeAppChannel(appInfo.getId());
				for(long channelId : channels) {
					appMapper.addAppChannel(appInfo.getId(), channelId);
				}
				
				ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
				ret.setMessage("应用-升级应用成功！");
			} else {
				ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
				ret.setMessage("应用-升级应用失败！【老版本不存在】");
			}
			ret.setCallbackType("closeCurrent");
			ret.setNavTabId("appList");
			ret.setRel("appList");
			ret.setForwardUrl("app/list");
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw new RuntimeException(e);
		}
		
		return ret;
	}
	
	public AjaxResponse addApp(AppInfo appInfo, long []catagories, long []channels, long createUser) {
		AjaxResponse ret = new AjaxResponse();
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			for(long catagoryId : catagories) {
				if(catagoryIsNotExisted(catagoryId)) {
					ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
					ret.setMessage("应用-添加应用失败【所添加分类已删除】");
					return ret;
				}
			}
			
			for(long channelId : channels) {
				if(channelIsNotExisted(channelId)) {
					ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
					ret.setMessage("应用-添加应用失败【所添加频道已删除】");
					return ret;
				}
			}
			
			//海报压缩和上传cdn
			//图片路径：md5(pkgName + verCode)
			String posterDest = compressImg2CDN(appInfo.getPkgName(), appInfo.getVerCode(), appInfo.getPosterUrl());
			appInfo.setPosterUrl(posterDest.replace("\\", "/"));
			
			//截图压缩和上传
			StringBuilder snapshotsUrlsBuilder = new StringBuilder();
			String []snapshotsUrls = appInfo.getSnapshotsUrls().split(",");
			int index = 0;
			for(String snapshotsUrl : snapshotsUrls) {
				String snapshotsDest = compressImg2CDN(appInfo.getPkgName(), appInfo.getVerCode(), snapshotsUrl);
				snapshotsUrlsBuilder.append(snapshotsDest.replace("\\", "/"));
				if(index < snapshotsUrls.length - 1) {
					snapshotsUrlsBuilder.append(",");
				}
				index ++;
			}
			appInfo.setSnapshotsUrls(snapshotsUrlsBuilder.toString().replace("\\", "/"));
			
			//apk路径上报
			//apk路径：md5(pkgName + verCode + id)
			String apkDest = notifyCDNApkReady(appInfo.getPkgName(), appInfo.getVerCode(), appInfo.getApkUrl());
			appInfo.setApkUrl(apkDest.replace("\\", "/"));
			
			//保存应用
			appInfo.setCreateUser(createUser);
			appInfo.setBrief(appInfo.getBrief().trim());
			appInfo.setUpdateContent(appInfo.getUpdateContent().trim());
			appMapper.addApp(appInfo);
			//更新应用最新版本
			//appMapper.updateLastestVer(appInfo.getPkgName(), Long.valueOf(appInfo.getVerCode()));
			
			appMapper.removeAppCatagory(appInfo.getId());
			for(long catagoryId : catagories) {
				appMapper.addAppCatagory(appInfo.getId(), catagoryId);
			}
			
			appMapper.removeAppChannel(appInfo.getId());
			for(long channelId : channels) {
				appMapper.addAppChannel(appInfo.getId(), channelId);
			}
			
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("应用-添加应用成功！");
			
			ret.setCallbackType("closeCurrent");
			ret.setNavTabId("appList");
			ret.setRel("appList");
			ret.setForwardUrl("app/list");
			
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw new RuntimeException(e);
		}
		
		return ret;
	}
	
	/**
	 * 压缩图片并上传图片到CDN(img.funshion.com)
	 * @param pkgName
	 * @param verCode
	 * @param fileName
	 * @return
	 */
	private String getAppImgPath(String pkgName, String verCode, String fileName) {
		String dir1stStr = MD5Util.getStringMD5String(pkgName).substring(0, 6);
		String dir2ndStr = MD5Util.getStringMD5String(verCode).substring(0, 6);
		
		String imgDirStr = ApkTool.getImgSavePath() + File.separator + dir1stStr + File.separator + dir2ndStr;
		File imgDir = new File(imgDirStr);
		if(!imgDir.exists()) {
			if(!imgDir.mkdirs()) {
				logger.error("创建图片路径失败！{}", imgDir);
			}
		}
		return (imgDirStr + File.separator + fileName);
	}
	
	/**
	 * 上传apk到CDN(neirong.funshion.com)
	 * @param pkgName
	 * @param verCode
	 * @param fileName
	 * @return
	 */
	private String getAppApkPath(String pkgName, String verCode, String fileName) {
		String dir1stStr = MD5Util.getStringMD5String(pkgName).substring(0, 6);
		String dir2ndStr = MD5Util.getStringMD5String(verCode).substring(0, 6);
		
		String pkgDirStr = ApkTool.getApkSavePath() + File.separator + dir1stStr + File.separator + dir2ndStr;
		File pkgDir = new File(pkgDirStr);
		if(!pkgDir.exists()) {
			if(!pkgDir.mkdirs()) {
				logger.error("创建apk路径失败！{}", pkgDir);
			}
		}
		return (pkgDirStr + File.separator + fileName);
	}
	
	private String compressImg2CDN(String pkgName, String verCode, String imgUrl) {
		String compressedFileName = getImgFileName(imgUrl);
		String imgSrc = ApkTool.getImgSavePath() + File.separator + compressedFileName;
		String imgDest = getAppImgPath(pkgName, verCode, compressedFileName);
		
		if(moveFile(imgSrc, imgDest)) {
			return imgDest.replace(ApkTool.getImgSavePath(), "");
		} else {
			return "";
		}
	}
	
	private String getImgFileName(String imgPath) {
		imgPath = imgPath.replace("\\", "/");
		String compressedFileName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
		return compressedFileName;
	}
	
	private String getApkFileName(String apkPath) {
		apkPath = apkPath.replace("\\", "/");
		String apkFileName = apkPath.substring(apkPath.lastIndexOf("/") + 1);
		
		return apkFileName;
	}
	
	private String notifyCDNApkReady(String pkgName, String verCode, String apkUrl) {
		String apkFileName = getApkFileName(apkUrl);
		String apkSrc = ApkTool.getApkSavePath() + File.separator + apkFileName;
		String apkDest = getAppApkPath(pkgName, verCode, apkFileName);
		if(moveFile(apkSrc, apkDest)) {
			return apkDest.replace(ApkTool.getApkSavePath(), "");
		} else {
			return "";
		}
	}
	
	private boolean moveFile(String src, String dest) {
		File srcFile = new File(src);
		if(srcFile.exists() && srcFile.isFile()) {
			if(!srcFile.renameTo(new File(dest))) {
				logger.error("移动文件 {} 到 {} 失败", src, dest);
				return false;
			}
		} else {
			logger.error("移动文件失败！源文件{}不存在", src);
			return false;
		}
		return true;
	}
	
	public AppInfo getAppById(long id) {
		AppInfo appInfo = appMapper.getAppById(id);
		
		setImgUrlPath(appInfo);
		
		return appInfo;
	}
	
	public AjaxResponse updateApp(AppInfo appInfo, long []catagories, long []channels, long updateUser) {
		AjaxResponse ret = new AjaxResponse();
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			for(long catagoryId : catagories) {
				if(catagoryIsNotExisted(catagoryId)) {
					ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
					ret.setMessage("应用-添加应用失败【所添加分类已删除】");
					return ret;
				}
			}
			
			for(long channelId : channels) {
				if(channelIsNotExisted(channelId)) {
					ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
					ret.setMessage("应用-添加应用失败【所添加频道已删除】");
					return ret;
				}
			}
			
			AppInfo oldAppInfo = getAppById(appInfo.getId());
			if(oldAppInfo != null) {
				updateAppImgUrlInfo(appInfo);
			}
			
			appInfo.setUpdateUser(updateUser);
			appInfo.setBrief(appInfo.getBrief().trim());
			appInfo.setUpdateContent(appInfo.getUpdateContent().trim());
			appMapper.updateApp(appInfo);
			
			appMapper.removeAppCatagory(appInfo.getId());
			for(long catagoryId : catagories) {
				appMapper.addAppCatagory(appInfo.getId(), catagoryId);
			}
			
			appMapper.removeAppChannel(appInfo.getId());
			for(long channelId : channels) {
				appMapper.addAppChannel(appInfo.getId(), channelId);
			}
			
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("应用-更新应用成功！");
			
			ret.setCallbackType("closeCurrent");
			ret.setNavTabId("appList");
			ret.setRel("appList");
			ret.setForwardUrl("app/list");
			
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw new RuntimeException(e);
		}
		
		return ret;	
	}
	
	private void updateAppImgUrlInfo(AppInfo appInfo) {
		AppInfo oldAppInfo = getAppById(appInfo.getId());
		if(oldAppInfo != null) {
			String oldPosterUrl = oldAppInfo.getPosterUrl();
			String oldSnapshotsUrls = oldAppInfo.getSnapshotsUrls();
			String newPosterUrl = appInfo.getPosterUrl();
			String newSnapshotsUrls = appInfo.getSnapshotsUrls();
			
			//如果升级后的海报url同老版本相同，则不用压缩和移动海报图片，不同则需要压缩并移动图片
			if(oldPosterUrl.compareTo(newPosterUrl) != 0) {
				String posterDest = compressImg2CDN(appInfo.getPkgName(), appInfo.getVerCode(), appInfo.getPosterUrl());
				appInfo.setPosterUrl(posterDest.replace("\\", "/"));
			} else {
				appInfo.setPosterUrl(newPosterUrl.replace(ApkTool.getImgUrlPath(), ""));
			}
			
			//如果升级后的截图url同老版本的相同，则不压缩和移动截图图片，不同则需要压缩并移动图片
			if(oldSnapshotsUrls.compareTo(newSnapshotsUrls) != 0) {
				//截图压缩和上传
				StringBuilder snapshotsUrlsBuilder = new StringBuilder();
				String []snapshotsUrls = appInfo.getSnapshotsUrls().split(",");
				int index = 0;
				for(String snapshotsUrl : snapshotsUrls) {
					String snapshotsDest = compressImg2CDN(appInfo.getPkgName(), appInfo.getVerCode(), snapshotsUrl);
					snapshotsUrlsBuilder.append(snapshotsDest);
					if(index < snapshotsUrls.length - 1) {
						snapshotsUrlsBuilder.append(",");
					}
					index ++;
				}
				appInfo.setSnapshotsUrls(snapshotsUrlsBuilder.toString().replace("\\", "/"));
			} else {
				appInfo.setSnapshotsUrls(newSnapshotsUrls.replace(ApkTool.getImgUrlPath(), ""));
			}
		}
	}
	
	public AjaxResponse publishAppById(long id, long updateUser) {
		AjaxResponse ret = new AjaxResponse();
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			
			AppInfo appInfo = this.getAppById(id);
			
			AppInfo appInfoLatest = appMapper.getLastestVerApp(appInfo.getPkgName());
			System.out.println("publishAppById:"+appInfoLatest.getVerCode());
			if(appInfoLatest.getLastestVer()==null||Long.valueOf(appInfoLatest.getVerCode()).longValue()<=Long.valueOf(appInfo.getVerCode()).longValue()){
				appMapper.publishAppById(id, updateUser);
				appMapper.updateLastestVer(appInfo.getPkgName(), Long.valueOf(appInfo.getVerCode()));//haohao
				
				appMapper.updateAppPublishState(appInfo,updateUser);//下架比当前版本小的app，modify by hao
				System.out.println("publishAppById---"+appInfo.getVerCode());
				ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
				ret.setMessage("应用-发布应用成功！");
				ret.setNavTabId("appList");
			}else{
				ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
				ret.setMessage("应用-发布应用失败，存在已经发布的新版本！");
				
				ret.setNavTabId("appList");
			}
			
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return ret;		
	}
	
	public AjaxResponse unpublishAppById(long id, long updateUser) {
		AjaxResponse ret = new AjaxResponse();
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			appMapper.unpublishAppById(id, updateUser);
			
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("应用-下架应用成功！");
			
			ret.setNavTabId("appList");
			
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw new RuntimeException(e);
		}
		
		return ret;		
	}
	
	public List<String> getAppCatagories(long id) {
		return appMapper.getAppCatagories(id);
	}
	
	public List<String> getAppChannels(long id) {
		return appMapper.getAppChannels(id);
	}
	
	public List<String> getAppSupportControllers(long id) {
		String supportControllers = appMapper.getAppSupportControllers(id);
		String []supportControllersArray = supportControllers.split(";");
		List<String> supportControllersList = (List<String>) Arrays.asList(supportControllersArray);
		
		return supportControllersList;
	}
	
	public AjaxResponse removeAppById(long id, long updateUser) {
		AjaxResponse ret = new AjaxResponse();
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			appMapper.removeAppById(id, updateUser);
			
			ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.SUCCESS);
			ret.setMessage("应用-删除应用成功！");
			ret.setNavTabId("appList");
			
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw new RuntimeException(e);
		}
		
		return ret;		
	}
	
	public AppInfo getLastestVerApp(String pkgName) {
		if(StringUtils.isNotBlank(pkgName)) {
			AppInfo appInfo = appMapper.getLastestVerApp(pkgName);

			setImgUrlPath(appInfo);
			
			return appInfo;
		} else {
			return null;
		}
	}
	
	private void setImgUrlPath(AppInfo appInfo) {
		if(appInfo != null) {
//			String channels = channelService.getChannelsByAppId(appInfo.getId());
//			if(channels.contains("其它") || appInfo.getApkUrl().startsWith("http:")) {
//				return;
//			}
			
			if(StringUtils.isNotBlank(appInfo.getApkUrl())) {
				appInfo.setApkUrl(ApkTool.getApkUrlPath() + appInfo.getApkUrl().replace(ApkTool.getApkUrlPath(), ""));
			}
			
			//添加图片访问路径
			if(StringUtils.isNotBlank(appInfo.getPosterUrl())) {
				appInfo.setPosterUrl(ApkTool.getImgUrlPath() + appInfo.getPosterUrl().replace(ApkTool.getImgUrlPath(), ""));
			}
			
			if(StringUtils.isNotBlank(appInfo.getSnapshotsUrls())) {
				appInfo.setSnapshotsUrls(appInfo.getSnapshotsUrls().replace(";", ","));
				String []allSnapshotsUrls = appInfo.getSnapshotsUrls().split(",");
				StringBuilder snapshotsUrls = new StringBuilder();
				int index = 0;
				for(String snapshotsUrl : allSnapshotsUrls) {
					snapshotsUrl = ApkTool.getImgUrlPath() + snapshotsUrl.replace(ApkTool.getImgUrlPath(), "");
					snapshotsUrls.append(snapshotsUrl);
					if(index < allSnapshotsUrls.length - 1 && StringUtils.isNotBlank(snapshotsUrl)) {
						snapshotsUrls.append(",");
					}
					index ++;
				}
				appInfo.setSnapshotsUrls(snapshotsUrls.toString());
			}
		}
	}
	
	public void repairUrl(){
		AppPagerForm pagerForm = new AppPagerForm();
		int total = appMapper.countAllAppsPagination(pagerForm);
		pagerForm.setNumPerPage(total);
		List<AppInfo> appList = this.findAllAppsPagination(pagerForm);
		
		for(AppInfo appInfo : appList) {
//			String channels = channelService.getChannelsByAppId(appInfo.getId());
//			if(channels.contains("其它") || appInfo.getApkUrl().startsWith("http:")) {
//				return;
//			}
			
			String apkUrl = appInfo.getApkUrl();
			String posterUrl = appInfo.getPosterUrl();
			String snapshotsUrl = appInfo.getSnapshotsUrls();
			
			if(StringUtils.isNotBlank(apkUrl)) {
				/***
				 *   /meedo-apk/a.apk->/asdf12/asdf12/a.apk
				 *   /meedo-apk/asdf12/12312s/a.apk->/asdf12/12312s/a.apk
				 */
				String apkPathUrl = getAppApkPath(appInfo.getPkgName(), appInfo.getVerCode(), getApkFileName(apkUrl));
				apkPathUrl = apkPathUrl.replace("\\", "/");
				String apkPathUrlMD5Head = apkPathUrl.substring(0, apkPathUrl.lastIndexOf("/"));
				apkPathUrlMD5Head = apkPathUrlMD5Head.replace(ApkTool.getApkSavePath(), "");
				if(apkUrl.startsWith(ApkTool.getApkUrlPath()) && apkUrl.contains(apkPathUrlMD5Head)) {
					apkUrl = apkUrl.replace(ApkTool.getApkUrlPath(), "");
				} else if (apkUrl.startsWith(ApkTool.getApkUrlPath()) && !apkUrl.contains(apkPathUrlMD5Head)) {
					apkUrl = notifyCDNApkReady(appInfo.getPkgName(), appInfo.getVerCode(), apkUrl);
				}
				
				appInfo.setApkUrl(apkUrl);
			}
			
			if(StringUtils.isNotBlank(posterUrl)) {
				/***
				 *   /meedo-img/a.png->/asdf12/asdf12/a.png
				 *   /meedo-img/asdf12/12312s/a.png->/asdf12/12312s/a.png
				 */
				String posterPathUrl = getAppImgPath(appInfo.getPkgName(), appInfo.getVerCode(), getImgFileName(posterUrl));
				posterPathUrl = posterPathUrl.replace("\\", "/");
				String posterPathUrlMD5Head = posterPathUrl.substring(0, posterPathUrl.lastIndexOf("/"));
				posterPathUrlMD5Head = posterPathUrlMD5Head.replace(ApkTool.getImgSavePath(), "");
				if(posterUrl.startsWith(ApkTool.getImgUrlPath()) && posterUrl.contains(posterPathUrlMD5Head)) {
					posterUrl = posterUrl.replace(ApkTool.getImgUrlPath(), "");
				} else if (posterUrl.startsWith(ApkTool.getImgUrlPath()) && !posterUrl.contains(posterPathUrlMD5Head)) {
					posterUrl = compressImg2CDN(appInfo.getPkgName(), appInfo.getVerCode(), posterUrl);
				}
				
				appInfo.setPosterUrl(posterUrl);
			}
			
			if(StringUtils.isNotBlank(snapshotsUrl)) {
				/***
				 *   /meedo-img/a.png->/asdf12/asdf12/a.png
				 *   /meedo-img/asdf12/12312s/a.png->/asdf12/12312s/a.png
				 */
				appInfo.setSnapshotsUrls(appInfo.getSnapshotsUrls().replace(";", ","));
				String []allSnapshotsUrls = appInfo.getSnapshotsUrls().split(",");
				StringBuilder snapshotsUrls = new StringBuilder();
				int index = 0;
				for(String item : allSnapshotsUrls) {
					String snapshotsPathUrl = getAppImgPath(appInfo.getPkgName(), appInfo.getVerCode(), getImgFileName(item));
					snapshotsPathUrl = snapshotsPathUrl.replace("\\", "/");
					String snapshotsPathUrlMD5Head = snapshotsPathUrl.substring(0, snapshotsPathUrl.lastIndexOf("/"));
					snapshotsPathUrlMD5Head = snapshotsPathUrlMD5Head.replace(ApkTool.getImgSavePath(), "");
					if(item.startsWith(ApkTool.getImgUrlPath()) && item.contains(snapshotsPathUrlMD5Head)) {
						item = item.replace(ApkTool.getImgUrlPath(), "");
					} else if (item.startsWith(ApkTool.getImgUrlPath()) && !item.contains(snapshotsPathUrlMD5Head)) {
						item = compressImg2CDN(appInfo.getPkgName(), appInfo.getVerCode(), item);
					}
					snapshotsUrls.append(item);
					if(index < allSnapshotsUrls.length - 1 && StringUtils.isNotBlank(item)) {
						snapshotsUrls.append(",");
					}
					index ++;
				}
				appInfo.setSnapshotsUrls(snapshotsUrls.toString());
			}
			
			appMapper.repaireAppUrl(appInfo);
		}
	}
	
	public List<AppInfo> getAllAppsIsW() {
		return appMapper.getAppAppsIsW();
	}
	
	public void updateAppPubStatus(long appId, String pubStatus) {
		appMapper.updateAppPubStatus(appId, pubStatus);
	}
}
