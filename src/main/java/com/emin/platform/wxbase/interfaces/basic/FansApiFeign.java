package com.emin.platform.wxbase.interfaces.basic;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "zuul")
public interface FansApiFeign {
	
	@RequestMapping(value = "/api-wx/fans/{woaId}/page",method = RequestMethod.GET)
	String page(@RequestHeader(value="ecmId") Long ecmId,
			@PathVariable("woaId") Long woaId,
			@RequestParam(value="page") Integer page,
			@RequestParam(value="limit") Integer limit,
			@RequestParam(value="keyword") String keyword);
	

	@RequestMapping(value = "/api-wx/fans/{woaId}/{openId}/info",method = RequestMethod.GET)
	String detailByOpenId(@RequestHeader(value="ecmId") Long ecmId,
			@PathVariable("woaId") Long woaId,
			@PathVariable("openId") Long openId);
	

	@RequestMapping(value = "/api-wx/fans/{woaId}/{unionId}/info_unionId",method = RequestMethod.GET)
	String detailByUnionId(@RequestHeader(value="ecmId") Long ecmId,
			@PathVariable("woaId") Long woaId,
			@PathVariable("unionId") Long unionId);
	
}
