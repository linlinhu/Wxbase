package com.emin.platform.wxbase.controller.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
import com.emin.platform.wxbase.interfaces.activity.*;

@Controller
@RequestMapping("/awardRecord")
public class AwardRecordController  extends BaseController {
	
	@Autowired
	AwardRecordApiFeign awardRecordApiFeign;
	
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(String keyword, Long hitTimeBegin, Long hitTimeEnd, Long consumeTimeBegin, Long consumeTimeEnd, String nickName, String consumeEcmName){
		ModelAndView mv = new ModelAndView("modules/awardRecord/manage");
		
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		
		if (ecmId == null) {
			throw new EminException("WXBASE_0.0.002");
		} else {
			String limit = String.valueOf(getPageRequestData().getLimit());
			String page = String.valueOf(getPageRequestData().getCurrentPage());
			String res = awardRecordApiFeign.pagedList(ecmId, hitTimeBegin, hitTimeEnd, consumeTimeBegin, consumeTimeEnd, nickName, consumeEcmName, limit, page);
			if (res != null) {
				JSONObject resJson = JSONObject.parseObject(res);
				if (!resJson.getBooleanValue("success")) {
					throw new EminException(resJson.getString("code"));
				}
				resJson = resJson.getJSONObject("result");
				resJson.put("limit", limit);
				mv.addObject("page", resJson);
				if(hitTimeBegin != null){
					mv.addObject("hitTimeBegin", hitTimeBegin);
					mv.addObject("hitTimeEnd", hitTimeEnd);
				}
				if(consumeTimeBegin != null){
					mv.addObject("consumeTimeBegin", consumeTimeBegin);
					mv.addObject("consumeTimeEnd", consumeTimeEnd);
				}
				mv.addObject("nickName", nickName);
				mv.addObject("consumeEcmName", consumeEcmName);
			}
		}
		
		
		return mv;
    }
	
	
}
