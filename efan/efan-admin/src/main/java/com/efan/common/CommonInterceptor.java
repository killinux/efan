package com.efan.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.efan.common.GlobalConstant.AjaxResponseStatusCode;
import com.efan.mybatis.domain.UserInfo;
import com.efan.util.AjaxResponseUtil;
import com.efan.util.AjaxResponseUtil.AjaxResponse;

public class CommonInterceptor implements HandlerInterceptor {
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		
		UserInfo user = (UserInfo)request.getSession().getAttribute(GlobalConstant.SessionKey.LOGIN_USER);
		if(user == null){
			ret.setStatusCode(AjaxResponseStatusCode.TIMEOUT);
			ret.setMessage("登录失效，请重新登录");
			AjaxResponseUtil.responseJsonObject(response, ret);
			return false;
		}
		
		return true;
	}

	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		
	}
}
