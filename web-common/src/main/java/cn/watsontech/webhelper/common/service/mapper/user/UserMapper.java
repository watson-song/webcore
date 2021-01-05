package cn.watsontech.webhelper.common.service.mapper.user;


import cn.watsontech.webhelper.common.entity.User;
import cn.watsontech.webhelper.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper extends Mapper<User> {
}