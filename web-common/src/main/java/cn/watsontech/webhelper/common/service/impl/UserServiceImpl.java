package cn.watsontech.webhelper.common.service.impl;

import cn.watsontech.webhelper.common.entity.User;
import cn.watsontech.webhelper.common.security.LoginUser;
import cn.watsontech.webhelper.common.service.UserService;
import cn.watsontech.webhelper.common.service.manually.UserManualService;
import cn.watsontech.webhelper.common.service.mapper.UserMapper;
import cn.watsontech.webhelper.mybatis.intf.BaseService;
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
public class UserServiceImpl extends BaseService<User, Long> implements UserService {

    @Autowired
    UserManualService manualService;

    @Autowired
    public UserServiceImpl(UserMapper mapper){
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
        Condition condition = new Condition(User.class);
        condition.selectProperties(selectProperties);
        Example.Criteria criteria = condition.createCriteria().andEqualTo(identity, identityValue);
        if (checkEnabled) {
            criteria.andEqualTo("isEnabled", true).andEqualTo("locked", false);
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
        return manualService.countUnreadMessages(userId);
    }

    @Override
    public List<Map<String, Object>> loadUserRoles(Long userId) {
        return null;
    }

    @Override
    public List<Map<String, Object>> loadUserPermissions(Long userId) {
        return null;
    }

    @Override
    public int updateLastLoginData(String loginIp, Long userId) {
        return manualService.updateLastLoginData(loginIp, userId);
    }

    @Override
    public String[] defaultLoginSelectProperties() {
        return new String[]{"id", "username", "password", "nickName", "gender", "email", "avatarUrl", "mobile", "lastLoginDate", "lastLoginIp", "isEnabled", "expired", "locked", "credentialsExpired", "extraData", "openid", "email", "logged", "version"};
    }
}