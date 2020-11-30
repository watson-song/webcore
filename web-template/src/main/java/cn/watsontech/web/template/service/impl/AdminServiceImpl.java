/*
* Copyright (c) 2020.  -- 
*/
package cn.watsontech.web.template.service.impl;

import cn.watsontech.web.template.entity.Admin;
import cn.watsontech.web.template.mapper.AdminMapper;
import cn.watsontech.web.template.service.AdminService;
import cn.watsontech.webhelper.common.security.LoginUser;
import cn.watsontech.webhelper.utils.MapBuilder;
import cn.watsontech.webhelper.mybatis.intf.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
* Created by Your name on 2020/11/30.
*/
@Service
@Transactional
public class AdminServiceImpl extends BaseService<Admin, Long> implements AdminService {

    @Autowired
    public AdminServiceImpl(AdminMapper mapper){
        super(mapper);
    }

    @Override
    public LoginUser loadUserByUsername(String username) {
        return loadUserByUserIdentity("username", username, null, false);
    }

    @Override
    public LoginUser loadUserByUsername(String username, String[] selectProperties, boolean checkEnabled) {
        return loadUserByUserIdentity("username", username, selectProperties, checkEnabled);
    }

    @Override
    public LoginUser loadUserByUserIdentity(String identity, Object identityValue, String[] selectProperties, boolean checkEnabled) {
        Condition condition = new Condition(Admin.class);
        condition.createCriteria().andEqualTo(identity, identityValue);
        if (checkEnabled) {
            condition.and().andEqualTo("enabled", checkEnabled);
        }
        condition.selectProperties(selectProperties);
        return selectFirstByCondition(condition);
    }

    @Override
    public int countUnreadMessages(Admin userId) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> loadUserRoles(Admin userId) {
        return Arrays.asList(MapBuilder.builder().putNext("admin", "管理员"));
    }

    @Override
    public List<Map<String, Object>> loadUserPermissions(Admin userId) {
        return Arrays.asList(MapBuilder.builder().putNext("all", "所有权限"));
    }

    @Override
    public int updateLastLoginData(String loginIp, Admin userId) {

        //默认更新成功
        return 1;
    }

    @Override
    public String[] defaultLoginSelectProperties() {
        return new String[]{"id", "username", "password"};
    }
}