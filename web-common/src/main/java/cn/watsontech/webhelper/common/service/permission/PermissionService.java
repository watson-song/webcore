package cn.watsontech.webhelper.common.service.permission;


import cn.watsontech.webhelper.common.entity.Permission;
import cn.watsontech.webhelper.common.vo.PermissionVo;
import cn.watsontech.webhelper.common.vo.PrinciplePermissionVo;
import cn.watsontech.webhelper.mybatis.intf.Service;

import java.util.List;

/**
* Created by Watson Song on 2020/03/05.
*/
public interface PermissionService extends Service<Permission, Long> {

    /**
     * 加载所有权限集合
     */
    List<PermissionVo> loadAll();

    /**
     * 加载当前角色所有权限集合
     * @param roleId
     */
    List<PermissionVo> loadAllByRoleId(long roleId);

    /**
     * 加载当前角色详情
     * @param permissionId
     */
    PermissionVo loadInfoById(long permissionId);

    /**
     * 加载当前权限所有子权限集合
     * @param parentId
     */
    List<PermissionVo> loadAllChild(long parentId);

    /**
     * 加载当前权限所有子权限集合
     * @param parentId
     */
    List<PrinciplePermissionVo> loadAllPrincipleChild(long parentId);
}