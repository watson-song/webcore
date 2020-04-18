package cn.watsontech.core.web.spring.security.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.persistence.*;

@ApiModel(value="cn.watsontech.core.security.entity.RefreshToken")
@Table(name = "tb_refreshtoken")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 刷新令牌
     */
    @Column(name = "refresh_token")
    @ApiModelProperty(value="refreshToken刷新令牌")
    private String refreshToken;

    /**
     * 过期时间
     */
    @Column(name = "expire_time")
    @ApiModelProperty(value="expireTime过期时间")
    private Date expireTime;

    /**
     * 用户类型
     */
    @Column(name = "user_type")
    @ApiModelProperty(value="userType用户类型")
    private String userType;

    /**
     * 0禁用，1可以
     */
    @ApiModelProperty(value="enabled0禁用，1可以")
    private Boolean enabled;

    /**
     * 版本
     */
    @ApiModelProperty(value="version版本")
    private Integer version;

    /**
     * 创建人
     */
    @Column(name = "created_by")
    @ApiModelProperty(value="createdBy创建人")
    private Long createdBy;

    /**
     * 创建人名称
     */
    @Column(name = "created_by_name")
    @ApiModelProperty(value="createdByName创建人名称")
    private String createdByName;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    @ApiModelProperty(value="createdTime创建时间")
    private Date createdTime;

    /**
     * 更新人
     */
    @Column(name = "modified_by")
    @ApiModelProperty(value="modifiedBy更新人")
    private Long modifiedBy;

    /**
     * 更新时间
     */
    @Column(name = "modified_time")
    @ApiModelProperty(value="modifiedTime更新时间")
    private Date modifiedTime;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public RefreshToken setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * 获取刷新令牌
     *
     * @return refresh_token - 刷新令牌
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * 设置刷新令牌
     *
     * @param refreshToken 刷新令牌
     */
    public RefreshToken setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
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
    public RefreshToken setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    /**
     * 获取用户类型
     *
     * @return user_type - 用户类型
     */
    public String getUserType() {
        return userType;
    }

    /**
     * 设置用户类型
     *
     * @param userType 用户类型
     */
    public RefreshToken setUserType(String userType) {
        this.userType = userType;
        return this;
    }

    /**
     * 获取0禁用，1可以
     *
     * @return enabled - 0禁用，1可以
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * 设置0禁用，1可以
     *
     * @param enabled 0禁用，1可以
     */
    public RefreshToken setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * 获取版本
     *
     * @return version - 版本
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * 设置版本
     *
     * @param version 版本
     */
    public RefreshToken setVersion(Integer version) {
        this.version = version;
        return this;
    }

    /**
     * 获取创建人
     *
     * @return created_by - 创建人
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * 设置创建人
     *
     * @param createdBy 创建人
     */
    public RefreshToken setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
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
    public RefreshToken setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
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
    public RefreshToken setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    /**
     * 获取更新人
     *
     * @return modified_by - 更新人
     */
    public Long getModifiedBy() {
        return modifiedBy;
    }

    /**
     * 设置更新人
     *
     * @param modifiedBy 更新人
     */
    public RefreshToken setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    /**
     * 获取更新时间
     *
     * @return modified_time - 更新时间
     */
    public Date getModifiedTime() {
        return modifiedTime;
    }

    /**
     * 设置更新时间
     *
     * @param modifiedTime 更新时间
     */
    public RefreshToken setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
        return this;
    }
}