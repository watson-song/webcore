package cn.watsontech.webhelper.vo;

import java.util.Date;

/**
 * Created by Watson on 2020/11/18.
 */
public class FileInfoVo {

    private Long id;

    /**
     * 应用code
     */
    private String appinfoId;

    /**
     * 拥有人
     */
    private Long userId;

    private String userType;

    /**
     * 云端id,aliyun,tencent,ucloud
     */
    private String cProvider;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 访问地址
     */
    private String url;

    /**
     * 云端路径
     */
    private String path;

    /**
     * 云端bucket
     */
    private String bucket;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 引用对象id
     */
    private String refObjectId;

    /**
     * 引用对象类型
     */
    private String refObjectType;

    /**
     * 标签
     */
    private String tags;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 创建人名称
     */
    private Long createdBy;

    private String createdByName;

    /**
     * 创建时间
     */
    private Date createdTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppinfoId() {
        return appinfoId;
    }

    public void setAppinfoId(String appinfoId) {
        this.appinfoId = appinfoId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getcProvider() {
        return cProvider;
    }

    public void setcProvider(String cProvider) {
        this.cProvider = cProvider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getRefObjectId() {
        return refObjectId;
    }

    public void setRefObjectId(String refObjectId) {
        this.refObjectId = refObjectId;
    }

    public String getRefObjectType() {
        return refObjectType;
    }

    public void setRefObjectType(String refObjectType) {
        this.refObjectType = refObjectType;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
