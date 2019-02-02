package com.emin.platform.wxbase.interfaces.activity;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/***
 * 活动管理接口桥梁定义
 * @author winnie
 */
@FeignClient(value = "zuul")
public interface ActivityManagementApiFeign {
	/**
	 * 获取主体的上级厂商
	 * @param ecmId 主体编号
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/wxactivity/activityTopEcm",method = RequestMethod.GET)
	String activityTopEcm(@RequestHeader(value="ecmId") Long ecmId);
	
	/**
	 * 报备方活动列表获取
	 * @param ecmId 主体编号
	 * @param createEcmId 活动厂商的ecmId
	 * @param startTimeBegin 活动开始时间起
	 * @param startTimeEnd 活动开始时间止
	 * @param activityStatus 活动状态
	 * @param executeStatus 活动执行状态
	 * @param keyword 查询关键字
	 * @param limit 每页显示数目
	 * @param page 查询第几页
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/wxactivity/{createEcmId}/customActivities",method = RequestMethod.GET)
	String customActivities(@RequestHeader(value="ecmId") Long ecmId,
			@PathVariable("createEcmId") Long createEcmId,
			@RequestParam(value="startTimeBegin") Long startTimeBegin,
			@RequestParam(value="startTimeEnd") Long startTimeEnd,
			@RequestParam(value="activityStatus") Integer activityStatus,
			@RequestParam(value="executeStatus") Integer executeStatus,
			@RequestParam(value="keyword") String keyword,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="page") String page);
	/**
	 * 获取主体已经使用的收货单信息
	 * @param ecmId 主体编号
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/wxactivity/usedReceiveSN",method = RequestMethod.GET)
	String usedReceiveSN(@RequestHeader(value="ecmId") Long ecmId);
	
	/**
	 * 获取主体的所有收货单信息
	 * @param ecmId 主体编号
	 * @param deliverEcmId 发货方的主体id
	 * @return
	 */
	@RequestMapping(value = "/api-rdg/rg/findAllReceivedRecord",method = RequestMethod.GET)
	String findAllReceivedRecord(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="deliverEcmId") Long deliverEcmId);
	
	/**
	 * 查询收货单详情
	 * @param ecmId 主体编号
	 * @param sn 收货单号
	 * @return
	 */
	@RequestMapping(value = "/api-rdg/rg/findRGBySn",method = RequestMethod.GET)
	String findRGBySn(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="sn") String sn);
	
	/**
	 * 获取活动详情
	 * @param ecmId 主体编号
	 * @param activityId 活动id
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/wxactivity/{activityId}/info",method = RequestMethod.GET)
	String info(@RequestHeader(value="ecmId") Long ecmId,
			@PathVariable("activityId") Long activityId);
	
	/**
	 * 保存活动
	 * @param ecmId 主体编号
	 * @param wxActivity 活动信息
	 * @return
	 */
	@RequestMapping(value = "api-wxact/wxactivity/createOrUpdate",method = RequestMethod.POST,consumes= {"application/json"})
	String createOrUpdate(@RequestHeader(value="ecmId") Long ecmId,
			@RequestBody String wxActivity);
	
	/**
	 * 活动删除
	 * @param ecmId 主体编号
	 * @param id 活动id
	 * @return
	 */

	@RequestMapping(value = "/api-wxact/wxactivity/delete",method = RequestMethod.POST)
	String delete(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="id") Long id);
	
	/**
	 * 提交活动审核
	 * @param ecmId 主体编号
	 * @param activityId 活动id
	 * @param auditor 用户信息
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/wxactivity/submitToAudit",method = RequestMethod.POST,consumes= {"application/json"})
	String submitToAudit(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="activityId") Long activityId,
			@RequestBody String auditor);

}
