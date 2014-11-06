package com.funshion.meedo.module.home;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.funshion.meedo.common.AbstractController;
import com.funshion.meedo.common.GlobalConstant;
import com.funshion.meedo.common.GlobalConstant.AjaxResponseStatusCode;
import com.funshion.meedo.mybatis.domain.UserInfo;
import com.funshion.meedo.mybatis.service.UserService;
import com.funshion.meedo.util.AjaxResponseUtil;
import com.funshion.meedo.util.AjaxResponseUtil.AjaxResponse;
import com.funshion.meedo.util.MD5Util;

/**
 * 首页
 * @author weiyl
 *
 */
@Controller
public class HomeController extends AbstractController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/")
	public String prepare() {
		return "/login";
	}
	
	@RequestMapping(value="/login")
	public String showLogin() {
		return "/jsp/user/user_login.jsp";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String doLogin(HttpSession session, HttpServletRequest request, Map<String, Object> map) {
		if(doLoginCommon(session, request, map)) {
			return "/jsp/home/home.jsp";
		} else {
			return "/jsp/user/user_login.jsp";
		}
	}

	/**
	 * 跳转到登录对话框页面
	 */
	@RequestMapping(value = "/login_dialog", method = RequestMethod.GET)
	public String loginDialog() {
		return "/jsp/home/login_dialog.jsp";
	}
	
	/**
	 * 登录对话框的登录
	 */
	@RequestMapping(value = "/login_dialog", method = RequestMethod.POST)
	public void doChangePwd(HttpSession session, 
			HttpServletRequest request, 
			HttpServletResponse response, 
			Map<String, Object> map) {
		AjaxResponse res = AjaxResponseUtil.getAjaxResponse();
		try {
			if (doLoginCommon(session, request, map)){
				res.setStatusCode(AjaxResponseStatusCode.SUCCESS);
				res.setCallbackType("closeCurrent");
				res.setMessage("登录成功");
			} else {
				res.setStatusCode(AjaxResponseStatusCode.FAIL);
				res.setMessage("用户名或密码错误");
			}
		} catch (Exception e) {
			genAjaxErrorInfo(res, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, res);
		}
	}	
	
	/**
	 * 检查用户是否可以登录，如果可以放必要的内容在session和页面流里
	 */
	private boolean doLoginCommon(HttpSession session, HttpServletRequest request, Map<String, Object> map) {
		String username = request.getParameter("login_username");
		String password = request.getParameter("login_password");
		
		UserInfo loginUser = userService.getUserByLoginName(username);
		if(loginUser != null){
			if (loginUser.getIsDeleted()) {
				map.put("errMsg", "该用户被禁止登陆");
				return false;
			}
			
			password = MD5Util.getStringMD5String(password);
			if (!StringUtils.isBlank(loginUser.getPassword()) && loginUser.getPassword().equals(password)) {
				// 登录成功
				session.setAttribute(GlobalConstant.SessionKey.LOGIN_USER, loginUser);
				putSessionInfosToFlow(session, map);
				map.put("auth", loginUser.getRole());
				map.put("displayName", loginUser.getDisplayName());
				return true;
			}
		}
		
		map.put("errMsg", "用户名或密码错误");
		return false;
	}	
	
	@RequestMapping(value="/logout")
	public String doLogout() {
		return "/";
	}	
}
