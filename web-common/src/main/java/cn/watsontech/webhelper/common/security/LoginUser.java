package cn.watsontech.webhelper.common.security;

import cn.watsontech.webhelper.common.entity.Admin;
import cn.watsontech.webhelper.common.entity.User;
import cn.watsontech.webhelper.common.vo.PrinciplePermissionVo;
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

        @Override
        public String toString() {
            return name();
        }

        @Override
        public Class<LoginUser> getUserClass() {
            return userClaz;
        }
    }

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "账户ID")
    public abstract Long getId();

    //获取用户类型，admin或user
//    @JsonIgnore @Robin 打开用户类型
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

    @ApiModelProperty(value = "账户是否启用(替代 enabled，仅为enabled别名)")
    public abstract Boolean getEnabled();

    @ApiModelProperty(value = "获取用户昵称")
    public abstract String getNickName();

    @ApiModelProperty(value = "获取用户头像")
    public abstract String getAvatarUrl();

    @JsonIgnore
    @ApiModelProperty(value = "账户数据版本号")
    public Integer getVersion() {
        return null;
    }

    /**
     * 是否已认证/备案
     */
    @ApiModelProperty(value = " 用户是否已认证/锁匠是否已备案")
    public Boolean isVerified() {
        return null;
    }

    @ApiModelProperty(value = "获取用户名（优先获取昵称，昵称为空则返回username）")
    @JsonIgnore
    public String getName() {
        if (getNickName()!=null) return getNickName();

        return getUsername();
    }

    @ApiModelProperty(value = "获取昵称加用户名，nickName(username)")
    @JsonIgnore
    public String getFullName() {
        if (getNickName()!=null) return String.format("%s(%s)", getNickName(), getUsername());

        return getUsername();
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
    Set<Map<String, Object>> roles = new HashSet<>();

    //用户权限
    @Transient
    Set<PrinciplePermissionVo> permissions = new HashSet<>();

//    @JsonIgnore
    @ApiModelProperty(value = "用户角色")
    public Set<Map<String, Object>> getRoles() {
        return roles;
    }

    public void setRoles(Set<Map<String, Object>> roles) {
        this.roles = roles;
    }

    @ApiModelProperty(value = "用户权限集合")
    public Set<PrinciplePermissionVo> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PrinciplePermissionVo> permissions) {
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

        return roleList.stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role)).collect(Collectors.toList());
    }

    @Override
    public boolean isEnabled() {
        Boolean value = getEnabled();
        return value==null?false:value;
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
