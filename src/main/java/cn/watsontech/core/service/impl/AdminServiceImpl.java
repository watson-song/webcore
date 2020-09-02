package cn.watsontech.core.service.impl;

import cn.watsontech.core.service.AdminService;
import cn.watsontech.core.service.intf.BaseService;
import cn.watsontech.core.service.manually.AdminManualService;
import cn.watsontech.core.service.mapper.AdminMapper;
import cn.watsontech.core.web.spring.security.LoginUser;
import cn.watsontech.core.web.spring.security.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;


/**
* Created by Watson Song on 2020/03/06.
*/
@Service
@Transactional
public class AdminServiceImpl extends BaseService<Admin, Long> implements AdminService {

    @Autowired
    AdminManualService adminManualService;

    @Autowired
    public AdminServiceImpl(AdminMapper mapper){
        super(mapper);
    }

    @Override
    public LoginUser loadUserByUsername(String username) {
        return loadUserByUserIdentity("username", username, defaultLoginSelectProperties(), true);
    }

    @Override
    public LoginUser loadUserByUsername(String username, String[] selectProperties, boolean checkEnabled) {
        return loadUserByUserIdentity("username", username, selectProperties, checkEnabled);
    }

    @Override
    public LoginUser loadUserByUserIdentity(String identity, Object identityValue, String[] selectProperties, boolean checkEnabled) {
        Condition condition = new Condition(Admin.class);
        condition.selectProperties(selectProperties);
        Example.Criteria criteria = condition.createCriteria().andEqualTo(identity, identityValue);
        if (checkEnabled) {
            criteria.andEqualTo("enabled", true).andEqualTo("locked", false);
        }
        LoginUser loginUser = selectFirstByCondition(condition);

        if (loginUser!=null) {
            loginUser.setUnreadMessages(countUnreadMessages(loginUser.getId()));
            loginUser.setRoles(loadUserRoles(loginUser.getId()));
            loginUser.setPermissions(loadUserPermissions(loginUser.getId()));
        }
        return loginUser;
    }

    @Override
    public int countUnreadMessages(Long userId) {
        return adminManualService.countUnreadMessages(userId);
    }

    @Override
    public List<Map<String, Object>> loadUserRoles(Long userId) {
        return adminManualService.getAllRoles(userId);
    }

    @Override
    public List<Map<String, Object>> loadUserPermissions(Long userId) {
        return adminManualService.getAllPermissions(userId);
    }

    @Override
    public int updateLastLoginData(String loginIp, Long userId) {
        return adminManualService.updateLastLoginData(loginIp, userId);
    }

    @Override
    public String[] defaultLoginSelectProperties() {
        return new String[]{"id", "username", "password", "nickName", "gender", "email", "avatarUrl", "mobile", "lastLoginDate", "lastLoginIp", "enabled", "expired", "locked", "credentialsExpired", "extraData", "type", "department", "title"};
    }
}