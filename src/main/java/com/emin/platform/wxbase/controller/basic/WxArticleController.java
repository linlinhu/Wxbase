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
import com.emin.platform.wxbase.interfaces.basic.WxArticleApiFeign;

/**
 * 图文库逻辑控制层
 * @author kakadanica
 *
 */
@Controller
@RequestMapping("/wxArticle")
public class WxArticleController  extends BaseController {
	

	@Autowired
	WxoaApiFeign wxoaApiFeign;
	@Autowired
	WxArticleApiFeign wxArticleApiFeign;
	
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(Long woaId){
		ModelAndView mv = new ModelAndView("modules/wxArticle/manage");
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
			String res = wxArticleApiFeign.page(ecmId, woaId, getPageRequestData().getCurrentPage(), getPageRequestData().getLimit());
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
	

	@RequestMapping("/getPage")
	@ResponseBody
	public JSONObject getPages(Long woaId) {
		JSONObject json = new JSONObject();
		if(woaId == null) {
			throw new EminException("WXBASE_0.0.001");
		}
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId == null) {
			throw new EminException("WXBASE_0.0.002");
		} else {
			String res = wxArticleApiFeign.page(ecmId, woaId, getPageRequestData().getCurrentPage(), getPageRequestData().getLimit());
			if (res != null) {
				json = JSONObject.parseObject(res);
				if (!json.getBooleanValue("success")) {
					throw new EminException(json.getString("code"));
				}
				JSONObject jsonRes = json.getJSONObject("result");
				jsonRes.put("limit", getPageRequestData().getLimit());
				json.put("limit", jsonRes);
			}
		}
		
		return json;
	}
	
	@RequestMapping("/form")
	@ResponseBody
	public ModelAndView goForm(Long woaId, Long id){
		ModelAndView mv = new ModelAndView("modules/wxArticle/form");
		if(woaId == null) {
			throw new EminException("WXBASE_0.0.001");
		}
		mv.addObject("woaId", woaId);
		if (id != null) {
			Long ecmId = null;
			if (getRequest().getHeader("ecmId") != null) {
				ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
			}
			if (ecmId == null) {
				throw new EminException("WXBASE_0.0.002");
			} else {
				String res = wxArticleApiFeign.detail(ecmId, id);
				if (res != null) {
					JSONObject resJson = JSONObject.parseObject(res);
					if (!resJson.getBooleanValue("success")) {
						throw new EminException(resJson.getString("code"));
					}
					mv.addObject("wxArticle", resJson.get("result"));
				}
			}
		}
		return mv;
    }
	

	@RequestMapping("/saveWxArticle")
	@ResponseBody
	public JSONObject save(String jsonStr){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = wxArticleApiFeign.save(ecmId, jsonStr);
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
	
	@RequestMapping("/deleteWxArticle")
	@ResponseBody
	public JSONObject delete(Long woaId, Long ids){
		JSONObject json = new JSONObject();
		if(woaId == null) {
			throw new EminException("WXBASE_0.0.001");
		}
		if(ids == null) {
			throw new EminException("WXBASE_0.0.003");
		}
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = wxArticleApiFeign.delete(ecmId, woaId, ids);
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
