package com.emin.platform.wxbase.interfaces.basic;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "zuul")
public interface KeyReplyApiFeign {
	
	@RequestMapping(value = "/api-wx/keyReply/{woaId}/page",method = RequestMethod.GET)
	String page(@RequestHeader(value="ecmId") Long ecmId,
			@PathVariable("woaId") Long woaId,
			@RequestParam(value="page") Integer page,
			@RequestParam(value="limit") Integer limit,
			@RequestParam(value="keyword") String keyword);
	

	@RequestMapping(value = "/api-wx/keyReply/{id}/info",method = RequestMethod.GET)
	String detail(@RequestHeader(value="ecmId") Long ecmId,
			@PathVariable("id") Long id);
	
	@RequestMapping(value = "/api-wx/keyReply/createOrUpdate",method = RequestMethod.POST,consumes= {"application/json"})
	String save(@RequestHeader(value="ecmId") Long ecmId,
			@RequestBody String keyReply,
			@RequestParam(value="articleIds") String articleIds);
	

	@RequestMapping(value = "/api-wx/keyReply/delete",method = RequestMethod.POST)
	String delete(@RequestHeader(value="ecmId") Long ecmId,
			@RequestParam(value="id") Long id);
	
}
