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
@RequestMapping("/audit")
public class AuditController  extends BaseController {
	
	@Autowired
	ActivityManagementApiFeign activityManagementApiFeign;
	@Autowired
	AuditApiFeign auditApiFeign;
	
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(String keyword, Long startTimeBegin, Long startTimeEnd, Integer activityStatus){
		ModelAndView mv = new ModelAndView("modules/audit/manage");
		
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		
		if (ecmId == null) {
			throw new EminException("WXBASE_0.0.002");
		} else {
			String limit = String.valueOf(getPageRequestData().getLimit());
			String page = String.valueOf(getPageRequestData().getCurrentPage());
			String res = auditApiFeign.activities(ecmId, startTimeBegin, startTimeEnd, activityStatus, keyword, limit, page);
			if (res != null) {
				JSONObject resJson = JSONObject.parseObject(res);
				if (!resJson.getBooleanValue("success")) {
					throw new EminException(resJson.getString("code"));
				}
				resJson = resJson.getJSONObject("result");
				resJson.put("limit", limit);
				mv.addObject("page", resJson);
				if(startTimeBegin != null){
					mv.addObject("startTimeBegin", startTimeBegin);
					mv.addObject("startTimeEnd", startTimeEnd);
				}
				mv.addObject("keyword", keyword);
			}
		}
		
		
		return mv;
    }
	
	@RequestMapping("/form")
	@ResponseBody
	public ModelAndView goForm(Long id){
		ModelAndView mv = new ModelAndView("modules/audit/form");
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if(id != null){
			if (ecmId == null) {
				throw new EminException("WXBASE_0.0.002");
			} else {
				String res = activityManagementApiFeign.info(ecmId, id);
				if (res != null) {
					JSONObject resJson = JSONObject.parseObject(res);
					if (!resJson.getBooleanValue("success")) {
						throw new EminException(resJson.getString("code"));
					}
					mv.addObject("activity", resJson.get("result"));
				}
			}
		}	
		return mv;
    }
	
	// 查询活动详情
	@RequestMapping("/info")
	@ResponseBody
	public JSONObject info(Long id, Long ecmId){
		JSONObject json = new JSONObject();
		
		if (ecmId == null) {
			if (getRequest().getHeader("ecmId") != null) {
				ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
			}
		}
		if (ecmId == null) {
			throw new EminException("WXBASE_0.0.002");
		} else {
			String res = activityManagementApiFeign.info(ecmId, id);
			if (res != null) {
				json = JSONObject.parseObject(res);
				if (!json.getBooleanValue("success")) {
					throw new EminException(json.getString("code"));
				}
				JSONArray mrList = json.getJSONObject("result").getJSONArray("mrList");
				JSONArray receiveList = new JSONArray();
				for(int i = 0; i < mrList.size(); i++ ) {
					String sn = mrList.getJSONObject(i).getString("receiveSN");
					String receiveItem = activityManagementApiFeign.findRGBySn(ecmId, sn);
					JSONObject receiveItemJson = JSONObject.parseObject(receiveItem);
					if (!receiveItemJson.getBooleanValue("success")) {
						throw new EminException(receiveItemJson.getString("code"));
					}
					receiveList.add(receiveItemJson.getJSONObject("result"));
				}
				json.getJSONObject("result").put("receiveList", receiveList);
			}
		}
		
		return json;
    }
	
	// 审核
	@RequestMapping("/audit")
	@ResponseBody
	public JSONObject audit(Long ecmId, String auditLog){
		JSONObject json = new JSONObject();
		
		String res = auditApiFeign.audit(ecmId, auditLog);
		if (res != null) {
			json = JSONObject.parseObject(res);
			if (!json.getBooleanValue("success")) {
				throw new EminException(json.getString("code"));
			}
		}
		
		return json;
    }
	
	//活动审核记录
	@RequestMapping("/auditLog")
	@ResponseBody
	public JSONObject auditLog(Long id){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = auditApiFeign.auditLog(ecmId, id);
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
