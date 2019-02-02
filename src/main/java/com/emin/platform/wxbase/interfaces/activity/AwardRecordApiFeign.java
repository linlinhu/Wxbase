package com.emin.platform.wxbase.interfaces.activity;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/***
 * 中奖及兑奖记录接口桥梁定义
 * @author winnie
 */
@FeignClient(value = "zuul")
public interface AwardRecordApiFeign {
	
	/**
	 * 中奖及兑奖记录列表获取
	 * @param ecmId 主体编号
	 * @param hitTimeBegin 中奖时间起
	 * @param hitTimeEnd 中奖时间止
	 * @param consumeTimeBegin 兑换时间起
	 * @param consumeTimeEnd 兑换时间止
	 * @param nickName 昵称
	 * @param consumeEcmName 核销方名称关键字
	 * @param limit 每页显示数目
	 * @param page 查询第几页
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/awardRecord/pagedList",method = RequestMethod.GET)
	String pagedList(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="hitTimeBegin") Long hitTimeBegin,
			@RequestParam(value="hitTimeEnd") Long hitTimeEnd,
			@RequestParam(value="consumeTimeBegin") Long consumeTimeBegin,
			@RequestParam(value="consumeTimeEnd") Long consumeTimeEnd,
			@RequestParam(value="nickName") String nickName,
			@RequestParam(value="consumeEcmName") String consumeEcmName,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="page") String page);
	

}
