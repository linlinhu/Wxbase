package com.emin.platform.wxbase.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
import com.emin.platform.wxbase.interfaces.PersonApiFeign;

@Controller
public class LoginController extends BaseController{
	private Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private PersonApiFeign personApiFeign;
	
	@RequestMapping("/login")
	@ResponseBody
	public ModelAndView goManage(String keyword, Long ecmId){
		ModelAndView mv = new ModelAndView("modules/login/manage");
		getRequest().getSession().setAttribute("title","微信基础支撑服务平台");
		getRequest().getSession().setAttribute("base",getBasePath());
		return mv;
		
	}
	
	@RequestMapping("/getValidImg")
	@ResponseBody
	public byte[] getImg(){
		
		try {
			return personApiFeign.getImg();
		} catch (EminException e) {
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
    }
	
	@RequestMapping("/loginIn")
	@ResponseBody
	public JSONObject userLogin(String username, String password, String code){
		JSONObject res = new JSONObject();
		
		
		res = personApiFeign.login(username, password, code);
		if (res != null && res.getBoolean("success") == true) {	
			
			JSONObject person = new JSONObject();
			JSONObject ecm = new JSONObject();
			
			Long id = res.getJSONObject("data").getLong("id");
			person = personApiFeign.detail(id);
			ecm = personApiFeign.ecmDetail(person.getJSONObject("data").getLong("ecmId"));
			if (!ecm.getBooleanValue("success")) {
				throw new EminException(ecm.getString("code"));
			}
			res.put("ecmId", person.getJSONObject("data").getString("ecmId"));
			res.put("person", person.getJSONObject("data"));
			res.put("ecmIdName", ecm.getJSONObject("result").getString("name"));
			
			getRequest().getSession().setAttribute("person", person.getJSONObject("data"));
		} else {
			throw new EminException(res.getString("code"));
		}
			
		return res;
    }
	
	@RequestMapping("/logout")
	@ResponseBody
	public JSONObject logout(String token){
		JSONObject res = new JSONObject();
		
		res = personApiFeign.outLogin(token);
		if (!res.getBooleanValue("success")) {
			throw new EminException(res.getString("code"));
		}
		getRequest().getSession().removeAttribute("user");
		getRequest().getSession().removeAttribute("token");
		getRequest().getSession().invalidate();

		return res;
    }
	
	//验证用户是否登录
	@RequestMapping("/userValidate")
	@ResponseBody
	public JSONObject userValidate(String token){
		JSONObject res = new JSONObject();
		
		res = personApiFeign.userValidate(token);
		
		return res;
    }
	
}