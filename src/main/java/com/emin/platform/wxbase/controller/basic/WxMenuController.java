package com.emin.platform.wxbase.controller.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
import com.emin.platform.wxbase.interfaces.WxoaApiFeign;
import com.emin.platform.wxbase.interfaces.basic.WxMenuApiFeign;

@Controller
@RequestMapping("/wxMenu")
public class WxMenuController  extends BaseController {

	@Autowired
	WxoaApiFeign wxoaApiFeign;
	@Autowired
	WxMenuApiFeign wxMenuApiFeign;
	
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(){
		ModelAndView mv = new ModelAndView("modules/wxMenu/manage");
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
				mv.addObject("woaList", resJson.get("result"));
			}
		}
		return mv;
    }
	
	@RequestMapping("/load")
	@ResponseBody
	public JSONObject getWxMenus(Long woaId, Long pid){
		JSONObject json = new JSONObject();
		if (woaId != null) {
			Long ecmId = null;
			if (getRequest().getHeader("ecmId") != null) {
				ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
			}
			if (ecmId != null) {
				String res = wxMenuApiFeign.list(ecmId, woaId, pid);
				if (res != null) {
					json = JSONObject.parseObject(res);
					if (!json.getBooleanValue("success")) {
						throw new EminException(json.getString("code"));
					}
				}
			} else {
				throw new EminException("WXBASE_0.0.002");
			}
		} else {
			throw new EminException("WXBASE_0.0.001");
		}
 		return json;
    }

	@RequestMapping("/saveWxMenu")
	@ResponseBody
	public JSONObject saveWxmenu(String jsonStr){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = wxMenuApiFeign.save(ecmId, jsonStr);
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
	
	@RequestMapping("/deleteWxMenu")
	@ResponseBody
	public JSONObject deleteWxmenu(Long id){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = wxMenuApiFeign.delete(ecmId, id);
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
	
	@RequestMapping("/generate")
	@ResponseBody
	public JSONObject generateWxmenu(Long woaId){
		JSONObject json = new JSONObject();
		if (woaId != null) {
			Long ecmId = null;
			if (getRequest().getHeader("ecmId") != null) {
				ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
			}
			if (ecmId != null) {
				String res = wxMenuApiFeign.pulish(ecmId, woaId);
				if (res != null) {
					json = JSONObject.parseObject(res);
					if (!json.getBooleanValue("success")) {
						throw new EminException(json.getString("code"));
					}
				}
			} else {
				throw new EminException("WXBASE_0.0.002");
			}
		} else {
			throw new EminException("WXBASE_0.0.001");
		}
 		return json;
    }
}