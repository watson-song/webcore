package cn.watsontech.core.web.spring.security;

import cn.watsontech.core.web.spring.security.entity.Admin;
import cn.watsontech.core.web.spring.security.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import javax.persistence.Transient;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统一admin和user登录
 *
 * Created by Watson on 2019/12/26.
 */
public abstract class LoginUser implements UserDetails {
    public enum Type implements IUserType {
        admin("管理员", Admin.class)/*管理员 or 运营人员*/, user("用户", User.class)/*顾客*/, unknow("未知", null);

        String label;
        Class userClaz;

        Type(String label, Class claz) {
            this.label = label;
            this.userClaz = claz;
        }

        @Override
        public String label() {
            return this.label;
        }

        @Override
        public Type valueFor(String name) {
            return valueOf(name);
        }
//
//        @Override
//        public Class getUserClass() {
//            return userClaz;
//        }

        @Override
        public String toString() {
            return name();
        }
    }

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "账户ID")
    public abstract Long getId();

    //获取用户类型，admin或user
    @JsonIgnore
    @ApiModelProperty(value = "用户类型，admin或user")
    public abstract IUserType getUserType();

    //用户绑定的手机号码
    @ApiModelProperty(value = "用户绑定的手机号码")
    public abstract String getMobile();

    @JsonIgnore
    @ApiModelProperty(value = "账户是否过期")
    public abstract Boolean getExpired();

    @JsonIgnore
    @ApiModelProperty(value = "账户是否锁定")
    public abstract Boolean getLocked();

    @JsonIgnore
    @ApiModelProperty(value = "账户密码是否过期")
    public abstract Boolean getCredentialsExpired();

    @JsonIgnore
    public abstract String getPassword();

    @ApiModelProperty(value = "账户是否启用")
    public abstract boolean isEnabled();

    /**
     * 仅小程序用户 有openid
     */
    @ApiModelProperty(value = " 仅小程序用户有openid")
    public String getOpenid() { return null; }

    /**
     * 获取昵称
     */
    @ApiModelProperty(value = "获取用户昵称")
    public abstract String getNickName();

    @ApiModelProperty(value = "获取用户头像")
    public String getAvatarUrl(){ return null;}

    /**
     * 是否已认证/备案
     */
    @ApiModelProperty(value = " 用户是否已认证/锁匠是否已备案")
    public boolean isVerified() {
        return false;
    }

    @Transient
    @ApiModelProperty(value = "未读消息数")
    int unreadMessages = 0;

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(int unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    //用户角色
    @Transient
    List<Map<String, Object>> roles = new ArrayList<>();

    //用户权限
    @Transient
    List<Map<String, Object>> permissions = new ArrayList<>();

    @JsonIgnore
    @ApiModelProperty(value = "用户角色")
    public List<Map<String, Object>> getRoles() {
        return roles;
    }

    public void setRoles(List<Map<String, Object>> roles) {
        this.roles = roles;
    }

    @ApiModelProperty(value = "用户权限集合")
    public List<Map<String, Object>> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Map<String, Object>> permissions) {
        this.permissions = permissions;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<String> roleList = new HashSet<>();
        roleList.add(getUserType().name());

        if (!CollectionUtils.isEmpty(roles)) {
            roleList.addAll(roles.stream().filter(role -> role!=null&&role.containsKey("name")).map(role -> String.valueOf(role.getOrDefault("name", "NOBODY"))).collect(Collectors.toSet()));
        }

        return roleList.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        Boolean value = getExpired();
        return value==null?false:!value;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        Boolean value = getLocked();
        return value==null?false:!value;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        Boolean value = getCredentialsExpired();
        return value==null?false:!value;
    }
}
