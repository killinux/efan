package com.funshion.meedo.util;

import java.io.FileReader;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.funshion.meedo.common.GlobalConstant;

public class AjaxResponseUtil {
	private static final Logger logger = LoggerFactory.getLogger(AjaxResponseUtil.class);
	private static final ObjectMapper mapper = new ObjectMapper();

	public static String getDefaultJson() throws Exception {
		return mapper.writeValueAsString(getAjaxResponse());
	}
	
	public static String getAjaxJson(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static AjaxResponse getAjaxResponse() {
		return new AjaxResponse();
	}
	
	/**
	 * 返回前端 Ajax 请求的结果
	 */
	public static void responseJsonObject(HttpServletResponse response, Object ret){
		try {
			if(ret != null && ret instanceof AjaxResponse){
				response.setHeader(GlobalConstant.CustomResponseHeader.NAMI_STATUS_CODE, ((AjaxResponse)ret).getStatusCode());
			}
			
			responseJsonString(response, getAjaxJson(ret));
		} catch (Exception e) {
			logger.error("Fail to response ajax json.", e);
		}
	}
	
	/**
	 * 从json字符串组装对象
	 */
	public static <T> T loadObjectFromJsonString(String jsonStr, Class<T> valueType){
		try {
			if (StringUtils.isBlank(jsonStr)) {
				return null;
			}
			return mapper.readValue(jsonStr, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 从json字符串组装对象
	 */
	public static <T> T loadObjectFromJsonString(String jsonStr, TypeReference<T> valueTypeRef){
		try {
			if (StringUtils.isBlank(jsonStr)) {
				return null;
			}
			return mapper.readValue(jsonStr, valueTypeRef);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 从json文件组装对象
	 */
	public static <T> T loadObjectFromFile(String filepath, Class<T> valueType) {
		FileReader reader = null;
		try {
			reader = new FileReader(filepath);
			return mapper.readValue(reader, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error("Fail to close reader.", e);
				}
			}
		}
	}
	
	/**
	 * 返回前端 Ajax 请求的结果
	 */
	public static void responseJsonString(HttpServletResponse response, String str){
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(str);
			response.getWriter().flush();
		} catch (Exception e) {
			logger.error("Fail to response ajax json.", e);
		}
	}

	public static class AjaxResponse {
		private String statusCode;
		private String message;
		private String navTabId;
		private String rel;
		private String callbackType;
		private String forwardUrl;
		private String confirmMsg;
		private String data;

		public String getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(String statusCode) {
			this.statusCode = statusCode;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getNavTabId() {
			return navTabId;
		}

		public void setNavTabId(String navTabId) {
			this.navTabId = navTabId;
		}

		public String getRel() {
			return rel;
		}

		public void setRel(String rel) {
			this.rel = rel;
		}

		public String getCallbackType() {
			return callbackType;
		}

		public void setCallbackType(String callbackType) {
			this.callbackType = callbackType;
		}

		public String getForwardUrl() {
			return forwardUrl;
		}

		public void setForwardUrl(String forwardUrl) {
			this.forwardUrl = forwardUrl;
		}

		public String getConfirmMsg() {
			return confirmMsg;
		}

		public void setConfirmMsg(String confirmMsg) {
			this.confirmMsg = confirmMsg;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}

	}
}
