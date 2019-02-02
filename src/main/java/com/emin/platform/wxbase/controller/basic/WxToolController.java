package com.emin.platform.wxbase.controller.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.emin.base.controller.BaseController;

@Controller
@RequestMapping("/wxresource")
public class WxToolController  extends BaseController {
	private Logger logger = LoggerFactory.getLogger(WxToolController.class);

	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(){
		logger.info("查询..");
		ModelAndView mv = new ModelAndView("modules/wxresource/manage");
		return mv;
    }
	
	@RequestMapping("/form")
	@ResponseBody
	public ModelAndView goForm(){
		
		ModelAndView mv = new ModelAndView("modules/wxresource/form");
		return mv;
    }
	
	
}
