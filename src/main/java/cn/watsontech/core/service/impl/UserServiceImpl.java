package cn.watsontech.core.service.impl;

import cn.watsontech.core.service.UserService;
import cn.watsontech.core.service.intf.BaseService;
import cn.watsontech.core.service.mapper.UserMapper;
import cn.watsontech.core.web.spring.security.LoginUser;
import cn.watsontech.core.web.spring.security.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
* Created by Watson Song on 2020/03/06.
*/
@Service
@Transactional
public class UserServiceImpl extends BaseService<User, Long> implements UserService {

    @Autowired
    public UserServiceImpl(UserMapper mapper){
        super(mapper);
    }

    @Override
    public LoginUser loadUserByUsername(String username) {
        return null;
    }

    @Override
    public LoginUser loadUserByUsername(String username, List<String> selectProperties, boolean checkEnabled) {
        return null;
    }

    @Override
    public LoginUser loadUserByUserIdentity(String identity, Object identityValue, List<String> selectProperties, boolean checkEnabled) {
        return null;
    }

    @Override
    public int countUnreadMessages(Object userId) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> loadUserRoles() {
        return null;
    }

    @Override
    public List<Map<String, Object>> loadUserPermissions() {
        return null;
    }

    @Override
    public int updateLastLoginData(String loginIp) {
        return 0;
    }
}