package cn.watsontech.webhelper;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Copyright to watsontech
 * 基本业务配置注解类
 */
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

	public UploadService.UploadVendor getVendor() {
		return vendor;
	}

	public void setVendor(UploadService.UploadVendor vendor) {
		this.vendor = vendor;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getAccessSecret() {
		return accessSecret;
	}

	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getWebRootUrl() {
		return webRootUrl;
	}

	public void setWebRootUrl(String webRootUrl) {
		this.webRootUrl = webRootUrl;
	}

	public String getImageRootUrl() {
		return imageRootUrl;
	}

	public void setImageRootUrl(String imageRootUrl) {
		this.imageRootUrl = imageRootUrl;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getAdminBucket() {
		return adminBucket;
	}

	public void setAdminBucket(String adminBucket) {
		this.adminBucket = adminBucket;
	}

	public String getAdminLocalPath() {
		return adminLocalPath;
	}

	public void setAdminLocalPath(String adminLocalPath) {
		this.adminLocalPath = adminLocalPath;
	}

	public String getStaticAccessPath() {
		return staticAccessPath;
	}

	public void setStaticAccessPath(String staticAccessPath) {
		this.staticAccessPath = staticAccessPath;
	}
}