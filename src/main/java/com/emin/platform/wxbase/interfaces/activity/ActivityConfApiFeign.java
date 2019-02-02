package com.emin.platform.wxbase.interfaces.activity;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/***
 * 活动配置接口桥梁定义
 * @author winnie
 */
@FeignClient(value = "zuul")
public interface ActivityConfApiFeign {
	
	/**
	 * 获取主体的一级经销商
	 * @param ecmId 主体编号
	 * @return
	 */
	@RequestMapping(value = "/api-dm/dis/findAllOneLevelDis",method = RequestMethod.GET)
	String findAllOneLevelDis(@RequestHeader(value="ecmId") Long ecmId);
	
	/**
	 * 获取活动配置
	 * @param ecmId 主体编号
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/wxActivityConf/info",method = RequestMethod.GET)
	String info(@RequestHeader(value="ecmId") Long ecmId);
	
	/**
	 * 保存活动配置
	 * @param ecmId 主体编号
	 * @param wxActivityConf 活动配置信息
	 * @return
	 */
	@RequestMapping(value = "api-wxact/wxActivityConf/update",method = RequestMethod.POST,consumes= {"application/json"})
	String update(@RequestHeader(value="ecmId") Long ecmId,
			@RequestBody String wxActivityConf);

}
