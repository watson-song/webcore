package cn.watsontech.webhelper.common.vo;

/**
 * Created by Watson on 2020/10/24.
 */
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public Boolean getSubscribe() {
        return isSubscribe;
    }

    public void setSubscribe(Boolean subscribe) {
        isSubscribe = subscribe;
    }

    public Long getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(Long subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String getSubscribeScene() {
        return subscribeScene;
    }

    public void setSubscribeScene(String subscribeScene) {
        this.subscribeScene = subscribeScene;
    }

    public String getWxRemark() {
        return wxRemark;
    }

    public void setWxRemark(String wxRemark) {
        this.wxRemark = wxRemark;
    }

    public String getWxTagIds() {
        return wxTagIds;
    }

    public void setWxTagIds(String wxTagIds) {
        this.wxTagIds = wxTagIds;
    }

    public Boolean getLogged() {
        return logged;
    }

    public void setLogged(Boolean logged) {
        this.logged = logged;
    }

    public String getOsTags() {
        return osTags;
    }

    public void setOsTags(String osTags) {
        this.osTags = osTags;
    }

    public String getDeviceTags() {
        return deviceTags;
    }

    public void setDeviceTags(String deviceTags) {
        this.deviceTags = deviceTags;
    }
}
