package cn.watsontech.webhelper.openapi.params;


import cn.watsontech.webhelper.common.security.IUserType;
import cn.watsontech.webhelper.common.security.LoginUser;
import cn.watsontech.webhelper.common.security.UserTypeFactory;
import cn.watsontech.webhelper.common.vo.PrinciplePermissionVo;
import cn.watsontech.webhelper.openapi.params.base.OpenApiParams;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 主要给openapi 确认当前操作用户信息
 * Created by Watson on 2020/2/15.
 */
public class OpenApiLoginUser extends LoginUser implements OpenApiParams {

    @NotNull(message = "用户id不能为空")
    Long userId;
    @NotNull(message = "用户类型不能为空")
    String userType;
    @NotNull(message = "用户名不能为空")
    String userName;

    @Autowired
    @Transient
    @JsonIgnore
    UserTypeFactory userTypeFactory;

    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public IUserType getUserType() {
        if (userTypeFactory==null||userType==null) return Type.unknow;

        return userTypeFactory.valueOf(userType);
    }

    public void setUserType(String userType) {
        this.userType = userType;
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
    @Transient
    @JsonIgnore
    public String getMobile() {
        return null;
    }

    @Override
    @Transient
    @JsonIgnore
    public String getUsername() {
        return userName;
    }

    @Override
    @Transient
    @JsonIgnore
    public Boolean getExpired() {
        return false;
    }

    @Override
    @Transient
    @JsonIgnore
    public Boolean getLocked() {
        return false;
    }

    @Override
    @Transient
    @JsonIgnore
    public Boolean getCredentialsExpired() {
        return false;
    }

    @Override
    @Transient
    @JsonIgnore
    public Boolean getEnabled() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public String getNickName() {
        return null;
    }

    @Override
    @Transient
    @JsonIgnore
    public String getAvatarUrl() {
        return null;
    }

    @Override
    @Transient
    @JsonIgnore
    public String getPassword() {
        return null;
    }

    @Override
    @Transient
    @JsonIgnore
    public int getUnreadMessages() {
        return 0;
    }

    @Override
    @Transient
    @JsonIgnore
    public List<Map<String, Object>> getRoles() {
        return null;
    }

    @Override
    @Transient
    @JsonIgnore
    public List<PrinciplePermissionVo> getPermissions() {
        return null;
    }

    @Override
    @Transient
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
