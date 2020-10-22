package cn.watsontech.core.web.spring.security.entity;

import cn.watsontech.core.mybatis.CreatedEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@ApiModel(value="cn.watsontech.core.security.entity.Role")
@Table(name = "tb_role")
public class Role implements CreatedEntity<Role, Long, Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="id")
    private Long id;

    @ApiModelProperty(value="label")
    private String label;

    @ApiModelProperty(value="name")
    private String name;

    @ApiModelProperty(value="tag")
    private String tag;

    /**
     * 0禁用，1可以
     */
    @ApiModelProperty(value="status0禁用，1可以")
    private Integer status;

    /**
     * 角色类型，对应admin的type
     */
    @ApiModelProperty(value="账号类型：1管理员，2运营，支持更多自定义")
    private Integer type;

    /**
     * 内置类型：0系统自带，1用户创建的
     */
    @ApiModelProperty(value="0系统自带，1用户创建的")
    private Boolean builtinType;

    @Column(name = "created_by")
    @ApiModelProperty(value="createdBy")
    private Long createdBy;

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
     * 创建人名称
     */
    @Column(name = "created_by_name")
    @ApiModelProperty(value="createdByName创建人名称")
    private String createdByName;

    /**
     * 最后更新人ID
     */
    @Column(name = "modified_by")
    @ApiModelProperty(value="modifiedBy最后更新人ID")
    private Long modifiedBy;

    /**
     * 最后更新时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(value="modifiedTime最后更新时间")
    private Date modifiedTime;

    /**
     * 是否已启用
     */
    @ApiModelProperty(value="enabled是否已启用")
    private boolean enabled = true;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public Role setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * @return label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label
     */
    public Role setLabel(String label) {
        this.label = label;
        return this;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public Role setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag
     */
    public Role setTag(String tag) {
        this.tag = tag;
        return this;
    }

    /**
     * 获取0禁用，1可以
     *
     * @return status - 0禁用，1可以
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0禁用，1可以
     *
     * @param status 0禁用，1可以
     */
    public Role setStatus(Integer status) {
        this.status = status;
        return this;
    }

    /**
     * 获取0系统自带，1用户创建的
     *
     * @return type - 账号类型：1管理员，2运营，3自定义
     */
    public Integer getType() {
        return type;
    }

    /**
     * 账号类型：1管理员，2运营，3自定义
     *
     * @param type 账号类型：1管理员，2运营，3自定义
     */
    public Role setType(Integer type) {
        this.type = type;
        return this;
    }

    /**
     * 获取0系统自带，1用户创建的
     *
     * @return type - 0系统自带，1用户创建的
     */
    public Boolean getBuiltinType() {
        return builtinType;
    }

    public void setBuiltinType(Boolean builtinType) {
        this.builtinType = builtinType;
    }

    /**
     * @return created_by
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy
     */
    public Role setCreatedBy(Long createdBy) {
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
    public Role setCreatedTime(Date createdTime) {
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
    public Role setVersion(Integer version) {
        this.version = version;
        return this;
    }

    /**
     * 获取创建人名称
     *
     * @return created_by_name - 创建人名称
     */
    public String getCreatedByName() {
        return createdByName;
    }

    /**
     * 设置创建人名称
     *
     * @param createdByName 创建人名称
     */
    public Role setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
        return this;
    }

    /**
     * 获取最后更新人ID
     *
     * @return modified_by - 最后更新人ID
     */
    public Long getModifiedBy() {
        return modifiedBy;
    }

    /**
     * 设置最后更新人ID
     *
     * @param modifiedBy 最后更新人ID
     */
    public Role setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    /**
     * 获取最后更新时间
     *
     * @return modified_time - 最后更新时间
     */
    public Date getModifiedTime() {
        return modifiedTime;
    }

    /**
     * 设置最后更新时间
     *
     * @param modifiedTime 最后更新时间
     */
    public Role setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
        return this;
    }

    /**
     * 获取是否已启用
     *
     * @return enabled - 是否已启用
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 设置是否已启用
     *
     * @param enabled 是否已启用
     */
    public Role setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}