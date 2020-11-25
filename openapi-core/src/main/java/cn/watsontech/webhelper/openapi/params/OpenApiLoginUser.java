package cn.watsontech.webhelper.openapi.params;


import cn.watsontech.webhelper.common.security.IUserType;
import cn.watsontech.webhelper.common.security.LoginUser;
import cn.watsontech.webhelper.openapi.params.base.OpenApiParams;

import javax.validation.constraints.NotNull;

/**
 * 主要给openapi 确认当前操作用户信息
 * Created by Watson on 2020/2/15.
 */
public class OpenApiLoginUser extends LoginUser implements OpenApiParams {

    @NotNull(message = "用户id不能为空")
    Long userId;
    @NotNull(message = "用户类型不能为空")
    IUserType userType;
    @NotNull(message = "用户名不能为空")
    String userName;

    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public IUserType getUserType() {
        return userType;
    }

    @Override
    public String getMobile() {
        return null;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public Boolean getExpired() {
        return false;
    }

    @Override
    public Boolean getLocked() {
        return false;
    }

    @Override
    public Boolean getCredentialsExpired() {
        return false;
    }

    @Override
    public Boolean getEnabled() {
        return true;
    }

    @Override
    public String getNickName() {
        return null;
    }

    @Override
    public String getAvatarUrl() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

}
