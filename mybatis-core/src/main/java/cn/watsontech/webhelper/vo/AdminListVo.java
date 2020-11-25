package cn.watsontech.webhelper.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Watson on 2020/7/28.
 */
@Data
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
}
