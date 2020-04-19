package cn.watsontech.core.service.impl;

import cn.watsontech.core.service.RoleService;
import cn.watsontech.core.service.intf.BaseService;
import cn.watsontech.core.service.mapper.RoleMapper;
import cn.watsontech.core.web.spring.security.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* Created by Watson Song on 2020/03/05.
*/
@Service
@Transactional
public class RoleServiceImpl extends BaseService<Role, Long> implements RoleService {

    @Autowired
    public RoleServiceImpl(RoleMapper mapper){
        super(mapper);
    }

}