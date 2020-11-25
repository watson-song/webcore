package cn.watsontech.webhelper.service.impl;

import cn.watsontech.webhelper.service.RoleService;
import cn.watsontech.webhelper.service.intf.BaseService;
import cn.watsontech.webhelper.service.mapper.RoleMapper;
import cn.watsontech.webhelper.web.spring.security.entity.Role;
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