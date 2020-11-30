
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
	@Select(" select id,name,label,tag,status,type,builtin_type builtinType,enabled,created_by createdBy, created_by_name createdByName, created_time createdTime from tb_role where id=#{roleId}")
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

}