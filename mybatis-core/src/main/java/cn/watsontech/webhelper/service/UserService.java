package cn.watsontech.webhelper.service;


import cn.watsontech.webhelper.web.spring.security.IUserLoginService;
import cn.watsontech.webhelper.web.spring.security.entity.User;
import cn.watsontech.webhelper.service.intf.Service;

/**
* Created by Watson Song on 2020/03/06.
*/
public interface UserService extends Service<User, Long>, IUserLoginService<Long> {

}