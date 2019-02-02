package com.emin.platform.wxbase.interfaces;

import com.alibaba.fastjson.JSONObject;

public class FeignResultCheckUtil {

	public static boolean valid(JSONObject result){
		if(result!=null
				&& !result.isEmpty()
				&& result.containsKey("success")
				&& result.containsKey("result")
				&& result.getBoolean("success")
				&& result.get("result")!=null){
			return true;
		}
		return false;
	}
}
