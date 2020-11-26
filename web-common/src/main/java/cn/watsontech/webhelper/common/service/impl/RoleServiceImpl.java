package cn.watsontech.webhelper.common.service.impl;

import cn.watsontech.webhelper.common.entity.Role;
import cn.watsontech.webhelper.common.service.RoleService;
import cn.watsontech.webhelper.common.service.mapper.RoleMapper;
import cn.watsontech.webhelper.mybatis.intf.BaseService;
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