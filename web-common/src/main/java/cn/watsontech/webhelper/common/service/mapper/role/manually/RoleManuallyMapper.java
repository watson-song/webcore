
package cn.watsontech.webhelper.common.service.mapper.role.manually;


import cn.watsontech.webhelper.common.vo.PermissionVo;
import cn.watsontech.webhelper.common.vo.RoleVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleManuallyMapper {

	/**
	 * 获取角色的详细信息
	 * @param roleId  角色Id
	 * @return
	 */
	@Select(" select id, name, label, tag, status, type, builtin_type builtinType, enabled, created_by createdBy, created_by_name createdByName, created_time createdTime from tb_role where id=#{roleId}")
	@Results({
		@Result(property = "id", column="id"),
		@Result(property = "permissions", javaType=List.class, column="id", many = @Many(select = "selectAllPermissionsByRoleId")),
	})
	RoleVo selectRoleInfoById(@Param("roleId") Long roleId);

    /**
     * 获取本角色下的所有权限列表
     * @param roleId 角色Id
     */
	@Select("select b.id, b.name, b.label from ref_role_permission a left join tb_permission b on a.permission_id=b.id and b.enabled = true where a.role_id =#{roleId}")
	@Results({
			@Result(property = "id", column="id"),
			@Result(property = "children", javaType=List.class, column="id", many = @Many(select = "cn.watsontech.webhelper.common.service.mapper.permission.manually.PermissionManuallyMapper.selectAllChildPermissions")),
	})
    List<PermissionVo> selectAllPermissionsByRoleId(@Param("roleId") Long roleId);

	/**
	 * 批量新增角色权限
	 * @param permission 权限列表
	 * @param roleId 角色ID
	 * @param userId 创建人ID
	 * @param userName 创建人名称
	 */
	@Insert(value = "<script>insert ignore into ref_role_permission(role_id, permission_id, created_by, created_by_name) values " +
			" <foreach collection=\"permissionIds\" separator=\",\" item=\"item\"> " +
			" (#{roleId}, #{item}, #{createdBy}, #{createdByName}) " +
			"</foreach></script>")
	int addPermissionsToRole(@Param("permissionIds") List<Long> permission, @Param("roleId") Long roleId, @Param("createdBy") Long userId, @Param("createdByName") String userName);

	/**
	 * 清空角色权限
	 * @param roleId 角色id
	 */
	@Delete("delete from ref_role_permission where role_id=#{roleId}")
	int clearRolePermissions(@Param("roleId") Long roleId);

	/**
	 * 删除角色某个权限
	 * @param roleId 角色id
	 * @param permissionId 权限id
	 */
	@Delete("delete from ref_role_permission where role_id=#{roleId} and permission_id=#{permissionId}")
	int removeRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);
}