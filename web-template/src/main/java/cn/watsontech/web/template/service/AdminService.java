/*
* Copyright (c) 2020.  -- 
*/
package cn.watsontech.web.template.service;
import cn.watsontech.webhelper.common.security.IUserLoginService;
import cn.watsontech.web.template.entity.Admin;
import cn.watsontech.webhelper.mybatis.intf.Service;

/**
* Created by Your name on 2020/11/30.
*/
public interface AdminService extends Service<Admin, Long>, IUserLoginService<Admin> {

}