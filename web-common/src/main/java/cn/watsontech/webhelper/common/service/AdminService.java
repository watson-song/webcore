package cn.watsontech.webhelper.common.service;


import cn.watsontech.webhelper.common.entity.Admin;
import cn.watsontech.webhelper.common.security.IUserLoginService;
import cn.watsontech.webhelper.mybatis.intf.Service;

/**
* Created by Watson Song on 2020/03/06.
*/
public interface AdminService extends Service<Admin, Long>, IUserLoginService<Long> {

}