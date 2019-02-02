package com.emin.platform.wxbase.interfaces.activity;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/***
 * 核销记录接口桥梁定义
 * @author winnie
 */
@FeignClient(value = "zuul")
public interface ConsumeApiFeign {
	
	/**
	 * 核销记录列表获取
	 * @param ecmId 主体编号
	 * @param ecmIds 核销方id,可以传多个
	 * @param awardName 奖品关键字
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param limit 每页显示数目
	 * @param page 查询第几页
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/consume/pagedConsumeRecord",method = RequestMethod.GET)
	String pagedConsumeRecord(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="ecmIds") String ecmIds,
			@RequestParam(value="awardName") String awardName,
			@RequestParam(value="startTime") Long startTime,
			@RequestParam(value="endTime") Long endTime,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="page") String page);
	
	/**
	 * 获取分销商树
	 * @param ecmId 主体编号
	 * @param parentNodeId 符节点的id
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/consume/consumerTree",method = RequestMethod.GET)
	String findTree(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="parentNodeId") Long parentNodeId);
	
}
