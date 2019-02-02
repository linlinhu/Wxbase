package com.emin.platform.wxbase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
import com.emin.platform.wxbase.interfaces.WxoaApiFeign;

/**
 * 微信公众号管理控制层
 * @author kakadanica
 *
 */
@Controller
@RequestMapping("/wxoa")
public class WxoaController  extends BaseController {

	@Autowired
	WxoaApiFeign wxoaApiFeign;
	
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(){
		ModelAndView mv = new ModelAndView("modules/wxoa/manage");
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId == null) {
			throw new EminException("WXBASE_0.0.002");
		} else {
			String res = wxoaApiFeign.list(ecmId);
			if (res != null) {
				JSONObject resJson = JSONObject.parseObject(res);
				if (!resJson.getBooleanValue("success")) {
					throw new EminException(resJson.getString("code"));
				}
				mv.addObject("result", resJson.get("result"));
			}
		}
		return mv;
    }
	
	@RequestMapping("/form")
	@ResponseBody
	public ModelAndView goForm(Long id){
		ModelAndView mv = new ModelAndView("modules/wxoa/form");
		if (id != null) {
			Long ecmId = null;
			if (getRequest().getHeader("ecmId") != null) {
				ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
			}
			if (ecmId == null) {
				throw new EminException("WXBASE_0.0.002");
			} else {
				String res = wxoaApiFeign.detail(ecmId, id);
				if (res != null) {
					JSONObject resJson = JSONObject.parseObject(res);
					if (!resJson.getBooleanValue("success")) {
						throw new EminException(resJson.getString("code"));
					}
					mv.addObject("wxoa", resJson.get("result"));
				}
			}
		}
		return mv;
    }
	
	@RequestMapping("/saveWxoa")
	@ResponseBody
	public JSONObject saveWxoa(String jsonStr){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = wxoaApiFeign.save(ecmId, jsonStr);
			if (res != null) {
				json = JSONObject.parseObject(res);
				if (!json.getBooleanValue("success")) {
					throw new EminException(json.getString("code"));
				}
			}
		} else {
			throw new EminException("WXBASE_0.0.002");
		}
 		return json;
    }
	
	@RequestMapping("/disableWxoa")
	@ResponseBody
	public JSONObject disable(Long ids){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = wxoaApiFeign.disable(ecmId, ids);
			if (res != null) {
				json = JSONObject.parseObject(res);
				if (!json.getBooleanValue("success")) {
					throw new EminException(json.getString("code"));
				}
			}
		} else {
			throw new EminException("WXBASE_0.0.002");
		}
 		return json;
    }
	
	@RequestMapping("/enableWxoa")
	@ResponseBody
	public JSONObject enable(Long ids){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = wxoaApiFeign.enable(ecmId, ids);
			if (res != null) {
				json = JSONObject.parseObject(res);
				if (!json.getBooleanValue("success")) {
					throw new EminException(json.getString("code"));
				}
			}
		} else {
			throw new EminException("WXBASE_0.0.002");
		}
 		return json;
    }
	
	
}
