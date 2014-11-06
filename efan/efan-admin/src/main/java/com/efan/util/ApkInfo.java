package com.efan.util;

public class ApkInfo {
	String apkUrl;
	String pkgName;
	String verName;
	long verCode;
	double pkgSize;
	String displayName;
	private boolean isUpdate;
	
	public String getApkUrl() {
		return apkUrl;
	}
	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getPkgName() {
		return pkgName;
	}
	
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	
	public double getPkgSize() {
		return pkgSize;
	}
	
	public void setPkgSize(double pkgSize) {
		this.pkgSize = pkgSize;
	}
	
	public String getVerName() {
		return verName;
	}
	
	public void setVerName(String verName) {
		this.verName = verName;
	}
	
	public long getVerCode() {
		return verCode;
	}
	
	public void setVerCode(long verCode) {
		this.verCode = verCode;
	}
	public boolean getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}
}
