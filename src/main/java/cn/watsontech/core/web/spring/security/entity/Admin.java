package cn.watsontech.core.web.spring.security.entity;

import com.alibaba.fastjson.JSONObject;
import cn.watsontech.core.web.spring.security.LoginUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@ApiModel(value="cn.watsontech.core.security.entity.Admin")
@Table(name = "tb_admin")
public class Admin extends LoginUser {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty(value="username用户名")
    private String username;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    @ApiModelProperty(value="nickName昵称")
    private String nickName;

    /**
     * 性别，0未知，1男，2女
     */
    @ApiModelProperty(value="gender性别，0未知，1男，2女")
    private String gender;

    /**
     * 账号类型：1管理员，2运营
     */
    @ApiModelProperty(value="type账号类型：1管理员，2运营")
    private Integer type;

    /**
     * 版本号
     */
    @ApiModelProperty(value="version版本号")
    @tk.mybatis.mapper.annotation.Version
    private Integer version;

    /**
     * 创建人ID
     */
    @Column(name = "created_by")
    @ApiModelProperty(value="createdBy创建人ID")
    private Long createdBy;

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

    @Column(name = "created_time")
    @ApiModelProperty(value="createdTime")
    private Date createdTime;

    /**
     * 头像
     */
    @Column(name = "avatar_url")
    @ApiModelProperty(value="avatarUrl头像")
    private String avatarUrl;

    /**
     * 手机号码
     */
    @ApiModelProperty(value="mobile手机号码")
    private String mobile;

    /**
     * 邮箱
     */
    @ApiModelProperty(value="email邮箱")
    private String email;

    /**
     * 地址
     */
    @ApiModelProperty(value="address地址")
    private String address;

    /**
     * 是否启用web登录
     */
    @Column(name = "is_weblogin_active")
    @ApiModelProperty(value="isWebloginActive是否启用web登录")
    private Boolean isWebloginActive;

    /**
     * 加密密码
     */
    @ApiModelProperty(value="password加密密码")
    private String password;

    /**
     * 是否已过期
     */
    @ApiModelProperty(value="expired是否已过期")
    private Boolean expired;

    /**
     * 是否已锁定
     */
    @ApiModelProperty(value="locked是否已锁定")
    private Boolean locked;

    /**
     * 密码是否已过期
     */
    @Column(name = "credentials_expired")
    @ApiModelProperty(value="credentialsExpired密码是否已过期")
    private Boolean credentialsExpired;

    /**
     * 是否已启用
     */
    @ApiModelProperty(value="enabled是否已启用")
    private boolean enabled = true;

    /**
     * 最后登录ip
     */
    @Column(name = "login_ip")
    @ApiModelProperty(value="loginIp最后登录ip")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @Column(name = "login_date")
    @ApiModelProperty(value="loginDate最后登录时间")
    private Date loginDate;

    /**
     * 上次登录ip
     */
    @Column(name = "last_login_ip")
    @ApiModelProperty(value="lastLoginIp上次登录ip")
    private String lastLoginIp;

    /**
     * 上次登录日期
     */
    @Column(name = "last_login_date")
    @ApiModelProperty(value="lastLoginDate上次登录日期")
    private Date lastLoginDate;

    /**
     * 微信额外信息
     */
    @Column(name = "extra_data")
    @ApiModelProperty(value="extraData微信额外信息")
    private JSONObject extraData;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    @Override
    public Type getUserType() {
        return Type.admin;
    }

    /**
     * @param id
     */
    public Admin setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public Admin setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * 获取昵称
     *
     * @return nick_name - 昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置昵称
     *
     * @param nickName 昵称
     */
    public Admin setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    /**
     * 获取性别，0未知，1男，2女
     *
     * @return gender - 性别，0未知，1男，2女
     */
    public String getGender() {
        return gender;
    }

    /**
     * 设置性别，0未知，1男，2女
     *
     * @param gender 性别，0未知，1男，2女
     */
    public Admin setGender(String gender) {
        this.gender = gender;
        return this;
    }

