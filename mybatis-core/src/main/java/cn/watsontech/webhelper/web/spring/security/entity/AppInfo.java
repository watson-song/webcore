package cn.watsontech.webhelper.web.spring.security.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tb_appinfo")
public class AppInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 应用id
     */
    private String code;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 拥有人
     */
    private String secret;

    /**
     * 描述
     */
    private String description;

    /**
     * 允许最大delay时长，单位为s
     */
    @Column(name = "allow_delay")
    private Integer allowDelay;

    /**
     * 白名单ip，逗号隔开
     */
    @Column(name = "allow_ips")
    private String allowIps;

    /**
     * 运行访问的urls
     */
    @Column(name = "allow_urls")
    private String allowUrls;

    /**
     * 过期时间
     */
    @Column(name = "expire_time")
    private Date expireTime;

    /**
     * 联系人
     */
    @Column(name = "contact_name")
    private String contactName;

    /**
     * 联系方式
     */
    @Column(name = "contact_telephone")
    private String contactTelephone;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建人名称
     */
    @Column(name = "created_by")
    private Long createdBy;

    /**
     * 创建人
     */
    @Column(name = "created_by_name")
    private String createdByName;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 是否已经删除，1已删除
     */
    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_by_name")
    private String modifiedByName;

    /**
     * 删除时间
     */
    @Column(name = "modified_time")
    private Date modifiedTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取应用id
     *
     * @return code - 应用id
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置应用id
     *
     * @param code 应用id
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取文件名称
     *
     * @return name - 文件名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置文件名称
     *
     * @param name 文件名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取拥有人
     *
     * @return secret - 拥有人
     */
    public String getSecret() {
        return secret;
    }

    /**
     * 设置拥有人
     *
     * @param secret 拥有人
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取允许最大delay时长，单位为s
     *
     * @return allow_delay - 允许最大delay时长，单位为s
     */
    public Integer getAllowDelay() {
        return allowDelay;
    }

    /**
     * 设置允许最大delay时长，单位为s
     *
     * @param allowDelay 允许最大delay时长，单位为s
     */
    public void setAllowDelay(Integer allowDelay) {
        this.allowDelay = allowDelay;
    }

    /**
     * 获取白名单ip，逗号隔开
     *
     * @return allow_ips - 白名单ip，逗号隔开
     */
    public String getAllowIps() {
        return allowIps;
    }

    /**
     * 设置白名单ip，逗号隔开
     *
     * @param allowIps 白名单ip，逗号隔开
     */
    public void setAllowIps(String allowIps) {
        this.allowIps = allowIps;
    }

    /**
     * 获取运行访问的urls
     *
     * @return allow_urls - 运行访问的urls
     */
    public String getAllowUrls() {
        return allowUrls;
    }

    /**
     * 设置运行访问的urls
     *
     * @param allowUrls 运行访问的urls
     */
    public void setAllowUrls(String allowUrls) {
        this.allowUrls = allowUrls;
    }

    /**
     * 获取过期时间
     *
     * @return expire_time - 过期时间
     */
    public Date getExpireTime() {
        return expireTime;
    }

    /**
     * 设置过期时间
     *
     * @param expireTime 过期时间
     */
    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * 获取联系人
     *
     * @return contact_name - 联系人
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * 设置联系人
     *
     * @param contactName 联系人
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * 获取联系方式
     *
     * @return contact_telephone - 联系方式
     */
    public String getContactTelephone() {
        return contactTelephone;
    }

    /**
     * 设置联系方式
     *
     * @param contactTelephone 联系方式
     */
    public void setContactTelephone(String contactTelephone) {
        this.contactTelephone = contactTelephone;
    }

    /**
     * 获取是否启用
     *
     * @return enabled - 是否启用
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * 设置是否启用
     *
     * @param enabled 是否启用
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 获取版本号
     *
     * @return version - 版本号
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * 设置版本号
     *
     * @param version 版本号
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * 获取创建人名称
     *
     * @return created_by - 创建人名称
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * 设置创建人名称
     *
     * @param createdBy 创建人名称
     */
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * 获取创建人
     *
     * @return created_by_name - 创建人
     */
    public String getCreatedByName() {
        return createdByName;
    }

    /**
     * 设置创建人
     *
     * @param createdByName 创建人
     */
    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    /**
     * 获取创建时间
     *
     * @return created_time - 创建时间
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * 设置创建时间
     *
     * @param createdTime 创建时间
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 获取是否已经删除，1已删除
     *
     * @return modified_by - 是否已经删除，1已删除
     */
    public Long getModifiedBy() {
        return modifiedBy;
    }

    /**
     * 设置是否已经删除，1已删除
     *
     * @param modifiedBy 是否已经删除，1已删除
     */
    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * @return modified_by_name
     */
    public String getModifiedByName() {
        return modifiedByName;
    }

    /**
     * @param modifiedByName
     */
    public void setModifiedByName(String modifiedByName) {
        this.modifiedByName = modifiedByName;
    }

    /**
     * 获取删除时间
     *
     * @return modified_time - 删除时间
     */
    public Date getModifiedTime() {
        return modifiedTime;
    }

    /**
     * 设置删除时间
     *
     * @param modifiedTime 删除时间
     */
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}