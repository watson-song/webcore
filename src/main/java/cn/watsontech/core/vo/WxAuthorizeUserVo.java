package cn.watsontech.core.vo;

import lombok.Data;

/**
 * Created by Watson on 2020/10/24.
 */
@Data
public class WxAuthorizeUserVo {
    private Long id;

    /**
     * 微信openid
     */
    private String openid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别，0未知，1男，2女
     */
    private String gender;

//    private Date createdTime;

    /**
     * 语言
     */
    private String language;

    /**
     * 城市
     */
    private String city;

    /**
     * 省
     */
    private String province;

    /**
     * 国家
     */
    private String country;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 电话号码
     */
    private String telephone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 地址
     */
    private String address;

    /**
     * appid
     */
    private String appid;

    /**
     * unionid
     */
    private String unionid;

    /**
     * 是否订阅了公众号
     */
    private Boolean isSubscribe;

    /**
     * 公众号订阅时间
     */
    private Long subscribeTime;

    /**
     * 公众号订阅场景值
     */
    private String subscribeScene;

    /**
     * 微信备注
     */
    private String wxRemark;

    /**
     * 微信tagids
     */
    private String wxTagIds;

    /**
     * 是否已授权获取头像
     */
    private Boolean logged;

    /**
     * 系统标签
     */
    private String osTags;

    /**
     * 设备标签
     */
    private String deviceTags;
}
