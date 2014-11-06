package com.funshion.meedo.module.operation.catagory;

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
import com.funshion.meedo.mybatis.domain.CatagoryInfo;
import com.funshion.meedo.mybatis.service.CatagoryService;
import com.funshion.meedo.util.AjaxResponseUtil;
import com.funshion.meedo.util.AjaxResponseUtil.AjaxResponse;

@Controller
@RequestMapping(value="/catagory")
public class CatagoryController extends AbstractController {
	@Autowired
	private CatagoryService catagoryService;
	
	@RequestMapping(value="/list")
	public String getCatagoryList(Map<String, Object> map) {
		CatagoryPagerForm pagerForm = new CatagoryPagerForm();
		pagerForm.setOrderField("id");
		List<CatagoryInfo> catagoryList = catagoryService.findAllCatagoriesPagination(pagerForm);
		map.put("pagerForm", pagerForm);
		map.put("list", catagoryList);
		return "/jsp/operation/catagory/catagory_list.jsp";
	}
	
	@RequestMapping(value="search")
	public String searchCatagory(CatagoryPagerForm pagerForm, Map<String, Object> map) {
		List<CatagoryInfo> catagoryList = catagoryService.findAllCatagoriesPagination(pagerForm);
		map.put("pagerForm", pagerForm);
		map.put("list", catagoryList);
		return "/jsp/operation/catagory/catagory_list.jsp";
	}
	
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public String addCatagoryGet(){
		return "/jsp/operation/catagory/catagory_add.jsp";
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public void addCatagoryPost(CatagoryInfo catagoryInfo, HttpSession session, HttpServletResponse response) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			ret = catagoryService.addCatagory(catagoryInfo, getLoginUserId(session));
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}	
	}
	
	@RequestMapping(value="/remove/{id}")
	public void removeCatagory(@PathVariable("id") long id, HttpSession session, HttpServletResponse response) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			ret = catagoryService.removeCatagoryById(id, getLoginUserId(session));
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}	
	}
	
	@RequestMapping(value="/update/{id}", method=RequestMethod.GET)
	public String updateCatagoryGet(@PathVariable("id") long id, Map<String, Object> map) {
		CatagoryInfo catagory = catagoryService.getCatagoryById(id);
		map.put("entity", catagory);
		return "/jsp/operation/catagory/catagory_update.jsp";
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public void updateCatagoryPost(CatagoryInfo catagoryInfo, HttpSession session, HttpServletResponse response) {
		AjaxResponse ret = AjaxResponseUtil.getAjaxResponse();
		try {
			ret = catagoryService.updateCatagory(catagoryInfo, getLoginUserId(session));
		} catch (Exception e) {
			this.genAjaxErrorInfo(ret, e);
		} finally {
			AjaxResponseUtil.responseJsonObject(response, ret);
		}
	}
}
