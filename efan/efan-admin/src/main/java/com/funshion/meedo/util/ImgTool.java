package com.funshion.meedo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImgTool {
	protected final static Logger logger = LoggerFactory.getLogger(ImgTool.class);
	public final static String SUPPORT_IMG_FORMAT = ".png";
	
	public static boolean isPng(String fileName) {
		if(StringUtils.isNotBlank(fileName)) {
			return fileName.endsWith(SUPPORT_IMG_FORMAT);
		}
		
		return false;
	}
	
	public static void compressPng(String file) {
		Process process = null;
		BufferedReader br = null;
		
		try {
			ProcessBuilder pb = new ProcessBuilder();
			process = pb.command("pngquant", "256", file, "--ext", ".png", "--force").start();
			
			br = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
			
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch(IOException e) {
			logger.error("Compress png picture failed.{}", e.getMessage());
		} finally {
			if(process != null) {
				process.destroy();
			}
			
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error("ImgTool close iostream failed.{}", e.getMessage());
				}
			}
		}
	}
}
