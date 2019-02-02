package com.emin.platform.wxbase.interfaces.activity;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/***
 * 奖品管理接口桥梁定义
 * @author winnie
 */
@FeignClient(value = "zuul")
public interface AwardApiFeign {
	/**
	 * 查询主体下的所有奖品单位
	 * @param ecmId 主体编号
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/awardUnit/list",method = RequestMethod.GET)
	String awardUnitList(@RequestHeader(value="ecmId") Long ecmId);
	
	/**
	 * 删除奖品单位
	 * @param ecmId 主体编号
	 * @param id 奖品单位id
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/awardUnit/delete",method = RequestMethod.POST)
	String awardUnitDelete(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="id") Long id);
	
	/**
	 * 奖品分页查询
	 * @param ecmId 主体编号
	 * @param keyword 查询关键字
	 * @param limit 每页显示数目
	 * @param page 查询第几页
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/award/page",method = RequestMethod.GET)
	String page(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="keyword") String keyword,
			@RequestParam(value="limit") String limit,
			@RequestParam(value="page") String page);
	/**
	 * 查询所有奖品
	 * @param ecmId 主体编号
	 * @param keyword 查询关键字
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/award/list",method = RequestMethod.GET)
	String list(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="keyword") String keyword);
	
	/**
	 * 获取奖品详情
	 * @param ecmId 主体编号
	 * @param id 奖品id
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/award/{id}/info",method = RequestMethod.GET)
	String info(@RequestHeader(value="ecmId") Long ecmId,
			@PathVariable("id") Long id);
	
	/**
	 * 保存奖品
	 * @param ecmId 主体编号
	 * @param wxAward 奖品信息
	 * @return
	 */
	@RequestMapping(value = "/api-wxact/award/createOrUpdate",method = RequestMethod.POST,consumes= {"application/json"})
	String createOrUpdate(@RequestHeader(value="ecmId") Long ecmId,
			@RequestBody String wxAward);
	
	/**
	 * 奖品删除
	 * @param ecmId 主体编号
	 * @param id 奖品id
	 * @return
	 */

	@RequestMapping(value = "/api-wxact/award/delete",method = RequestMethod.POST)
	String delete(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="id") Long id);

}
