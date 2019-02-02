package com.emin.platform.wxbase.interfaces;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.emin.platform.wxbase.config.FeignMultipartSupportConfig;


/***
 * 主体接口桥梁定义
 * @author kakadanica
 */
@FeignClient(value = "zuul",configuration = FeignMultipartSupportConfig.class)
public interface FileApiFeign {
	
	/**
	 * 分页查询主体信息
	 * @param pageRequest 分页基本字段
	 * @param ecmIndustories 行业编号
	 * @param keyword 关键字
	 * @return
	 */
	@RequestMapping(value = "/api-storage/storage/upload/uploadCompressImg",method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	String upload(@RequestPart(value="file") MultipartFile file);
	
	
	
}
