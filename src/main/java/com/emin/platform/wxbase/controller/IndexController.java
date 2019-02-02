package com.emin.platform.wxbase.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.emin.base.controller.BaseController;
import com.emin.platform.wxbase.interfaces.WxApiFeign;

@RestController
public class IndexController extends BaseController {
	
	@Autowired
	WxApiFeign wxApiFeign;
	
	@ResponseBody
	@RequestMapping(value = "/",method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request) throws UnsupportedEncodingException{
		ModelAndView mv = new ModelAndView("index");
		getRequest().getSession().setAttribute("base",getBasePath());
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value = "/app",method = RequestMethod.GET)
	public ModelAndView app(HttpServletRequest request) throws UnsupportedEncodingException{
		ModelAndView mv = new ModelAndView("home");
		String res = wxApiFeign.wxConfig("eminjinli", URLEncoder.encode(request.getRequestURI(), "UTF-8"));
		mv.addObject("wxConfig", res);
		mv.addObject("base", getBasePath());
		return mv;
	}
	
	
	
}
