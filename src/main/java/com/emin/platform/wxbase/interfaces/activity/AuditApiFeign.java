package com.emin.platform.wxbase.interfaces.activity;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/***
 * 活动审核接口桥梁定义
 * @author winnie
 */
@FeignClient(value = "zuul")
public interface AuditApiFeign {
	
	/**
	 * 审核方活动列表获取
	 * @param ecmId 主体编号
	 * @param startTimeBegin 活动开始时间起
	 * @param startTimeEnd 活动开始时间止
	 * @param activityStatus 活动状态
	 * @param executeStatus 活动执行状态
	 * @param keyword 查询关键字
	 * @param limit 每页显示数目
	 * @param page 查询第几页
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/wxactivity/activities",method = RequestMethod.GET)
	String activities(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="startTimeBegin") Long startTimeBegin,
			@RequestParam(value="startTimeEnd") Long startTimeEnd,
			@RequestParam(value="activityStatus") Integer activityStatus,
			@RequestParam(value="keyword") String keyword,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="page") String page);
	
	/**
	 * 审核活动
	 * @param ecmId 主体编号
	 * @param auditLog 审核信息
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/wxActivityAudit/audit",method = RequestMethod.POST,consumes= {"application/json"})
	String audit(@RequestHeader(value="ecmId") Long ecmId,
			@RequestBody String auditLog);
	
	/**
	 * 活动审核记录
	 * @param ecmId 主体编号
	 * @param wxActivityId 活动id
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/wxActivityAudit/{wxActivityId}/auditLog",method = RequestMethod.GET)
	String auditLog(@RequestHeader(value="ecmId") Long ecmId,
			@PathVariable("wxActivityId") Long wxActivityId);

}
