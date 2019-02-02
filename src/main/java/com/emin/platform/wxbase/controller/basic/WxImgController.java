package com.emin.platform.wxbase.controller.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
import com.emin.platform.wxbase.interfaces.basic.WxImgApiFeign;

/**
 * 图文库逻辑控制层
 * @author kakadanica
 *
 */
@Controller
@RequestMapping("/wxImg")
public class WxImgController  extends BaseController {
	

	@Autowired
	WxImgApiFeign wxImgApiFeign;
	
	@RequestMapping("/getPage")
	@ResponseBody
	public JSONObject goManage(Long woaId){
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
			String res = wxImgApiFeign.page(ecmId, woaId, getPageRequestData().getCurrentPage(), getPageRequestData().getLimit());
			if (res != null) {
				json = JSONObject.parseObject(res);
				if (!json.getBoolean("success")) {
					throw new EminException(json.getString("code"));
				}

				JSONObject resJson = json.getJSONObject("result");
				resJson.put("limit", getPageRequestData().getLimit());
				json.put("result", resJson);

			}
			
		}
		return json;
    }
	
	@RequestMapping("/add")
	@ResponseBody
	public JSONObject addImg(Long woaId, String name, String path, Long size){
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
			String res = wxImgApiFeign.add(ecmId, woaId, name, path, size);
			if (res != null) {
				json = JSONObject.parseObject(res);
				if (!json.getBooleanValue("success")) {
					throw new EminException(json.getString("code"));
				}
			}
		}
		return json;
    }
	

	@RequestMapping("/delete")
	@ResponseBody
	public JSONObject delete(Long id){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = wxImgApiFeign.delete(ecmId, id);
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
