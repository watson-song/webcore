package cn.watsontech.webhelper.service.impl;

import cn.watsontech.webhelper.service.PermissionService;
import cn.watsontech.webhelper.service.intf.BaseService;
import cn.watsontech.webhelper.service.mapper.PermissionMapper;
import cn.watsontech.webhelper.web.spring.security.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* Created by Watson Song on 2020/03/05.
*/
@Service
@Transactional
public class PermissionServiceImpl extends BaseService<Permission, Long> implements PermissionService {

    @Autowired
    public PermissionServiceImpl(PermissionMapper mapper){
        super(mapper);
    }

}