package cn.watsontech.core.service;


import cn.watsontech.core.web.spring.security.IUserLoginService;
import cn.watsontech.core.web.spring.security.entity.Admin;
import cn.watsontech.core.service.intf.Service;

/**
* Created by Watson Song on 2020/03/06.
*/
public interface AdminService extends Service<Admin, Long>, IUserLoginService<Long> {

}