package cn.watsontech.webhelper.common.security;

import cn.watsontech.webhelper.common.vo.PrinciplePermissionVo;
import cn.watsontech.webhelper.common.vo.PrincipleRoleVo;

import java.util.Set;

/**
 * Created by Watson on 2020/8/12.
 */
public interface IUserLoginService<T> {

    /**
     * 根据用户名查询用户
     * 注意：需加载用户roles和permissions
     */
    LoginUser loadUserByUsername(String username);

    /**
     * 根据用户名查询用户
     * @param selectProperties 查询的参数列表
     * @param checkEnabled 是否检查启用和禁用状态
     * 注意：需加载用户roles和permissions
     */
    LoginUser loadUserByUsername(String username, String[] selectProperties, boolean checkEnabled);

    /**
     * 根据用户唯一标识查询用户
     *
     * @param identity 可以是 username,id,openid
     * @param selectProperties 查询的参数列表
     * @param checkEnabled 是否检查启用和禁用状态
     * 注意：需加载用户roles和permissions
     */
    LoginUser loadUserByUserIdentity(String identity, Object identityValue, String[] selectProperties, boolean checkEnabled);

    /**
     * 查询用户未读消息数量
     * @param userId
     */
    int countUnreadMessages(T userId);

    /**
     * 加载用户角色
     */
    Set<PrincipleRoleVo> loadUserRoles(T userId);

    /**
     * 加载用户权限
     */
    Set<PrinciplePermissionVo> loadUserPermissions(T userId);

    /**
     * 更新最后登录时间
     * @param loginIp 登录ip地址
     */
    int updateLastLoginData(String loginIp, T userId);

    /**
     * 登录查询属性列表 默认登录查询参数：id,username,nickName,gender,avatarUrl,mobile,lastLoginDate,lastLoginIp,enabled,expired,locked,credentialsExpired,extraData,createdTime
     * 注意：此处可通过修改属性来修改该方法返回值：loginService.defaultLoginSelectProperties.[userType]，比如：admin,user
     */
    String[] defaultLoginSelectProperties();
}
