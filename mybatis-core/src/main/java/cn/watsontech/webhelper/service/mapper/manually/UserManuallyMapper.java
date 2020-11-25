
package cn.watsontech.webhelper.service.mapper.manually;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserManuallyMapper {

    /**
	 * 更新最后登录时间
	 */
	@Update("update tb_user set last_login_date=login_date, last_login_ip=login_ip, login_ip=#{ip}, login_date=now() where id = #{userId}")
	int updateLastLoginDate(@Param("userId") long userId, @Param("ip") String ip);

	/**
	 * 查询未读消息数量
	 * @param userId 用户id
	 */
	@Select("select count(*) from tb_user_message where user_id = #{userId} and state = 'unread'")
	int countUnreadMessage(@Param("userId") Long userId);
}