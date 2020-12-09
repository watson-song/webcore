package cn.watsontech.webhelper.common.vo;

import cn.watsontech.webhelper.common.security.LoginUser;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Watson on 2019/10/9.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {

    public LoginResponse() {}

    public LoginResponse(Integer errCode, String errMsg) {
        this.code = errCode;
        this.error = errMsg;
    }

    public LoginResponse(String token, LoginUser account) {
        this.token = token;
        this.account = account;
    }
    public LoginResponse(String token, String refreshToken, LoginUser account) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.account = account;
    }

    public LoginResponse(LoginUser account) {
        this.account = account;
    }

    @ApiModelProperty(value = "token令牌")
    String token;
    @ApiModelProperty(value = "刷新令牌（仅app端使用），token过期后使用refreshToken刷新token，refreshToken也过期则弹出登录框请求用户输入用户名密码登录")
    String refreshToken;
    @ApiModelProperty(value = "账户信息")
    LoginUser account;

    @ApiModelProperty(value = "错误编码，仅适用于微信登录，app端忽略该属性")
    Integer code;
    @ApiModelProperty(value = "错误消息，仅适用于微信登录，app端忽略该属性")
    String error;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LoginUser getAccount() {
        return account;
    }

    public void setAccount(LoginUser account) {
        this.account = account;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}