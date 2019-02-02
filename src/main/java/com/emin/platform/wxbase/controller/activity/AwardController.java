package com.emin.platform.wxbase.controller.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
/*import com.emin.platform.wxbase.interfaces.WxoaApiFeign;*/
import com.emin.platform.wxbase.interfaces.activity.*;

@Controller
@RequestMapping("/award")
public class AwardController  extends BaseController {
	
	@Autowired
	AwardApiFeign awardApiFeign;
	
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(Long woaId, String keyword){
		ModelAndView mv = new ModelAndView("modules/award/manage");
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId == null) {
			throw new EminException("WXBASE_0.0.002");
		} else {
			String limit = String.valueOf(getPageRequestData().getLimit());
			String page = String.valueOf(getPageRequestData().getCurrentPage());
			String res = awardApiFeign.page(ecmId, keyword, limit, page);
			if (res != null) {
				JSONObject resJson = JSONObject.parseObject(res);
				if (!resJson.getBooleanValue("success")) {
					throw new EminException(resJson.getString("code"));
				}
				resJson = resJson.getJSONObject("result");
				resJson.put("limit", limit);
				mv.addObject("page", resJson);
			}
		}
		if (keyword != null) {
			mv.addObject("keyword", keyword);
		}
		return mv;
    }
	
	@RequestMapping("/form")
	@ResponseBody
	public ModelAndView goForm(Long id){
		ModelAndView mv = new ModelAndView("modules/award/form");
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if(id != null){
			if (ecmId == null) {
				throw new EminException("WXBASE_0.0.002");
			} else {
				String res = awardApiFeign.info(ecmId, id);
				if (res != null) {
					JSONObject resJson = JSONObject.parseObject(res);
					if (!resJson.getBooleanValue("success")) {
						throw new EminException(resJson.getString("code"));
					}
					mv.addObject("award", resJson.get("result"));
				}
			}
		}
		return mv;
    }
	
	//保存或者编辑奖品
	@RequestMapping("/saveAward")
	@ResponseBody
	public JSONObject save(String jsonStr){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = awardApiFeign.createOrUpdate(ecmId, jsonStr);
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
	
	//删除奖品
	@RequestMapping("/deleteAward")
	@ResponseBody
	public JSONObject delete(Long ids){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = awardApiFeign.delete(ecmId, ids);
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
	
	//查询主体下的所有奖品单位
	@RequestMapping("/awardUnitList")
	@ResponseBody
	public JSONObject awardUnitList(){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = awardApiFeign.awardUnitList(ecmId);
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
	
	//删除奖品单位
	@RequestMapping("/awardUnitDelete")
	@ResponseBody
	public JSONObject awardUnitDelete(Long id){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = awardApiFeign.awardUnitDelete(ecmId, id);
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
