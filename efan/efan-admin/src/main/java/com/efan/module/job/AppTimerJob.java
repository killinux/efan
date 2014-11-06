package com.efan.module.job;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.efan.common.AbstractCronJob;
import com.efan.common.GlobalConstant.App;
import com.efan.mybatis.domain.AppInfo;
import com.efan.mybatis.service.AppService;
import com.efan.util.ApkTool;

public class AppTimerJob extends AbstractCronJob {
	private static final int SOCKET_TIMEOUT = 10000;
	private static final int CONNECT_TIMEOUT = 3000;
	
	private AppService appService;
	
	@Override
	protected void doExecute() {
		List<AppInfo> newApps = appService.getAllAppsIsW();
		
		for(AppInfo app : newApps) {
			String apkRelUrl = app.getApkUrl();
			String apkReqUrl = ApkTool.getApkDNSPath() + ApkTool.getApkCDNPath() + apkRelUrl;
			if(!checkUrlIsValid(apkReqUrl)) {
				continue;
			}
			
			String posterRelUrl = app.getPosterUrl();
			String posterReqUrl = ApkTool.getImgDNSPath() + ApkTool.getImgCDNPath() + posterRelUrl;
			if(!checkUrlIsValid(posterReqUrl)) {
				continue;
			}
			
			String allSnapshotsUrls = app.getSnapshotsUrls();
			String []snapshotsUrlsArr = allSnapshotsUrls.split(",");
			boolean snapshotsValid = true;
			for(String snapshotsUrl : snapshotsUrlsArr) {
				String snapshotsReqUrl = ApkTool.getImgDNSPath() + ApkTool.getImgCDNPath() + snapshotsUrl;
				if(!checkUrlIsValid(snapshotsReqUrl)) {
					snapshotsValid = false;
				}
			}
			
			if(snapshotsValid) {
				appService.updateAppPubStatus(app.getId(), App.Status.TO_PUBLISH);
			}
		}
	}
	
	private boolean checkUrlIsValid(String apkUrl) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT)
				.build();
		
		HttpGet httpGet = new HttpGet(apkUrl);
		httpGet.setConfig(requestConfig);
		CloseableHttpResponse response = null;
		
		boolean isValid = false;
		
		try {
			response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200) {
				isValid = true;
			}
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}
		return isValid;
	}

	public AppService getAppService() {
		return appService;
	}

	public void setAppService(AppService appService) {
		this.appService = appService;
	}
}