    /**
     * 获取账号类型：1管理员，2运营
     *
     * @return type - 账号类型：1管理员，2运营
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置账号类型：1管理员，2运营
     *
     * @param type 账号类型：1管理员，2运营
     */
    public Admin setType(Integer type) {
        this.type = type;
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
    public Admin setVersion(Integer version) {
        this.version = version;
        return this;
    }

    /**
     * 获取创建人ID
     *
     * @return created_by - 创建人ID
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * 设置创建人ID
     *
     * @param createdBy 创建人ID
     */
    public Admin setCreatedBy(Long createdBy) {
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
    public Admin setCreatedByName(String createdByName) {
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
    public Admin setModifiedBy(Long modifiedBy) {
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
    public Admin setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
        return this;
    }

    /**
     * @return created_time
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime
     */
    public Admin setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    /**
     * 获取头像
     *
     * @return avatar_url - 头像
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * 设置头像
     *
     * @param avatarUrl 头像
     */
    public Admin setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    /**
     * 获取手机号码
     *
     * @return mobile - 手机号码
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号码
     *
     * @param mobile 手机号码
     */
    public Admin setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public Admin setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * 获取地址
     *
     * @return address - 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     *
     * @param address 地址
     */
    public Admin setAddress(String address) {
        this.address = address;
        return this;
    }

    /**
     * 获取是否启用web登录
     *
     * @return is_weblogin_active - 是否启用web登录
     */
    public Boolean getIsWebloginActive() {
        return isWebloginActive;
    }

    /**
     * 设置是否启用web登录
     *
     * @param isWebloginActive 是否启用web登录
     */
    public Admin setIsWebloginActive(Boolean isWebloginActive) {
        this.isWebloginActive = isWebloginActive;
        return this;
    }

    /**
     * 获取加密密码
     *
     * @return password - 加密密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置加密密码
     *
     * @param password 加密密码
     */
    public Admin setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * 获取是否已过期
     *
     * @return expired - 是否已过期
     */
    public Boolean getExpired() {
        return expired;
    }

    /**
     * 设置是否已过期
     *
     * @param expired 是否已过期
     */
    public Admin setExpired(Boolean expired) {
        this.expired = expired;
        return this;
    }

    /**
     * 获取是否已锁定
     *
     * @return locked - 是否已锁定
     */
    public Boolean getLocked() {
        return locked;
    }

    /**
     * 设置是否已锁定
     *
     * @param locked 是否已锁定
     */
    public Admin setLocked(Boolean locked) {
        this.locked = locked;
        return this;
    }

    /**
     * 获取密码是否已过期
     *
     * @return credentials_expired - 密码是否已过期
     */
    public Boolean getCredentialsExpired() {
        return credentialsExpired;
    }

    /**
     * 设置密码是否已过期
     *
     * @param credentialsExpired 密码是否已过期
     */
    public Admin setCredentialsExpired(Boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
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
    public Admin setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * 获取最后登录ip
     *
     * @return login_ip - 最后登录ip
     */
    public String getLoginIp() {
        return loginIp;
    }

    /**
     * 设置最后登录ip
     *
     * @param loginIp 最后登录ip
     */
    public Admin setLoginIp(String loginIp) {
        this.loginIp = loginIp;
        return this;
    }

    /**
     * 获取最后登录时间
     *
     * @return login_date - 最后登录时间
     */
    public Date getLoginDate() {
        return loginDate;
    }

    /**
     * 设置最后登录时间
     *
     * @param loginDate 最后登录时间
     */
    public Admin setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
        return this;
    }

    /**
     * 获取上次登录ip
     *
     * @return last_login_ip - 上次登录ip
     */
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    /**
     * 设置上次登录ip
     *
     * @param lastLoginIp 上次登录ip
     */
    public Admin setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
        return this;
    }

    /**
     * 获取上次登录日期
     *
     * @return last_login_date - 上次登录日期
     */
    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    /**
     * 设置上次登录日期
     *
     * @param lastLoginDate 上次登录日期
     */
    public Admin setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
        return this;
    }

    /**
     * 获取微信额外信息
     *
     * @return extra_data - 微信额外信息
     */
    public JSONObject getExtraData() {
        return extraData;
    }

    /**
     * 设置微信额外信息
     *
     * @param extraData 微信额外信息
     */
    public Admin setExtraData(JSONObject extraData) {
        this.extraData = extraData;
        return this;
    }

    /**
     * 获取类型名称
     * @return
     */
    @ApiModelProperty(value = "管理员类型名称（超级管理员/运营）")
    public String getTypeName() {
        if(getType()!=null) {
            switch (getType()) {
                case 1: return "admin";
                default: return "operator";
            }
        }

        return null;
    }
}