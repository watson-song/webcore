package cn.watsontech.webhelper.common.service.role;

import cn.watsontech.webhelper.common.entity.Role;
import cn.watsontech.webhelper.common.form.CreateRoleForm;
import cn.watsontech.webhelper.common.security.LoginUser;
import cn.watsontech.webhelper.common.service.mapper.role.RoleMapper;
import cn.watsontech.webhelper.common.service.mapper.role.manually.RoleManuallyMapper;
import cn.watsontech.webhelper.common.vo.PermissionVo;
import cn.watsontech.webhelper.common.vo.RoleVo;
import cn.watsontech.webhelper.mybatis.intf.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
* Created by Watson Song on 2020/03/05.
*/
@Service
@Transactional
public class RoleServiceImpl extends BaseService<Role, Long> implements RoleService {

    RoleManuallyMapper roleVoMapper;

    @Autowired
    public RoleServiceImpl(RoleMapper mapper, RoleManuallyMapper roleVoMapper){
        super(mapper);
        this.roleVoMapper = roleVoMapper;
    }

    /**
     * 加载当前角色所有权限集合
     * @param roleId 角色id
     */
    @Override
    public List<PermissionVo> loadAllPermissionsById(long roleId) {
        return roleVoMapper.selectAllPermissionsByRoleId(roleId);
    }

    /**
     * 加载当前角色详情
     * @param roleId 角色id
     */
    @Override
    public RoleVo loadInfoById(long roleId) {
        return roleVoMapper.selectRoleInfoById(roleId);
    }

    /**
     * 给角色添加权限集合
     * @param roleId 角色id
     * @param permissionIds 权限列表id
     * @param user 操作人
     */
    @Override
    public int addPermissions(long roleId, List<Long> permissionIds, LoginUser user) {
        return roleVoMapper.addPermissionsToRole(permissionIds, roleId, user.getId(), user.getUsername());
    }

    /**
     * 给角色添加权限集合
     * @param roleId 角色id
     * @param permissionId 权限id
     * @param user 操作人
     */
    @Override
    public int removePermission(long roleId, long permissionId, LoginUser user) {
        return roleVoMapper.removeRolePermission(roleId, permissionId);
    }

    /**
     * 给角色添加权限集合
     * @param roleId 角色id
     * @param user 操作人
     */
    @Override
    public int clearPermissions(long roleId, LoginUser user) {
        return roleVoMapper.clearRolePermissions(roleId);
    }

    /**
     * 创建角色
     * @param form 角色信息表单
     * @param user 创建人
     */
    @Override
    @Transactional
    public Role createRole(CreateRoleForm form, LoginUser user) {
       Role role = form.getObject(user.getId(), user.getFullName());
       int success = insertSelective(role);

       //插入权限集合
       if (success>0) {
           int permissionSuccess = addPermissions(role.getId(), form.getPermissions(), user);
       }

       return role;
    }

    /**
     * 更新角色
     * @param roleId 角色id
     * @param form 角色信息表单
     * @param user 创建人
     */
    @Override
    @Transactional
    public int updateRole(long roleId, CreateRoleForm form, LoginUser user) {
        Role role = form.getUpdateObject(roleId, user.getId());
        int success = updateByPrimaryKeySelective(role);

        if (!CollectionUtils.isEmpty(form.getPermissions())) {
            //第一步：清空当前角色权限列表
            clearPermissions(roleId, user);

            //第二步：为当前角色添加权限列表
            addPermissions(roleId, form.getPermissions(), user);
        }

        return success;
    }
}