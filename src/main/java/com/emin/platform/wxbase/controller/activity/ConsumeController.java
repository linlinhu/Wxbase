package com.emin.platform.wxbase.controller.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
import com.emin.platform.wxbase.interfaces.activity.*;


@Controller
@RequestMapping("/consume")
public class ConsumeController  extends BaseController {
	
	@Autowired
	ConsumeApiFeign consumeApiFeign;
	
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(String ecmIds, String ecmIdName,String awardName, Long startTime, Long endTime){
		ModelAndView mv = new ModelAndView("modules/consume/manage");
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId == null) {
			throw new EminException("WXBASE_0.0.002");
		} else {
			String limit = String.valueOf(getPageRequestData().getLimit());
			String page = String.valueOf(getPageRequestData().getCurrentPage());
			String res = consumeApiFeign.pagedConsumeRecord(ecmId, ecmIds, awardName, startTime, endTime, limit, page);
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
		if (ecmIds != null) {
			mv.addObject("ecmIds", ecmIds);
			mv.addObject("ecmIdName", ecmIdName);
		}
		if (awardName != null) {
			mv.addObject("awardName", awardName);
		}
		if (startTime != null) {
			mv.addObject("startTime", startTime);
			mv.addObject("endTime", endTime);
		}
		return mv;
    }
	
	//保存或者编辑优惠券
	@RequestMapping("/findTree")
	@ResponseBody
	public JSONArray findTree(Long parentId){
		JSONArray tree = new JSONArray();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = consumeApiFeign.findTree(ecmId, parentId);
			if (res != null) {
				JSONObject json = JSONObject.parseObject(res);
				if (!json.getBooleanValue("success")) {
					throw new EminException(json.getString("code"));
				}
				tree = json.getJSONArray("result");
			}
		} else {
			throw new EminException("WXBASE_0.0.002");
		}
 		return tree;
    }
}
