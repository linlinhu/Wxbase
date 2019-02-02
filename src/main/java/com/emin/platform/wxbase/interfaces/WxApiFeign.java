package com.emin.platform.wxbase.interfaces;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-zuul")
public interface WxApiFeign {
	
	/**
	 * 获得js-sdk的配置信息
	 * @param companyCode 公众号code
	 * @param encodedURLWithParams 其它参数
	 * @return
	 */
	@RequestMapping(value = "/api-wx/wxtool/{companyCode}/configByCode",method = RequestMethod.POST)
	String wxConfig(@PathVariable("companyCode") String companyCode, @RequestParam(value="encodedURLWithParams") String encodedURLWithParams);
	
	/**
	 * 获得js-sdk的配置信息
	 * @param woaId 公众号id
	 * @param encodedURLWithParams 其它参数
	 * @return
	 */
	@RequestMapping(value = "/api-wx/wxtool/{woaId}/config",method = RequestMethod.POST)
	String wxConfig(@PathVariable(value="woaId") Long woaId, @RequestParam(value="encodedURLWithParams") String encodedURLWithParams);
	
	/**
	 * 获取用户基本信息
	 * @param woaId 公众号id
	 * @param code 
	 * @return
	 */
	@RequestMapping(value = "/api-wx/wxtool/{woaId}/code_user/{code}",method = RequestMethod.GET)
	String getCodeUser(@PathVariable(value="woaId") Long woaId, @PathVariable(value="code") String code);
	
	/**
	 * 将普通URL转换成微信oauthURL
	 * @param woaId 公众号ID
	 * @param url 普通url
	 * @return
	 */
	@RequestMapping(value = "/api-wx/wxtool/{woaId}/oauthUrl",method = RequestMethod.GET)
	String getOauthURL(@PathVariable(value="woaId") Long woaId, @RequestParam(value="url") String url);
	
	/**
	 * 将普通URL转换成微信SNSbaseUrl
	 * @param woaId 公众号ID
	 * @param url 普通url
	 * @return
	 */
	@RequestMapping(value = "/api-wx/wxtool/{woaId}/snsBaseUrl",method = RequestMethod.GET)
	String getSnsBaseURL(@PathVariable(value="woaId") Long woaId, @RequestParam(value="url") String url);
	
	/**
	 * 保存微信公众号
	 * @param wxOfficialAccount 微信公众实体JSON字符串
	 * @return
	 */
	@RequestMapping(value = "/api-wx/woa/createOrUpdate",method = RequestMethod.POST)
	String saveWxoa(@RequestBody String wxOfficialAccount);
	/**
	 * 停用微信公众号
	 * @param id 微信公众号id
	 * @return
	 */
	@RequestMapping(value = "/api-wx/woa/disable",method = RequestMethod.POST)
	String disableWxoa(@RequestParam(value="id") Long id);
	
	/**
	 * 加载微信公众号列表
	 * @param companyId 公司id
	 * @return
	 */
	@RequestMapping(value = "/api-wx/woa/{companyId}/woalist",method = RequestMethod.GET)
	String loadWxoas(@PathVariable(value="companyId") Long companyId);
	
	/**
	 * 获得微信公众号详情
	 * @param id 微信公众号id
	 * @return
	 */
	@RequestMapping(value = "/api-wx/woa/{id}/info",method = RequestMethod.GET)
	String getWxoa(@PathVariable(value="id") Long id);
	
	/**
	 * 保存自定义菜单
	 * @param menu 自定义菜单实体JSON字符串
	 * @return
	 */
	@RequestMapping(value = "/api-wx/wxmenu/createOrUpdate",method = RequestMethod.POST)
	String saveWxmenu(@RequestParam(value="menu") String menu);
	
	/**
	 * 删除微信公众号自定义菜单
	 * @param id 自定义菜单id
	 * @return
	 */
	@RequestMapping(value = "/api-wx/wxmenu/remove",method = RequestMethod.POST)
	String delWxmenu(@RequestParam(value="id") Long id);
	
	/**
	 * 生成公众号自定义菜单
	 * @param woaId 公众号id
	 * @return
	 */
	@RequestMapping(value = "/api-wx/wxmenu/{woaId}/generate",method = RequestMethod.POST)
	String generateWxmenu(@PathVariable(value="woaId") Long woaId);
	
	
	
}
