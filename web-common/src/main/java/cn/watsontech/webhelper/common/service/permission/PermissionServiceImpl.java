package cn.watsontech.webhelper.common.service.permission;

import cn.watsontech.webhelper.common.entity.Permission;
import cn.watsontech.webhelper.common.service.mapper.permission.PermissionMapper;
import cn.watsontech.webhelper.common.service.mapper.permission.manually.PermissionManuallyMapper;
import cn.watsontech.webhelper.common.vo.PermissionVo;
import cn.watsontech.webhelper.common.vo.PrinciplePermissionVo;
import cn.watsontech.webhelper.mybatis.intf.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


/**
* Created by Watson Song on 2020/03/05.
*/
@Service
@Transactional
public class PermissionServiceImpl extends BaseService<Permission, Long> implements PermissionService {

    PermissionManuallyMapper voMapper;

    @Autowired
    public PermissionServiceImpl(PermissionMapper mapper, PermissionManuallyMapper voMapper){
        super(mapper);
        this.voMapper = voMapper;
    }

    /**
     * 加载所有权限集合
     */
    @Override
    public List<PermissionVo> loadAll() {
        return voMapper.selectAll();
    }

    /**
     * 加载当前角色所有权限集合
     * @param roleId
     */
    @Override
    public List<PermissionVo> loadAllByRoleId(long roleId) {
        return voMapper.selectAllByRoleId(roleId);
    }

    /**
     * 加载当前角色详情
     * @param permissionId
     */
    @Override
    public PermissionVo loadInfoById(long permissionId) {
        return voMapper.selectInfoById(permissionId);
    }

    /**
     * 加载当前权限所有子权限集合
     * @param parentId
     */
    @Override
    public List<PermissionVo> loadAllChild(long parentId) {
        return voMapper.selectAllChildPermissions(parentId);
    }

    /**
     * 加载当前权限所有子权限集合
     * @param parentId
     */
    @Override
    public Set<PrinciplePermissionVo> loadAllPrincipleChild(long adminId, long parentId) {
        return voMapper.selectAllChildPrinciplePermissions(adminId, parentId);
    }

}