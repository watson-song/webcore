package cn.watsontech.webhelper.common.form;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

public class AdminRegisterForm {

    @ApiModelProperty(notes = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(notes = "部门")
    private String department;

    @ApiModelProperty(notes = "职位")
    private String title;

    /**
     * 头像
     */
    @ApiModelProperty(value="avatarUrl头像")
    private String avatarUrl;

    /**
     * 注册手机号码
     */
    @ApiModelProperty(notes = "电话号码")
    @NotBlank(message = "电话号码不能为空")
    private String mobile;

    @ApiModelProperty(notes = "管理员类型：管理员(1)/运营(2)/财务(3)/锁匠审核员(4)，默认2", allowableValues = "1,2,3,4")
    private int type = 2;

    /**
     * 密码
     */
    @Length(min = 6, max = 20, message = "密码长度最小6位，最大20位")
//    @Pattern(regexp = "[0-9]{1,}[a-zA-Z]{1,}[-_!+*@]{0,}", message = "必须包含至少一个数字和字母")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\-_!+*@]{6,20}$", message = "密码必须包含至少一个数字和字母")
    @ApiModelProperty(notes = "密码6-20位，数字和字母组合")
    private String password;

    /**
     * 昵称
     */
    @ApiModelProperty(value="nickName昵称")
    private String nickName;

    /**
     * 性别，0未知，1男，2女
     */
    @ApiModelProperty(value="gender性别，0未知，1男，2女")
    private String gender;

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
     * 角色id列表
     */
    @ApiModelProperty(name = "角色id")
    private List<Long> roleIds;

    @ApiModelProperty(name = "额外数据字段")
    private JSONObject extraData;
    /**
     * 是否启用web登录
     */
    @Column(name = "is_weblogin_active")
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
     * 是否已启用
     */
    @ApiModelProperty(value="enabled是否已启用")
    private Boolean isEnabled;
    /**
     * 密码是否已过期
     */
    @ApiModelProperty(value="credentialsExpired密码是否已过期")
    private Boolean credentialsExpired;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public JSONObject getExtraData() {
        return extraData;
    }

    public void setExtraData(JSONObject extraData) {
        this.extraData = extraData;
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
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
}