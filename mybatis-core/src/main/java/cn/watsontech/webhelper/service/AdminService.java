package cn.watsontech.webhelper.service;


import cn.watsontech.webhelper.web.spring.security.IUserLoginService;
import cn.watsontech.webhelper.web.spring.security.entity.Admin;
import cn.watsontech.webhelper.service.intf.Service;

/**
* Created by Watson Song on 2020/03/06.
*/
public interface AdminService extends Service<Admin, Long>, IUserLoginService<Long> {

}