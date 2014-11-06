package com.efan.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApkTool {
	protected final static Logger logger = LoggerFactory.getLogger(ApkTool.class);
	public final static String APK_PACAKGE = "package";
	public final static String APK_APPLICATION = "application";
	
	public final static Set<String> APK_SUFFIX_SET = new HashSet<String>();
	public final static Set<String> APK_IMG_FORMAT_SET = new HashSet<String>();
	static {
		APK_SUFFIX_SET.add(".apk");
		
		APK_IMG_FORMAT_SET.add(".jpg");
		APK_IMG_FORMAT_SET.add(".png");
		APK_IMG_FORMAT_SET.add(".webp");
	}
	
	/**
	 * 判断是否为APK文件
	 * @param fileName
	 * @return
	 */
	public static boolean checkApk(String fileName) {
		int suffixIndex = fileName.lastIndexOf(".");
		String suffix = fileName.toLowerCase().substring(suffixIndex);
		
		return APK_SUFFIX_SET.contains(suffix);
	}
	
	/**
	 * 判断是否为jgp、png、webp格式图片
	 * @param imgName
	 * @return
	 */
	public static boolean checkImg(String imgName) {
		int suffixIndex = imgName.lastIndexOf(".");
		String suffix = imgName.toLowerCase().substring(suffixIndex);
		
		return APK_IMG_FORMAT_SET.contains(suffix);
	}
	
	public static ApkInfo getApkInfo(String apkUri) {
		String finalApkUri = getApkSavePath() + File.separator + apkUri;
		ApkInfo apkInfo = new ApkInfo();
		getPkgInfo(apkInfo, finalApkUri);
		apkInfo.setPkgSize(getApkSize(finalApkUri));
		
		return apkInfo;
	}
	
	private static void getPkgInfo(ApkInfo apkInfo, String apkUri) {
		Process process = null;
		BufferedReader br = null;
		try {
			ProcessBuilder pb = new ProcessBuilder();
			process = pb.command("aapt", "d", "badging", apkUri).start();
			br = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
			
			String line = null;
			while ((line = br.readLine()) != null) {
				if(line.startsWith(APK_PACAKGE)) {
					line = line.replace("'", "");
					String []pkgArr = line.split(":");
					if(pkgArr != null && pkgArr.length > 1) {
						if(APK_PACAKGE.compareTo(pkgArr[0].trim()) == 0) {
							String []pkgDetails = pkgArr[1].trim().split(" ");
							if(pkgDetails != null) {
								for(String detail : pkgDetails) {
									if(detail.startsWith("name")) {
										apkInfo.setPkgName(detail.split("=")[1]);
									} else if(detail.startsWith("versionCode")) {
										apkInfo.setVerCode(Long.valueOf(detail.split("=")[1]));
									} else if(detail.startsWith("versionName")) {
										apkInfo.setVerName(detail.split("=")[1]);
									}
								}
							}
						}
					}
				} else if (line.startsWith(APK_APPLICATION)) {
					line = line.replace("'", "");
					String []apkNameArr = line.split(":");
					if(apkNameArr != null && (apkNameArr.length > 1)) {
						if(APK_APPLICATION.compareTo(apkNameArr[0].trim()) == 0) {
							String []appDetails = apkNameArr[1].trim().split(" ");
							if(appDetails != null) {
								for(String detail : appDetails) {
									if(detail.startsWith("label")) {
										apkInfo.setDisplayName(detail.split("=")[1]);
									}
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			logger.error("Extract apk info failed.{}", e.getMessage());
		} finally {
			if(process != null) {
				process.destroy();
			}
			
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error("ApkTool close iostream failed.{}", e.getMessage());
				}
			}
		}
	}
	
	private static double getApkSize(String apkUri) {
		double fileSize = 0;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(apkUri);
			fileSize = fis.available();
			BigDecimal bd = new BigDecimal(fileSize/(1024*1024));
			fileSize = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			return fileSize;
		} catch (FileNotFoundException e) {
			logger.error("ApkTool get apk file failed.{}-{}", e.getMessage(), apkUri);
		} catch (IOException e) {
			logger.error("ApkTool read apk file failed.{}-{}", e.getMessage(), apkUri);
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return fileSize;
	}
	
	public static String getImgSavePath() {
		String imgPath = "";
		try {
			imgPath = PropertyHandler.getInstance().getValue("img.file.path");
		} catch (Exception e) {
			logger.error("ApkTool get img file path property failed.{}-{}", e.getMessage());
		}
		return imgPath;
	}
	
	public static String getImgUrlPath() {
		String imgPath = "";
		try {
			imgPath = PropertyHandler.getInstance().getValue("img.url.path");
		} catch (Exception e) {
			logger.error("ApkTool get img url path property failed.{}-{}", e.getMessage());
		}
		return imgPath.replace("\\", "/");
	}
	
	public static String getImgCDNPath() {
		String imgPath = "";
		try {
			imgPath = PropertyHandler.getInstance().getValue("img.cdn.path");
		} catch (Exception e) {
			logger.error("ApkTool get img cdn path property failed.{}-{}", e.getMessage());
		}
		return imgPath.replace("\\", "/");
	}
	
	public static String getImgDNSPath() {
		String imgPath = "";
		try {
			imgPath = PropertyHandler.getInstance().getValue("meedo.img.dns");
		} catch (Exception e) {
			logger.error("ApkTool get img dns path property failed.{}-{}", e.getMessage());
		}
		return imgPath.replace("\\", "/");
	}		
	
	public static String getApkSavePath() {
		String apkPath = "";
		try {
			apkPath = PropertyHandler.getInstance().getValue("apk.file.path");
		} catch (Exception e) {
			logger.error("ApkTool get apk file path property failed.{}-{}", e.getMessage());
		}
		return apkPath;
	}
	
	public static String getApkUrlPath() {
		String apkPath = "";
		try {
			apkPath = PropertyHandler.getInstance().getValue("apk.url.path");
		} catch (Exception e) {
			logger.error("ApkTool get apk url path property failed.{}-{}", e.getMessage());
		}
		return apkPath.replace("\\", "/");
	}
	
	public static String getApkCDNPath() {
		String apkPath = "";
		try {
			apkPath = PropertyHandler.getInstance().getValue("apk.cdn.path");
		} catch (Exception e) {
			logger.error("ApkTool get apk cdn path property failed.{}-{}", e.getMessage());
		}
		return apkPath.replace("\\", "/");
	}
	
	public static String getApkDNSPath() {
		String apkPath = "";
		try {
			apkPath = PropertyHandler.getInstance().getValue("meedo.apk.dns");
		} catch (Exception e) {
			logger.error("ApkTool get apk dns path property failed.{}-{}", e.getMessage());
		}
		return apkPath.replace("\\", "/");
	}
}
