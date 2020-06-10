
package cn.watsontech.core.service.mapper.manually;


import cn.watsontech.core.web.spring.security.entity.Admin;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminManuallyMapper {

	/**
	 * 获取管理员的详细信息
	 * @param adminId  管理员Id
	 * @return
	 */
	@Select(" select id, no, username, nick_name nickName, title, department, gender, type, avatar_url avatarUrl, mobile, email, enabled, created_by createdBy, created_by_name createdByName, created_time createdTime from tb_admin where id=#{adminId}")
	@Results({
		@Result(property = "roles", javaType=List.class, column="id", many = @Many(select = "selectAllRolesByAdminId")),
		@Result(property = "permissions", javaType=List.class, column="id", many = @Many(select = "selectAllPermissionsByAdminId")),
	})
    Admin selectAdminInfoById(@Param("adminId") Long adminId);

    /**
     * 获取本角色下的所有角色列表
     * @param adminId 管理员Id
     */
	@Select("select b.id,b.name,b.label from ref_admin_role a left join tb_role b on a.role_id=b.id and b.enabled = 1 where a.admin_id =#{adminId}")
    List<Map<String, Object>> selectAllRolesByAdminId(@Param("adminId") Long adminId);

	/**
	 * 获取本账户下的所有权限列表
	 * @param adminId 管理员Id
	 */
	@Select("select c.id,c.name,c.label from ref_admin_role a left join ref_role_permission b on a.role_id=b.role_id left join tb_permission c on b.permission_id=c.id and c.enabled = true where a.admin_id =#{adminId}")
	@Results({
			@Result(column = "id", property = "id"),
			@Result(property = "children", javaType=List.class, column="id", many = @Many(select = "cn.watsontech.core.service.mapper.manually.PermissionManuallyMapper.selectAllChildPermissions")),
	})
	List<Map<String, Object>> selectAllPermissionsByAdminId(@Param("adminId") Long adminId);

}