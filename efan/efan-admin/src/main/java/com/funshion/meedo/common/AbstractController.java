package com.funshion.meedo.common;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.funshion.meedo.common.GlobalConstant.AjaxResponseStatusCode;
import com.funshion.meedo.mybatis.domain.UserInfo;
import com.funshion.meedo.util.AjaxResponseUtil;
import com.funshion.meedo.util.AjaxResponseUtil.AjaxResponse;

public abstract class AbstractController {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 将 session 中的信息放入流程中
	 */
	protected void putSessionInfosToFlow(HttpSession session, Map<String, Object> map) {
		UserInfo user = (UserInfo) session.getAttribute(GlobalConstant.SessionKey.LOGIN_USER);
		map.put("user", user);
	}
	
	/** 得到登陆用户的Id */
	protected Long getLoginUserId(HttpSession session) {
		UserInfo user = (UserInfo) session.getAttribute(GlobalConstant.SessionKey.LOGIN_USER);
		if(user != null){
			return user.getId();
		}else{
			return null;
		}
	}
	
	/** 得到登陆的用户信息 */
	protected UserInfo getLoginUser(HttpSession session) {
		return (UserInfo) session.getAttribute(GlobalConstant.SessionKey.LOGIN_USER);
	}
	
	/**
	 * 产生错误信息
	 */
	protected void genAjaxErrorInfo(AjaxResponse ret, Exception e) {
		ret.setStatusCode(AjaxResponseStatusCode.FAIL);
		String uuid = UUID.randomUUID().toString();
		logger.error("ERROR_ID:" + uuid, e);
		ret.setMessage("发生内部错误<br>ERROR_ID:" + uuid + "<br>请与管理员联系");
	}
	
	/**
	 * 返回前端 Ajax 请求的结果
	 */
	protected void responseAjaxJson(HttpServletResponse response, AjaxResponse ret){
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(AjaxResponseUtil.getAjaxJson(ret));
			response.getWriter().flush();
		} catch (Exception e) {
			logger.error("Fail to response ajax json.", e);
		}
	}	
}
