
package cn.watsontech.webhelper.common.service.mapper.admin.manually;


import cn.watsontech.webhelper.common.entity.Admin;
import cn.watsontech.webhelper.common.vo.AdminListVo;
import cn.watsontech.webhelper.common.vo.PrinciplePermissionVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminManuallyMapper {

	/**
	 * 获取管理员列表
	 */
	@Select("<script>select id, no, username, nick_name nickName, title, department, gender, type, avatar_url avatarUrl, mobile, email, enabled, created_by createdBy, created_by_name createdByName, created_time createdTime, last_login_date lastLoginDate from tb_admin " +
			"where 1=1 <if test='keywords!=null'> and username like #{keywords}</if> </script>")
	@Results({
			@Result(property = "id", column = "id"),
			@Result(property = "roles", javaType=List.class, column="id", many = @Many(select = "selectAllRolesByAdminId")),
			@Result(property = "permissions", javaType=List.class, column="id", many = @Many(select = "selectAllPermissionsByAdminId")),
	})
	List<AdminListVo> listAdminInfos(String keywords, RowBounds rowBounds);

	/**
	 * 获取管理员的详细信息
	 * @param adminId  管理员Id
	 * @return
	 */
	@Select(" select id, no, username, nick_name nickName, title, department, gender, type, avatar_url avatarUrl, mobile, email, enabled, created_by createdBy, created_by_name createdByName, created_time createdTime from tb_admin where id=#{adminId}")
	@Results({
		@Result(property = "id", column = "id"),
		@Result(property = "isEnabled", column = "enabled"),
		@Result(property = "roles", javaType=List.class, column="id", many = @Many(select = "selectAllRolesByAdminId")),
		@Result(property = "permissions", javaType=List.class, column="id", many = @Many(select = "selectAllPermissionsByAdminId")),
	})
	Admin selectAdminInfoById(@Param("adminId") Long adminId);

    /**
     * 获取本角色下的所有角色列表
     * @param adminId 管理员Id
     */
	@Select("select b.id,b.name,b.label from tb_role b left join ref_admin_role a on a.role_id=b.id and b.enabled = 1 where a.admin_id =#{adminId} group by b.id")
    List<Map<String, Object>> selectAllRolesByAdminId(@Param("adminId") Long adminId);

	/**
	 * 获取本账户下的所有权限列表
	 * @param adminId 管理员Id
	 */
	@Select("select c.id,c.name from tb_permission c left join ref_role_permission b on b.permission_id=c.id left join ref_admin_role a on a.role_id=b.role_id and c.enabled = true where a.admin_id =#{adminId} group by c.id")
	@Results({
			@Result(column = "id", property = "id"),
			@Result(property = "children", javaType=List.class, column="id", many = @Many(select = "cn.watsontech.webhelper.common.service.mapper.permission.manually.PermissionManuallyMapper.selectAllChildPrinciplePermissions")),
	})
	List<PrinciplePermissionVo> selectAllPermissionsByAdminId(@Param("adminId") Long adminId);

	/**
	 * 更新最后登录时间
	 */
	@Update("update tb_admin set last_login_date=login_date, last_login_ip=login_ip, login_ip=#{ip}, login_date=now() where id = #{userId}")
	int updateLastLoginDate(@Param("userId") long userId, @Param("ip") String ip);

	/**
	 * 查询未读消息数量
	 * @param userId 用户id
	 */
	@Select("select count(*) from tb_admin_message where user_id = #{userId} and state = 'unread'")
	int countUnreadMessage(@Param("userId") Long userId);

}