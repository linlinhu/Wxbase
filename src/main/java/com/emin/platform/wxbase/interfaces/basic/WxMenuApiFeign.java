package com.emin.platform.wxbase.interfaces.basic;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "zuul")
public interface WxMenuApiFeign {
	
	@RequestMapping(value = "/api-wx/wxMenu/{woaId}/{pid}/menus",method = RequestMethod.GET)
	String list(@RequestHeader(value="ecmId") Long ecmId,
			@PathVariable("woaId") Long woaId,
			@PathVariable("pid") Long pid);
	
	
	@RequestMapping(value = "/api-wx/wxMenu/createOrUpdate",method = RequestMethod.POST,consumes= {"application/json"})
	String save(@RequestHeader(value="ecmId") Long ecmId,
			@RequestBody String menu);
	

	@RequestMapping(value = "/api-wx/wxMenu/remove",method = RequestMethod.POST)
	String delete(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="id") Long id);
	

	@RequestMapping(value = "/api-wx/wxMenu/{woaId}/eventMenus",method = RequestMethod.GET)
	String eventMenus(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="woaId") Long woaId);
	
	


	@RequestMapping(value = "/api-wx/wxMenu/{woaId}/generate",method = RequestMethod.GET)
	String pulish(@RequestHeader(value="ecmId") Long ecmId,
			@PathVariable("woaId") Long woaId);

}
