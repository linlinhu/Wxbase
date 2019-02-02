package com.emin.platform.wxbase.interfaces.basic;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "zuul")
public interface WxImgApiFeign {
	
	@RequestMapping(value = "/api-wx/wximg/{woaId}/pagedImgs",method = RequestMethod.GET)
	String page(@RequestHeader(value="ecmId") Long ecmId,
			@PathVariable("woaId") Long woaId,
			@RequestParam(value="page") Integer page,
			@RequestParam(value="limit") Integer limit);
	

	@RequestMapping(value = "/api-wx/wximg/{woaId}/add",method = RequestMethod.POST)
	String add(@RequestHeader(value="ecmId") Long ecmId,
			@PathVariable("woaId") Long woaId,
			@RequestParam("name") String name,
			@RequestParam("path") String path,
			@RequestParam("size") Long size);
	
	@RequestMapping(value = "/api-wx/wximg/delete",method = RequestMethod.POST)
	String delete(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="id") Long id);
	
}
