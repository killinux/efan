package com.funshion.meedo.module.user;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.funshion.meedo.common.AbstractController;
import com.funshion.meedo.common.GlobalConstant;
import com.funshion.meedo.mybatis.domain.UserInfo;
import com.funshion.meedo.mybatis.service.UserService;
import com.funshion.meedo.util.AjaxResponseUtil;
import com.funshion.meedo.util.AjaxResponseUtil.AjaxResponse;

/**
 * 用户权限控制
 * @author weiyl
 *
 */
@Controller
@RequestMapping(value="/user")
public class UserController extends AbstractController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/list")
	public String listUser(Map<String, Object> map) {
		UserPagerForm pagerForm = new UserPagerForm();
		pagerForm.setUserListSearchIsDeleted(-1);
		generatePageContent(pagerForm, map);
		
		return "/jsp/user/user_list.jsp";
	}
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public String searchUser(UserPagerForm pagerForm, Map<String, Object> map) {
		generatePageContent(pagerForm, map);
		
		return "/jsp/user/user_list.jsp";
	}
	
	private void generatePageContent(UserPagerForm pagerForm, Map<String, Object> map) {
		List<UserInfo> userList = userService.findAllUser(pagerForm);
		map.put("pagerForm", pagerForm);
		map.put("list", userList);
	}
	
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public String addUser() {
		return "/jsp/user/user_add.jsp";
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public void doAddUser(UserInfo userInfo, HttpSession session, HttpServletResponse response) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			ret = userService.addUser(userInfo, this.getLoginUserId(session));
		} catch(Exception e) {
			genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
	
	@RequestMapping(value="/update/{id}", method=RequestMethod.GET)
	public String updateUser(@PathVariable("id") long id, Map<String, Object> map) {
		UserInfo userInfo = userService.getUserById(id);
		map.put("entity", userInfo);
		return "/jsp/user/user_update.jsp";
	}

	@RequestMapping(value="/update", method=RequestMethod.POST)
	public void doUpdateUser(UserInfo userInfo, HttpSession session, HttpServletResponse response) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			long opUserId = this.getLoginUserId(session);
			UserInfo opUser = userService.getUserById(opUserId);
			if(opUser.getRole() == GlobalConstant.User.Auth.ADMIN) {
				ret = userService.updateUserById(userInfo, this.getLoginUserId(session));
			} else {
				ret.setStatusCode(GlobalConstant.AjaxResponseStatusCode.FAIL);
				ret.setMessage("对不起，只有管理员才可以修改用户信息");
			}
		} catch (Exception e) {
			genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
}
