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
@RequestMapping("/activityManagement")
public class ActivityManagementController  extends BaseController {
	
	@Autowired
	ActivityManagementApiFeign activityManagementApiFeign;
	@Autowired
	AwardApiFeign awardApiFeign;
	@Autowired
	CouponApiFeign couponApiFeign;
	
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(Long createEcmId, String keyword, Long startTimeBegin, Long startTimeEnd, Integer activityStatus, Integer executeStatus){
		ModelAndView mv = new ModelAndView("modules/activityManagement/manage");
		
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId == null) {
			throw new EminException("WXBASE_0.0.002");
		} else {
			String limit = String.valueOf(getPageRequestData().getLimit());
			String page = String.valueOf(getPageRequestData().getCurrentPage());
			String topEcm  = activityManagementApiFeign.activityTopEcm(ecmId);
			if (topEcm != null) {
				JSONObject topEcmJson = JSONObject.parseObject(topEcm);
				if (!topEcmJson.getBooleanValue("success")) {
					throw new EminException(topEcmJson.getString("code"));
				}
				JSONArray topEcmList = topEcmJson.getJSONArray("result");
				if(topEcmList.size() == 0){
					createEcmId = ecmId;
				}
				if (createEcmId == null && topEcmList.size() > 0) {
					createEcmId = topEcmList.getJSONObject(0).getLong("id");
				}
				mv.addObject("topEcmList", topEcmList);
			}
			String res = activityManagementApiFeign.customActivities(createEcmId, ecmId, startTimeBegin, startTimeEnd, activityStatus, executeStatus,keyword, limit, page);
			if (res != null) {
				JSONObject resJson = JSONObject.parseObject(res);
				if (!resJson.getBooleanValue("success")) {
					throw new EminException(resJson.getString("code"));
				}
				resJson = resJson.getJSONObject("result");
				resJson.put("limit", limit);
				mv.addObject("page", resJson);
				mv.addObject("startTimeBegin", startTimeBegin);
				mv.addObject("startTimeEnd", startTimeEnd);
				mv.addObject("createEcmId", createEcmId);
			}
		}
		return mv;
    }
	//查询所有的活动
	@RequestMapping("/customActivities")
	@ResponseBody
	public JSONObject customActivities(Long createEcmId, String keyword, Long startTimeBegin, Long startTimeEnd, Integer activityStatus, Integer executeStatus){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId == null) {
			throw new EminException("WXBASE_0.0.002");
		} else {
			String limit = String.valueOf(getPageRequestData().getLimit());
			String page = String.valueOf(getPageRequestData().getCurrentPage());
			String res = activityManagementApiFeign.customActivities(createEcmId, ecmId,  startTimeBegin, startTimeEnd, activityStatus, executeStatus, keyword, limit, page);
			if (res != null) {
				json = JSONObject.parseObject(res);
				if (!json.getBooleanValue("success")) {
					throw new EminException(json.getString("code"));
				}
				JSONObject resJson = json.getJSONObject("result");
				resJson.put("limit", getPageRequestData().getLimit());
				json.put("result", resJson);
			}
			json.put("createEcmId", createEcmId);
			json.put("keyword", keyword);
			json.put("startTimeBegin", startTimeBegin);
			json.put("startTimeEnd", startTimeEnd);
			
		}
		return json;
    }
	
	
	@RequestMapping("/form")
	@ResponseBody
	public ModelAndView goForm(Long id,Long ecmId){
		ModelAndView mv = new ModelAndView("modules/activityManagement/form");
		Long creatorEcmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			creatorEcmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if(id != null){
			if (creatorEcmId== null) {
				throw new EminException("WXBASE_0.0.002");
			} else {
				String res = activityManagementApiFeign.info(creatorEcmId, id);
				if (res != null) {
					JSONObject resJson = JSONObject.parseObject(res);
					if (!resJson.getBooleanValue("success")) {
						throw new EminException(resJson.getString("code"));
					}
					mv.addObject("activity", resJson.get("result"));
				}
			}
		}
		if(creatorEcmId != null){
			mv.addObject("ecmId", ecmId);
		}
		return mv;
    }
	
	// 查询活动详情
	@RequestMapping("/info")
	@ResponseBody
	public JSONObject info(Long id){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
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
	
	// 查询收货单详情
	@RequestMapping("/findRGBySn")
	@ResponseBody
	public JSONObject findRGBySn(String sn){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId == null) {
			throw new EminException("WXBASE_0.0.002");
		} else {
			String res = activityManagementApiFeign.findRGBySn(ecmId, sn);
			if (res != null) {
				json = JSONObject.parseObject(res);
				if (!json.getBooleanValue("success")) {
					throw new EminException(json.getString("code"));
				}
			}
		}
		return json;
    }
	
	
	//保存或者编辑活动
	@RequestMapping("/saveActivityManagement")
	@ResponseBody
	public JSONObject save(String jsonStr){
		JSONObject json = new JSONObject();
		Long ecmId = JSONObject.parseObject(jsonStr).getLong("ecmId");
		if (ecmId != null) {
			String res = activityManagementApiFeign.createOrUpdate(ecmId, jsonStr);
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
	
	//删除活动
	@RequestMapping("/deleteActivityManagement")
	@ResponseBody
	public JSONObject delete(Long ids){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = activityManagementApiFeign.delete(ecmId, ids);
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
	
	//提交活动审核
	@RequestMapping("/submitToAudit")
	@ResponseBody
	public JSONObject submitToAudit(Long id,String auditor){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = activityManagementApiFeign.submitToAudit(ecmId, id, auditor);
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
	
	//获取所有的收货单
	@RequestMapping("/findAllReceivedRecord")
	@ResponseBody
	public JSONObject findAllReceivedRecord(Long deliverEcmId){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = activityManagementApiFeign.findAllReceivedRecord(ecmId, deliverEcmId);
			if (res != null) {
				json = JSONObject.parseObject(res);
				if (!json.getBooleanValue("success")) {
					throw new EminException(json.getString("code"));
				}
			}
			String usedReceiveSNStr = activityManagementApiFeign.usedReceiveSN(ecmId);
			if (usedReceiveSNStr != null) {
				JSONObject usedReceiveSNJson = JSONObject.parseObject(usedReceiveSNStr);
				if (!usedReceiveSNJson.getBooleanValue("success")) {
					throw new EminException(usedReceiveSNJson.getString("code"));
				}
				String usedReceiveSNList = usedReceiveSNJson.getString("result");
				JSONArray SNList = json.getJSONArray("result");
				for(int i = 0; i < SNList.size(); i++) {
					JSONObject SNItem = SNList.getJSONObject(i);
					Boolean isUsed = usedReceiveSNList.contains(SNItem.getString("receiveSn"));
					SNItem.put("isUsed", isUsed);
				}
				json.put("result", SNList);
			}
			
		} else {
			throw new EminException("WXBASE_0.0.002");
		}
 		return json;
    }
	
	//获取奖品、优惠券
	@RequestMapping("/getAwardCoupon")
	@ResponseBody
	public JSONObject getAward(){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		String keyword = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId == null) {
			throw new EminException("WXBASE_0.0.002");
		} else {
			String coupon = couponApiFeign.cashCouponlist(ecmId, keyword);
			String award = awardApiFeign.list(ecmId, keyword);
			if (coupon != null) {
				JSONObject couponJson = JSONObject.parseObject(coupon);
				JSONArray resultList = couponJson.getJSONArray("result");
				if (!couponJson.getBooleanValue("success")) {
					throw new EminException(couponJson.getString("code"));
				}
				for(int i=0;i<resultList.size();i++){
					JSONObject couponObj = resultList.getJSONObject(i);
					couponObj.put("fullData", couponObj.toJSONString());
				}
				json.put("coupons", resultList);
			}
			if (award != null) {
				JSONObject awardJson = JSONObject.parseObject(award);
				JSONArray resultList = awardJson.getJSONArray("result");
				if (!awardJson.getBooleanValue("success")) {
					throw new EminException(awardJson.getString("code"));
				}
				for(int i=0;i<resultList.size();i++){
					JSONObject awardObj = resultList.getJSONObject(i);
					awardObj.put("fullData", awardObj.toJSONString());
				}
				json.put("awards", resultList);
			}
		}
 		return json;
    }
}
