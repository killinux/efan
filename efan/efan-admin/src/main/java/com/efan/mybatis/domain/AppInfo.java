package com.efan.mybatis.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class AppInfo {
	private long id;
	private String name;
	private String pkgName;
	private String verName;
	private String verCode;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date pubTime;
	private String pubStatus;
	private double pkgSize;
	private String brief;
	private String posterUrl;
	private String iconUrl;
	private String snapshotsUrls;
	private String updateContent;
	private String supportControllers;
	private boolean isDeleted;
	private String apkUrl;
	private long downloadCnt;
	private int weight;
	private String developer;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private long createUser;
	private String createUserName;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	private long updateUser;
	private String updateUserName;
	private String lastestVer;
	
	/*
	 * 管理频道数据的时候返回给前段用的，数据库没这个值
	 */
	private String catagories;
	
	private String channels;
	
	/*
	 * 管理频道数据的时候返回给前段用的，数据库没这个值
	 */
	private String channelId;
	
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPkgName() {
		return pkgName;
	}
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	public String getVerName() {
		return verName;
	}
	public void setVerName(String verName) {
		this.verName = verName;
	}
	public String getVerCode() {
		return verCode;
	}
	public void setVerCode(String verCode) {
		this.verCode = verCode;
	}
	public Date getPubTime() {
		return pubTime;
	}
	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}
	public String getPubStatus() {
		return pubStatus;
	}
	public void setPubStatus(String pubStatus) {
		this.pubStatus = pubStatus;
	}
	public double getPkgSize() {
		return pkgSize;
	}
	public void setPkgSize(double pkgSize) {
		this.pkgSize = pkgSize;
	}
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
	}
	public String getPosterUrl() {
		return posterUrl;
	}
	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getSupportControllers() {
		return supportControllers;
	}
	public void setSupportControllers(String supportControllers) {
		this.supportControllers = supportControllers;
	}
	public String getSnapshotsUrls() {
		return snapshotsUrls;
	}
	public void setSnapshotsUrls(String snapshotsUrls) {
		this.snapshotsUrls = snapshotsUrls;
	}
	public String getUpdateContent() {
		return updateContent;
	}
	public void setUpdateContent(String updateContent) {
		this.updateContent = updateContent;
	}
	public boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getApkUrl() {
		return apkUrl;
	}
	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	public long getDownloadCnt() {
		return downloadCnt;
	}
	public void setDownloadCnt(long downloadCnt) {
		this.downloadCnt = downloadCnt;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public String getLastestVer() {
		return lastestVer;
	}
	public void setLastestVer(String lastestVer) {
		this.lastestVer = lastestVer;
	}
	public String getCatagories() {
		return catagories;
	}
	public void setCatagories(String catagories) {
		this.catagories = catagories;
	}
	public String getChannels() {
		return channels;
	}
	public void setChannels(String channels) {
		this.channels = channels;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(long createUser) {
		this.createUser = createUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public long getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(long updateUser) {
		this.updateUser = updateUser;
	}
	public String getUpdateUserName() {
		return updateUserName;
	}
	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
}
