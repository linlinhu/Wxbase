package com.emin.platform.wxbase.interfaces.activity;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/***
 * 优惠券管理接口桥梁定义
 * @author winnie
 */
@FeignClient(value = "zuul")
public interface CouponApiFeign {
	
	/**
	 * 优惠券分页查询
	 * @param ecmId 主体编号
	 * @param keyword 查询关键字
	 * @param limit 每页显示数目
	 * @param page 查询第几页
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/coupon/pagedCashCoupon",method = RequestMethod.GET)
	String pagedCashCoupon(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="keyword") String keyword,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="page") String page);
	/**
	 * 查询所有优惠券
	 * @param ecmId 主体编号
	 * @param keyword 查询关键字
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/coupon/cashCouponlist",method = RequestMethod.GET)
	String cashCouponlist(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="keyword") String keyword);
	
	/**
	 * 获取优惠券详情
	 * @param ecmId 主体编号
	 * @param id 优惠券id
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/coupon/{id}/cashCouponInfo",method = RequestMethod.GET)
	String cashCouponInfo(@RequestHeader(value="ecmId") Long ecmId,
			@PathVariable("id") Long id);
	
	/**
	 * 保存优惠券
	 * @param ecmId 主体编号
	 * @param cashCoupon 优惠券信息
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/coupon/saveCashCoupon",method = RequestMethod.POST,consumes= {"application/json"})
	String saveCashCoupon(@RequestHeader(value="ecmId") Long ecmId,
			@RequestBody String cashCoupon);
	
	/**
	 * 优惠券删除
	 * @param ecmId 主体编号
	 * @param id 优惠券id
	 * @return
	 */

	@RequestMapping(value = "/api-wxact/coupon/delete",method = RequestMethod.POST)
	String delete(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="id") Long id);

}
