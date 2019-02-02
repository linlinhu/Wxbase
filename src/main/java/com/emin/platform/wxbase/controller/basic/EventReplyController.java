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
import com.emin.platform.wxbase.interfaces.basic.EventReplyApiFeign;
import com.emin.platform.wxbase.interfaces.basic.WxMenuApiFeign;

@Controller
@RequestMapping("/eventReply")
public class EventReplyController  extends BaseController {
	
	@Autowired
	WxoaApiFeign wxoaApiFeign;
	
	@Autowired
	EventReplyApiFeign eventReplyApiFeign;

	@Autowired
	WxMenuApiFeign wxMenuApiFeign;
	
	
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(Long woaId, String keyword){
		ModelAndView mv = new ModelAndView("modules/eventReply/manage");
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
			String res = eventReplyApiFeign.page(ecmId, woaId, getPageRequestData().getCurrentPage(), getPageRequestData().getLimit(), keyword);
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
		return mv;
    }
	
	@RequestMapping("/form")
	@ResponseBody
	public ModelAndView goForm(Long woaId, Long id){
		ModelAndView mv = new ModelAndView("modules/eventReply/form");
		if (woaId != null) {
			mv.addObject("woaId", woaId);
			Long ecmId = null;
			if (getRequest().getHeader("ecmId") != null) {
				ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
			}
			if (ecmId == null) {
				throw new EminException("WXBASE_0.0.002");
			} else {

				String eventMenusStr = wxMenuApiFeign.eventMenus(ecmId, woaId);
				if (eventMenusStr != null) {
					JSONObject eventMenus = JSONObject.parseObject(eventMenusStr);
					if (!eventMenus.getBooleanValue("success")) {
						throw new EminException(eventMenus.getString("code"));
					}
					mv.addObject("eventMenus", eventMenus.get("result"));
				}
				if (id != null) {
					String res = eventReplyApiFeign.detail(ecmId, id);
					if (res != null) {
						JSONObject resJson = JSONObject.parseObject(res);
						if (!resJson.getBooleanValue("success")) {
							throw new EminException(resJson.getString("code"));
						}
						mv.addObject("eventReply", resJson.get("result"));
					}
				}
			}
			
		}
		return mv;
    }
	
	@RequestMapping("/saveEventReply")
	@ResponseBody
	public JSONObject save(String jsonStr, String articleIds){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = eventReplyApiFeign.save(ecmId, jsonStr, articleIds);
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
	
	@RequestMapping("/deleteEventReply")
	@ResponseBody
	public JSONObject delete(Long ids){
		JSONObject json = new JSONObject();
		if(ids == null) {
			throw new EminException("WXBASE_0.0.003");
		}
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = eventReplyApiFeign.delete(ecmId, ids);
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
