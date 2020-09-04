package cn.watsontech.core.web.spring.security.entity;

import cn.watsontech.core.mybatis.CreatedEntity;
import cn.watsontech.core.web.spring.security.LoginUser;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@ApiModel(value="cn.watsontech.core.security.entity.User")
@Table(name = "tb_user")
public class User extends LoginUser implements CreatedEntity<User, Long, Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 微信openid
     */
    @ApiModelProperty(value="openid微信openid")
    private String openid;

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
     * 语言
     */
    @ApiModelProperty(value="language语言")
    private String language;

    /**
     * 城市
     */
    @ApiModelProperty(value="city城市")
    private String city;

    /**
     * 省
     */
    @ApiModelProperty(value="province省")
    private String province;

    /**
     * 国家
     */
    @ApiModelProperty(value="country国家")
    private String country;

    /**
     * 头像
     */
    @Column(name = "avatar_url")
    @ApiModelProperty(value="avatarUrl头像")
    private String avatarUrl;

    /**
     * sessionkey
     */
    @Column(name = "session_key")
    @ApiModelProperty(value="sessionKeysessionkey")
    private String sessionKey;

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
     * appid
     */
    @ApiModelProperty(value="appidappid")
    private String appid;

    /**
     * unionid
     */
    @ApiModelProperty(value="unionidunionid")
    private String unionid;

    /**
     * 是否订阅了公众号
     */
    @Column(name = "is_subscribe")
    @ApiModelProperty(value="isSubscribe是否订阅了公众号")
    private Boolean isSubscribe;

    /**
     * 公众号订阅时间
     */
    @Column(name = "subscribe_time")
    @ApiModelProperty(value="subscribeTime公众号订阅时间")
    private Long subscribeTime;

    /**
     * 公众号订阅场景值
     */
    @Column(name = "subscribe_scene")
    @ApiModelProperty(value="subscribeScene公众号订阅场景值")
    private String subscribeScene;

    /**
     * 微信备注
     */
    @Column(name = "wx_remark")
    @ApiModelProperty(value="wxRemark微信备注")
    private String wxRemark;

    /**
     * 微信tagids
     */
    @Column(name = "wx_tag_ids")
    @ApiModelProperty(value="wxTagIds微信tagids")
    private String wxTagIds;

    /**
     * 是否已授权获取头像
     */
    @ApiModelProperty(value="logged是否已授权获取头像")
    private Boolean logged;

    /**
     * 是否启用web登录
     */
    @Column(name = "is_weblogin_active")
    @ApiModelProperty(value="isWebloginActive是否启用web登录")
    private Boolean isWebloginActive;

    /**
     * 是否为vip用户
     */
    @Column(name = "is_vip")
    @ApiModelProperty(value="是否为vip用户")
    private Boolean isVip;

    /**
     * 经度
     */
    @ApiModelProperty(value="lat经度")
    private Double lat;

    /**
     * 纬度
     */
    @ApiModelProperty(value="lng纬度")
    private Double lng;

    /**
     * 系统标签
     */
    @Column(name = "os_tags")
    @ApiModelProperty(value="osTags系统标签")
    private String osTags;

    /**
     * 设备标签
     */
    @Column(name = "device_tags")
    @ApiModelProperty(value="deviceTags设备标签")
    private String deviceTags;

    /**
     * 管理员设置标签
     */
    @Column(name = "admin_tags")
    @ApiModelProperty(value="adminTags管理员设置标签")
    private String adminTags;

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
    private Boolean enabled;

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
        return Type.user;
    }

    /**
     * @param id
     */
    public User setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * 获取微信openid
     *
     * @return openid - 微信openid
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * 设置微信openid
     *
     * @param openid 微信openid
     */
    public User setOpenid(String openid) {
        this.openid = openid;
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
    public User setUsername(String username) {
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
    public User setNickName(String nickName) {
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
    public User setGender(String gender) {
        this.gender = gender;
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
    public User setVersion(Integer version) {
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
    public User setCreatedBy(Long createdBy) {
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
    public User setCreatedByName(String createdByName) {
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
    public User setModifiedBy(Long modifiedBy) {
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
    public User setModifiedTime(Date modifiedTime) {
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
    public User setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    /**
     * 获取语言
     *
     * @return language - 语言
     */
    public String getLanguage() {
        return language;
    }

    /**
     * 设置语言
     *
     * @param language 语言
     */
    public User setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * 获取城市
     *
     * @return city - 城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置城市
     *
     * @param city 城市
     */
    public User setCity(String city) {
        this.city = city;
        return this;
    }

    /**
     * 获取省
     *
     * @return province - 省
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省
     *
     * @param province 省
     */
    public User setProvince(String province) {
        this.province = province;
        return this;
    }

    /**
     * 获取国家
     *
     * @return country - 国家
     */
    public String getCountry() {
        return country;
    }

    /**
     * 设置国家
     *
     * @param country 国家
     */
    public User setCountry(String country) {
        this.country = country;
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
    public User setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    /**
     * 获取sessionkey
     *
     * @return session_key - sessionkey
     */
    public String getSessionKey() {
        return sessionKey;
    }

    /**
     * 设置sessionkey
     *
     * @param sessionKey sessionkey
     */
    public User setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
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
    public User setMobile(String mobile) {
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
    public User setEmail(String email) {
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
    public User setAddress(String address) {
        this.address = address;
        return this;
    }

    /**
     * 获取appid
     *
     * @return appid - appid
     */
    public String getAppid() {
        return appid;
    }

    /**
     * 设置appid
     *
     * @param appid appid
     */
    public User setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    /**
     * 获取unionid
     *
     * @return unionid - unionid
     */
    public String getUnionid() {
        return unionid;
    }

    /**
     * 设置unionid
     *
     * @param unionid unionid
     */
    public User setUnionid(String unionid) {
        this.unionid = unionid;
        return this;
    }

    /**
     * 获取是否订阅了公众号
     *
     * @return is_subscribe - 是否订阅了公众号
     */
    public Boolean getIsSubscribe() {
        return isSubscribe;
    }

    /**
     * 设置是否订阅了公众号
     *
     * @param isSubscribe 是否订阅了公众号
     */
    public User setIsSubscribe(Boolean isSubscribe) {
        this.isSubscribe = isSubscribe;
        return this;
    }

    /**
     * 获取公众号订阅时间
     *
     * @return subscribe_time - 公众号订阅时间
     */
    public Long getSubscribeTime() {
        return subscribeTime;
    }

    /**
     * 设置公众号订阅时间
     *
     * @param subscribeTime 公众号订阅时间
     */
    public User setSubscribeTime(Long subscribeTime) {
        this.subscribeTime = subscribeTime;
        return this;
    }

    /**
     * 获取公众号订阅场景值
     *
     * @return subscribe_scene - 公众号订阅场景值
     */
    public String getSubscribeScene() {
        return subscribeScene;
    }

    /**
     * 设置公众号订阅场景值
     *
     * @param subscribeScene 公众号订阅场景值
     */
    public User setSubscribeScene(String subscribeScene) {
        this.subscribeScene = subscribeScene;
        return this;
    }

    /**
     * 获取微信备注
     *
     * @return wx_remark - 微信备注
     */
    public String getWxRemark() {
        return wxRemark;
    }

    /**
     * 设置微信备注
     *
     * @param wxRemark 微信备注
     */
    public User setWxRemark(String wxRemark) {
        this.wxRemark = wxRemark;
        return this;
    }

    /**
     * 获取微信tagids
     *
     * @return wx_tag_ids - 微信tagids
     */
    public String getWxTagIds() {
        return wxTagIds;
    }

    /**
     * 设置微信tagids
     *
     * @param wxTagIds 微信tagids
     */
    public User setWxTagIds(String wxTagIds) {
        this.wxTagIds = wxTagIds;
        return this;
    }

    /**
     * 获取是否已授权获取头像
     *
     * @return logged - 是否已授权获取头像
     */
    public Boolean getLogged() {
        return logged;
    }

    /**
     * 设置是否已授权获取头像
     *
     * @param logged 是否已授权获取头像
     */
    public User setLogged(Boolean logged) {
        this.logged = logged;
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
    public User setIsWebloginActive(Boolean isWebloginActive) {
        this.isWebloginActive = isWebloginActive;
        return this;
    }

    /**
     * 获取经度
     *
     * @return lat - 经度
     */
    public Double getLat() {
        return lat;
    }

    /**
     * 设置经度
     *
     * @param lat 经度
     */
    public User setLat(Double lat) {
        this.lat = lat;
        return this;
    }

    /**
     * 获取纬度
     *
     * @return lng - 纬度
     */
    public Double getLng() {
        return lng;
    }

    /**
     * 设置纬度
     *
     * @param lng 纬度
     */
    public User setLng(Double lng) {
        this.lng = lng;
        return this;
    }

    /**
     * 获取系统标签
     *
     * @return os_tags - 系统标签
     */
    public String getOsTags() {
        return osTags;
    }

    /**
     * 设置系统标签
     *
     * @param osTags 系统标签
     */
    public User setOsTags(String osTags) {
        this.osTags = osTags;
        return this;
    }

    /**
     * 获取设备标签
     *
     * @return device_tags - 设备标签
     */
    public String getDeviceTags() {
        return deviceTags;
    }

    /**
     * 设置设备标签
     *
     * @param deviceTags 设备标签
     */
    public User setDeviceTags(String deviceTags) {
        this.deviceTags = deviceTags;
        return this;
    }

    /**
     * 获取管理员设置标签
     *
     * @return admin_tags - 管理员设置标签
     */
    public String getAdminTags() {
        return adminTags;
    }

    /**
     * 设置管理员设置标签
     *
     * @param adminTags 管理员设置标签
     */
    public User setAdminTags(String adminTags) {
        this.adminTags = adminTags;
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
    public User setPassword(String password) {
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
    public User setExpired(Boolean expired) {
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
    public User setLocked(Boolean locked) {
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
    public User setCredentialsExpired(Boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
        return this;
    }

    /**
     * 获取是否已启用
     *
     * @return enabled - 是否已启用
     */
    public Boolean getActivated() {
        return enabled;
    }

    /**
     * 设置是否已启用
     *
     * @param enabled 是否已启用
     */
    public User setEnabled(Boolean enabled) {
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
    public User setLoginIp(String loginIp) {
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
    public User setLoginDate(Date loginDate) {
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
    public User setLastLoginIp(String lastLoginIp) {
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
    public User setLastLoginDate(Date lastLoginDate) {
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
    public User setExtraData(JSONObject extraData) {
        this.extraData = extraData;
        return this;
    }

    public Boolean getVip() {
        return isVip;
    }

    public User setVip(Boolean vip) {
        isVip = vip;
        return this;
    }
}