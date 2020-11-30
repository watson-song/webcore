package cn.watsontech.webhelper.common.service.user;


import cn.watsontech.webhelper.common.entity.User;
import cn.watsontech.webhelper.common.security.IUserLoginService;
import cn.watsontech.webhelper.mybatis.intf.Service;

/**
* Created by Watson Song on 2020/03/06.
*/
public interface UserService extends Service<User, Long>, IUserLoginService<Long> {

}