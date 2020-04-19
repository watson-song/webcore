package cn.watsontech.core.service.impl;

import cn.watsontech.core.service.UserService;
import cn.watsontech.core.service.intf.BaseService;
import cn.watsontech.core.service.mapper.UserMapper;
import cn.watsontech.core.web.spring.security.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

}