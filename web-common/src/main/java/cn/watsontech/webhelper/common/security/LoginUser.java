package cn.watsontech.webhelper.common.security;

import cn.watsontech.webhelper.common.entity.Admin;
import cn.watsontech.webhelper.common.entity.User;
import cn.watsontech.webhelper.common.vo.PrinciplePermissionVo;
import cn.watsontech.webhelper.common.vo.PrincipleRoleVo;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import javax.persistence.Transient;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    @JSONField(serialize = false)
    @ApiModelProperty(value = "账户是否过期")
    public abstract Boolean getExpired();

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty(value = "账户是否锁定")
    public abstract Boolean getLocked();

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty(value = "账户密码是否过期")
    public abstract Boolean getCredentialsExpired();

    @JsonIgnore
    @JSONField(serialize = false)
    public abstract String getPassword();

    @ApiModelProperty(value = "账户是否启用(替代 enabled，仅为enabled别名)")
    public abstract Boolean getEnabled();

    @ApiModelProperty(value = "获取用户昵称")
    public abstract String getNickName();

    @ApiModelProperty(value = "获取用户头像")
    public abstract String getAvatarUrl();

    @JsonIgnore
    @JSONField(serialize = false)
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
    @JSONField(serialize = false)
    public String getName() {
        if (getNickName()!=null) return getNickName();

        return getUsername();
    }

    @ApiModelProperty(value = "获取昵称加用户名，nickName(username)")
    @JsonIgnore
    @JSONField(serialize = false)
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
    Set<PrincipleRoleVo> roles = new HashSet<>();

    //用户权限
    @Transient
    Set<PrinciplePermissionVo> permissions = new HashSet<>();

//    @JsonIgnore
    @ApiModelProperty(value = "用户角色")
    public Set<PrincipleRoleVo> getRoles() {
        return roles;
    }

    public void setRoles(Set<PrincipleRoleVo> roles) {
        this.roles = roles;
    }

    @ApiModelProperty(value = "用户角色名称")
    public String getRoleName() {
        if (this.roles!=null) {
            return this.roles.stream().findFirst().orElse(new PrincipleRoleVo()).getLabel();
        }
        return null;
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
    @JSONField(serialize = false)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<String> roleList = new HashSet<>();
        roleList.add(getUserType().name());

        if (!CollectionUtils.isEmpty(roles)) {
            roleList.addAll(roles.stream().filter(role -> role!=null&&role.getName()!=null).map(role -> role.getName()).collect(Collectors.toSet()));
        }

        return roleList.stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role)).collect(Collectors.toList());
    }

    /**
     * 检查是否有权限，注意：先检查当前权限，如未找到会继续检查所有children
     * @param permissionName 目标权限
     * @return 如检查到了某权限则返回true，否则返回false
     */
    public boolean hasPermission(String permissionName) {
        PrinciplePermissionVo permission = new PrinciplePermissionVo(null, permissionName, null);
        return hasPermission(permission);
    }
    public boolean hasPermission(PrinciplePermissionVo permission) {
        Set<PrinciplePermissionVo> permissions = getPermissions();
        if (!CollectionUtils.isEmpty(permissions)) {
            return permissions.stream().anyMatch((permissionVo -> permissionVo!=null&&permissionVo.check(permission)));
        }

        return false;
    }
    /**
     * 是否包含任意一个目标权限
     * @param anyPermissions 目标权限列表
     * @return 包含则返回true，否则返回false
     */
    public boolean hasAnyPermission(List<PrinciplePermissionVo> anyPermissions) {
        if (!CollectionUtils.isEmpty(anyPermissions)) {
            Set<PrinciplePermissionVo> permissions = getPermissions();
            if (!CollectionUtils.isEmpty(permissions)) {
                return anyPermissions.stream().anyMatch(anyPermission -> permissions.stream().anyMatch(permissionVo -> permissionVo!=null&&permissionVo.check(anyPermission)));
            }
        }

        return false;
    }
    /**
     * 是否包含所有目标权限
     * @param allPermissions 目标权限列表
     * @return 包含则返回true，否则返回false
     */
    public boolean hasAllPermissions(List<PrinciplePermissionVo> allPermissions) {
        if (!CollectionUtils.isEmpty(allPermissions)) {
            Set<PrinciplePermissionVo> permissions = getPermissions();
            if (!CollectionUtils.isEmpty(allPermissions)) {
                return allPermissions.stream().allMatch(anyPermission -> permissions.stream().anyMatch(permissionVo -> permissionVo!=null&&permissionVo.check(anyPermission)));
            }
        }

        return false;
    }

    /**
     * 检查是否有角色，注意：检查当前角色是否与目标角色相同
     * @param roleName 目标角色
     * @return 如检查到了某角色则返回true，否则返回false
     */
    public boolean hasRole(String roleName) {
        PrincipleRoleVo role = new PrincipleRoleVo(null, roleName, null);
        return hasRole(role);
    }
    public boolean hasRole(PrincipleRoleVo role) {
        if (role!=null) {
            Set<PrincipleRoleVo> roles = getRoles();
            if (!CollectionUtils.isEmpty(roles)) {
                return roles.stream().anyMatch((roleVo -> roleVo!=null&&roleVo.check(role)));
            }
        }

        return false;
    }
    /**
     * 是否包含任意一个目标角色
     * @param anyRoles 目标角色列表
     * @return 包含则返回true，否则返回false
     */
    public boolean hasAnyRole(List<PrincipleRoleVo> anyRoles) {
        if (!CollectionUtils.isEmpty(anyRoles)) {
            Set<PrincipleRoleVo> roles = getRoles();
            if (!CollectionUtils.isEmpty(roles)) {
                return anyRoles.stream().anyMatch(anyRole -> roles.stream().anyMatch(roleVo -> roleVo!=null&&roleVo.check(anyRole)));
            }
        }

        return false;
    }
    /**
     * 是否包含所有目标角色
     * @param allRoles 目标角色列表
     * @return 包含则返回true，否则返回false
     */
    public boolean hasAllRoles(List<PrincipleRoleVo> allRoles) {
        if (!CollectionUtils.isEmpty(allRoles)) {
            Set<PrincipleRoleVo> roles = getRoles();
            if (!CollectionUtils.isEmpty(roles)) {
                return allRoles.stream().allMatch(anyRole -> roles.stream().anyMatch(roleVo -> roleVo!=null&&roleVo.check(anyRole)));
            }
        }

        return false;
    }

    @Override
    public boolean isEnabled() {
        Boolean value = getEnabled();
        return value==null?false:value;
    }

    @Override
    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        Boolean value = getExpired();
        return value==null?false:!value;
    }

    @Override
    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        Boolean value = getLocked();
        return value==null?false:!value;
    }

    @Override
    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        Boolean value = getCredentialsExpired();
        return value==null?false:!value;
    }
}
