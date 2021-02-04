package cn.watsontech.webhelper.vo;

import cn.watsontech.webhelper.UploadService;

public class UploadFile {
    private String name;
    private String path;
    private UploadService.UploadVendor vendor;
    private long size;
    private String bucket;
    private String type;
    private String tags;
    private String url;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    public UploadService.UploadVendor getVendor() {
        return vendor;
    }

    public void setVendor(UploadService.UploadVendor vendor) {
        this.vendor = vendor;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getBucket() {
        return bucket;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTags() {
        return tags;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
