package cn.watsontech.core.service;


import cn.watsontech.core.web.spring.security.IUserLoginService;
import cn.watsontech.core.web.spring.security.entity.User;
import cn.watsontech.core.service.intf.Service;

/**
* Created by Watson Song on 2020/03/06.
*/
public interface UserService extends Service<User, Long>, IUserLoginService {

}