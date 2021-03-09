
package cn.watsontech.webhelper.common.service.mapper.permission.manually;


import cn.watsontech.webhelper.common.vo.PermissionVo;
import cn.watsontech.webhelper.common.vo.PrinciplePermissionVo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

@Mapper
public interface PermissionManuallyMapper {

	/**
	 * 获取所有权限详细信息
	 * @return
	 */
	@Select(" select id, name, label, weight, enabled, created_by createdBy, created_by_name createdByName, created_time createdTime from tb_permission where parent_id is null and enabled = true")
	@Results({
			@Result(property = "id", column = "id"),
			@Result(property = "children", javaType=List.class, column="id", many = @Many(select = "selectAllChildPermissions")),
	})
    List<PermissionVo> selectAll();

	/**
	 * 获取所有某角色的权限列表
	 * @return
	 */
	@Select("select b.id, b.name, b.label, b.weight from ref_role_permission a left join tb_permission b on a.permission_id=b.id where b.parent_id is null and a.role_id=#{roleId} and b.enabled = true group by b.id")
	@Results({
			@Result(property = "id", column = "id"),
			@Result(property = "children", javaType=List.class, column="id", many = @Many(select = "selectAllChildPermissions")),
	})
	List<PermissionVo> selectAllByRoleId(long roleId);

	/**
	 * 获取所有某角色的权限列表
	 * @return
	 */
	@Select("select b.id, b.name, b.label, b.weight from ref_role_permission a left join tb_permission b on a.permission_id=b.id left join tb_role c on a.role_id=c.id where b.parent_id is null and c.name=#{name} and b.enabled = true group by b.id")
	@Results({
			@Result(property = "id", column = "id"),
			@Result(property = "children", javaType=List.class, column="id", many = @Many(select = "selectAllChildPermissions")),
	})
	List<PermissionVo> selectAllByRoleName(String name);

	/**
	 * 获取权限详细信息
	 * @param id  权限Id
	 * @return
	 */
	@Select("select id, name, label, weight, enabled, created_by createdBy, created_by_name createdByName, created_time createdTime from tb_permission where id=#{id}")
	@Results({
		@Result(property = "id", column = "id"),
		@Result(property = "children", javaType=List.class, column="id", many = @Many(select = "selectAllChildPermissions")),
	})
	PermissionVo selectInfoById(@Param("id") Long id);

	/**
	 * 获取权限详细信息
	 * @param name  权限名称
	 * @return
	 */
	@Select("select id, name, label, weight, enabled, created_by createdBy, created_by_name createdByName, created_time createdTime from tb_permission where name=#{name} limit 1")
	@Results({
			@Result(property = "id", column = "id"),
			@Result(property = "children", javaType=List.class, column="id", many = @Many(select = "selectAllChildPermissions")),
	})
	PermissionVo selectInfoByName(@Param("name") String name);

	/**
	 * 获取本权限下的所有子权限列表
	 * @param parentId 父亲谦虚Id
	 */
	@Select("select id, name, label from tb_permission where parent_id =#{parentId} and enabled = true")
	@Results({
			@Result(property = "id", column = "id"),
			@Result(property = "children", javaType=List.class, column="id", many = @Many(select = "selectAllChildPermissions")),
	})
	List<PermissionVo> selectAllChildPermissions(@Param("parentId") Long parentId);

    /**
     * 获取本权限下的所有子权限列表
     * @param parentId 父亲谦虚Id
     */
	@Select("select c.id, c.name, c.label, c.parent_id parentId, a.admin_id adminId from tb_permission c left join ref_role_permission b on b.permission_id=c.id left join ref_admin_role a on a.role_id=b.role_id left join tb_role d on a.role_id=d.id where c.parent_id =#{parentId} and a.admin_id=#{mallId} and c.enabled = true and d.enabled = true")
	@Results({
		@Result(property = "id", column = "id"),
		@Result(property = "children", javaType=Set.class, column = "{parentId=parentId,adminId=adminId}", many = @Many(select = "selectAllChildPrinciplePermissions")),
	})
    Set<PrinciplePermissionVo> selectAllChildPrinciplePermissionsByAdmin(@Param("adminId") Long adminId, @Param("parentId") Long parentId);

	/**
	 * 获取本角色下的所有权限集合
	 * @param roleId 角色Id
	 */
	@Select("select c.id, c.name, c.label, c.parent_id parentId from tb_permission c left join ref_role_permission b on b.permission_id=c.id left join tb_role a on b.role_id=a.id where a.id=#{roleId} and c.enabled = true and d.enabled = true")
	@Results({
			@Result(property = "id", column = "id"),
			@Result(property = "children", javaType=Set.class, column = "id", many = @Many(select = "selectAllChildPrinciplePermissions")),
	})
	Set<PrinciplePermissionVo> selectAllPrinciplePermissionsByRoleId(@Param("roleId") Long roleId);

	/**
	 * 获取本角色下的所有权限集合
	 * @param roleName 角色名称
	 */
	@Select("select c.id, c.name, c.label, c.parent_id parentId from tb_permission c left join ref_role_permission b on b.permission_id=c.id left join tb_role a on b.role_id=a.id where a.name=#{roleName} and c.enabled = true and d.enabled = true")
	@Results({
			@Result(property = "id", column = "id"),
			@Result(property = "children", javaType=Set.class, column = "id", many = @Many(select = "selectAllChildPrinciplePermissions")),
	})
	Set<PrinciplePermissionVo> selectAllPrinciplePermissionsByRoleName(@Param("roleName") String roleName);

	/**
	 * 获取本权限下的所有子权限列表
	 * @param parentId 父亲谦虚Id
	 */
	@Select("select id, name, label from tb_permission where parent_id =#{parentId} and enabled = true")
	@Results({
			@Result(property = "id", column = "id"),
			@Result(property = "children", javaType=Set.class, column="id", many = @Many(select = "selectAllChildPrinciplePermissions")),
	})
	Set<PrinciplePermissionVo> selectAllChildPrinciplePermissions(@Param("parentId") Long parentId);
}