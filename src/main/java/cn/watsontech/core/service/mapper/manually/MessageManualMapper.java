package cn.watsontech.core.service.mapper.manually;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Watson on 2020/2/24.
 */
@Mapper
public interface MessageManualMapper {

    @Select("select count(*) from tb_user_message where user_id = #{userId} and state = 'unread'")
    int countUserUnreadMessage(@Param("userId") Long userId);

    @Select("select count(*) from tb_admin_message where user_id = #{userId} and state = 'unread'")
    int countAdminUnreadMessage(@Param("userId") Long userId);

}