
package cn.watsontech.webhelper.common.service.mapper.permission.manually;


import cn.watsontech.webhelper.common.vo.PermissionVo;
import cn.watsontech.webhelper.common.vo.PrinciplePermissionVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

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
	@Select(" select b.id, b.name, b.label, b.weight from ref_role_permission a left join tb_permission b on a.permission_id=b.id where b.parent_id is null and a.role_id=#{roleId} and b.enabled = true")
	@Results({
			@Result(property = "id", column = "id"),
			@Result(property = "children", javaType=List.class, column="id", many = @Many(select = "selectAllChildPermissions")),
	})
	List<PermissionVo> selectAllByRoleId(long roleId);

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
     * 获取本权限下的所有子权限列表
     * @param parentId 父亲谦虚Id
     */
	@Select("select id, name, label from tb_permission where parent_id =#{parentId} and enabled = true")
	@Results({
		@Result(property = "id", column = "id"),
		@Result(property = "children", javaType=List.class, column="id", many = @Many(select = "selectAllChildPermissions")),
	})
    List<PrinciplePermissionVo> selectAllChildPermissions(@Param("parentId") Long parentId);

}