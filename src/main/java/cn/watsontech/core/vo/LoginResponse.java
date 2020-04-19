package cn.watsontech.core.vo;

import cn.watsontech.core.web.spring.security.LoginUser;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * Created by Watson on 2019/10/9.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {

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
}