package cn.watsontech.core.service.impl;

import cn.watsontech.core.service.AdminService;
import cn.watsontech.core.service.intf.BaseService;
import cn.watsontech.core.service.mapper.AdminMapper;
import cn.watsontech.core.web.spring.security.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* Created by Watson Song on 2020/03/06.
*/
@Service
@Transactional
public class AdminServiceImpl extends BaseService<Admin, Long> implements AdminService {

    @Autowired
    public AdminServiceImpl(AdminMapper mapper){
        super(mapper);
    }

}