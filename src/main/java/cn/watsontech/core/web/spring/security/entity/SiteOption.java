package cn.watsontech.core.web.spring.security.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@ApiModel(value="cn.watsontech.core.security.entity.SiteOption")
@Table(name = "tb_site_option")
public class SiteOption {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value="name名称")
    private String name;

    /**
     * 键
     */
    @Column(name = "_key")
    @ApiModelProperty(value="key键")
    private String key;

    /**
     * 值
     */
    @ApiModelProperty(value="value值")
    private String value;

    /**
     * 参数类型
     */
    @ApiModelProperty(value="type参数类型")
    private String type;

    /**
     * 是否启用
     */
    @ApiModelProperty(value="enabled是否启用")
    private Boolean enabled;

    /**
     * 是否内置
     */
    @ApiModelProperty(value="是否内置")
    private Boolean builtin;

    /**
     * 是否可修改名称
     */
    @ApiModelProperty(value="是否可修改名称和key")
    private Boolean nameEditable;

    @Column(name = "created_by")
    @ApiModelProperty(value="createdBy")
    private String createdBy;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    @ApiModelProperty(value="createdTime创建时间")
    private Date createdTime;

    /**
     * 版本号
     */
    @ApiModelProperty(value="version版本号")
    @tk.mybatis.mapper.annotation.Version
    private Integer version;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public SiteOption setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public SiteOption setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 获取键
     *
     * @return _key - 键
     */
    public String getKey() {
        return key;
    }

    /**
     * 设置键
     *
     * @param key 键
     */
    public SiteOption setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * 获取值
     *
     * @return value - 值
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置值
     *
     * @param value 值
     */
    public SiteOption setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * 获取参数类型
     *
     * @return type - 参数类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置参数类型
     *
     * @param type 参数类型
     */
    public SiteOption setType(String type) {
        this.type = type;
        return this;
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
    public SiteOption setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Boolean getBuiltin() {
        return builtin;
    }

    public void setBuiltin(Boolean builtin) {
        this.builtin = builtin;
    }

    public Boolean getNameEditable() {
        return nameEditable;
    }

    public void setNameEditable(Boolean nameEditable) {
        this.nameEditable = nameEditable;
    }

    /**
     * @return created_by
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy
     */
    public SiteOption setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
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
    public SiteOption setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
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
    public SiteOption setVersion(Integer version) {
        this.version = version;
        return this;
    }
}