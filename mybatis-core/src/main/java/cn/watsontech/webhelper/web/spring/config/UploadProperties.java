package cn.watsontech.webhelper.web.spring.config;

import cn.watsontech.webhelper.vendor.UploadService;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Copyright to watsontech
 * 基本业务配置注解类
 */
@Data
@Log4j2
@ConfigurationProperties(prefix = "file")
public class UploadProperties {
	private UploadService.UploadVendor vendor;

	private String accessKey;
	private String accessSecret;

	private String bucket;
	private String regionName;
	private String webRootUrl;
	private String imageRootUrl;

	private String localPath;

	private String adminBucket;
	private String adminLocalPath;

	//本地文件虚拟目录 /static/images/**
	private String staticAccessPath;
}