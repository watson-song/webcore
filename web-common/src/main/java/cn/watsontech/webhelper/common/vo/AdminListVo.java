package cn.watsontech.webhelper.common.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Watson on 2020/7/28.
 */
public class AdminListVo {
    private Long id;

    private String no;

    /**
     * 用户名
     */
    @ApiModelProperty(value="username用户名")
    private String username;

    /**
     * 昵称
     */
    @ApiModelProperty(value="nickName昵称")
    private String nickName;

    /**
     * 部门
     */
    @ApiModelProperty(value="部门")
    private String department;

    /**
     * 职位
     */
    @ApiModelProperty(value="职位")
    private String title;

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
     * 创建人ID
     */
    @ApiModelProperty(value="createdBy创建人ID")
    private Long createdBy;

    /**
     * 创建人名称
     */
    @ApiModelProperty(value="createdByName创建人名称")
    private String createdByName;

    /**
     * 最后更新人ID
     */
    @ApiModelProperty(value="modifiedBy最后更新人ID")
    private Long modifiedBy;

    /**
     * 最后更新时间
     */
    @ApiModelProperty(value="modifiedTime最后更新时间")
    private Date modifiedTime;

    @ApiModelProperty(value="createdTime")
    private Date createdTime;

    /**
     * 头像
     */
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
    @ApiModelProperty(value="isWebloginActive是否启用web登录")
    private Boolean isWebloginActive;

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
    @ApiModelProperty(value="loginIp最后登录ip")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @ApiModelProperty(value="loginDate最后登录时间")
    private Date loginDate;

    /**
     * 上次登录ip
     */
    @ApiModelProperty(value="lastLoginIp上次登录ip")
    private String lastLoginIp;

    /**
     * 上次登录日期
     */
    @ApiModelProperty(value="lastLoginDate上次登录日期")
    private Date lastLoginDate;

    /**
     * 微信额外信息
     */
    @ApiModelProperty(value="extraData微信额外信息")
    private JSONObject extraData;

    @ApiModelProperty(value="用户角色")
    List<Map<String, Object>> roles;

    @ApiModelProperty(value="用户权限")
    List<Map<String, Object>> permissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getWebloginActive() {
        return isWebloginActive;
    }

    public void setWebloginActive(Boolean webloginActive) {
        isWebloginActive = webloginActive;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(Boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public JSONObject getExtraData() {
        return extraData;
    }

    public void setExtraData(JSONObject extraData) {
        this.extraData = extraData;
    }

    public List<Map<String, Object>> getRoles() {
        return roles;
    }

    public void setRoles(List<Map<String, Object>> roles) {
        this.roles = roles;
    }

    public List<Map<String, Object>> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Map<String, Object>> permissions) {
        this.permissions = permissions;
    }
}
