package com.emin.platform.wxbase.controller.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
import com.emin.platform.wxbase.interfaces.WxoaApiFeign;
import com.emin.platform.wxbase.interfaces.basic.FansApiFeign;

@Controller
@RequestMapping("/fans")
public class FansController  extends BaseController {
	@Autowired
	WxoaApiFeign wxoaApiFeign;
	
	@Autowired
	FansApiFeign fansApiFeign;
	
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(Long woaId, String keyword){
		ModelAndView mv = new ModelAndView("modules/fans/manage");
		
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId == null) {
			throw new EminException("WXBASE_0.0.002");
		} else {
			String woaRes = wxoaApiFeign.list(ecmId);
			if (woaRes != null) {
				JSONObject woaResJson = JSONObject.parseObject(woaRes);
				if (!woaResJson.getBooleanValue("success")) {
					throw new EminException(woaResJson.getString("code"));
				}
				JSONArray woaList = woaResJson.getJSONArray("result");
				mv.addObject("woaList", woaList);
				if (woaList.size() > 0 && woaId == null) { // 默认取第一个公众号关联数据
					woaId = woaList.getJSONObject(0).getLong("id");
				}
			}
			
		}
		if (woaId != null) {
			mv.addObject("woaId", woaId);
			String res = fansApiFeign.page(ecmId, woaId, getPageRequestData().getCurrentPage(), getPageRequestData().getLimit(), keyword);
			if (res != null) {
				JSONObject resJson = JSONObject.parseObject(res);
				if (!resJson.getBooleanValue("success")) {
					throw new EminException(resJson.getString("code"));
				}

				resJson = resJson.getJSONObject("result");
				resJson.put("limit", getPageRequestData().getLimit());
				mv.addObject("page", resJson);
			}
		}
		
		if (keyword != null) {
			mv.addObject("keyword", keyword);
		}
		
		return mv;
    }
	
	
	
	
	
	
}
