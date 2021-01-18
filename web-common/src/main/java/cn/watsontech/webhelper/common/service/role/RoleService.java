package cn.watsontech.webhelper.common.service.role;


import cn.watsontech.webhelper.common.entity.Role;
import cn.watsontech.webhelper.common.form.CreateRoleForm;
import cn.watsontech.webhelper.common.security.LoginUser;
import cn.watsontech.webhelper.common.vo.PermissionVo;
import cn.watsontech.webhelper.common.vo.RoleVo;
import cn.watsontech.webhelper.mybatis.intf.Service;

import java.util.List;

/**
* Created by Watson Song on 2020/03/05.
*/
public interface RoleService extends Service<Role, Long> {

    /**
     * 加载当前角色所有权限集合
     * @param roleId 角色id
     */
    List<PermissionVo> loadAllPermissionsById(long roleId);

    /**
     * 加载当前角色详情
     * @param roleId 角色id
     */
    RoleVo loadInfoById(long roleId);

    /**
     * 创建角色
     * @param form 角色信息表单
     * @param user 创建人
     */
    Role createRole(CreateRoleForm form, LoginUser user);

    /**
     * 更新角色
     * @param roleId 角色id
     * @param form 角色信息表单
     * @param user 创建人
     */
    int updateRole(long roleId, CreateRoleForm form, LoginUser user);

    /**
     * 给角色添加权限集合
     * @param roleId 角色id
     * @param permissionIds 权限列表id
     * @param user 操作人
     */
    int addPermissions(long roleId, List<Long> permissionIds, LoginUser user);

    /**
     * 给角色添加权限集合
     * @param roleId 角色id
     * @param permissionId 权限id
     * @param user 操作人
     */
    int removePermission(long roleId, long permissionId, LoginUser user);

    /**
     * 给角色添加权限集合
     * @param roleId 角色id
     * @param user 操作人
     */
    int clearPermissions(long roleId, LoginUser user);
}