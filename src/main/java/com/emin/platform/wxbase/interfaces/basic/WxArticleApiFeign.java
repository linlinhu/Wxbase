package com.emin.platform.wxbase.interfaces.basic;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "zuul")
public interface WxArticleApiFeign {
	
	@RequestMapping(value = "/api-wx/wxArticle/{woaId}/page",method = RequestMethod.POST)
	String page(@RequestHeader(value="ecmId") Long ecmId,
			@PathVariable("woaId") Long woaId,
			@RequestParam(value="page") Integer page,
			@RequestParam(value="limit") Integer limit);
	

	@RequestMapping(value = "/api-wx/wxArticle/{id}/info",method = RequestMethod.POST)
	String detail(@RequestHeader(value="ecmId") Long ecmId,
			@PathVariable("id") Long id);
	
	@RequestMapping(value = "/api-wx/wxArticle/createOrUpdate",method = RequestMethod.POST,consumes= {"application/json"})
	String save(@RequestHeader(value="ecmId") Long ecmId,
			@RequestBody String wxArticle);
	

	@RequestMapping(value = "/api-wx/wxArticle/{woaId}/delete",method = RequestMethod.POST)
	String delete(@RequestHeader(value="ecmId") Long ecmId,
			@PathVariable("woaId") Long woaId,
			@RequestParam(value="id") Long id);
	
}
