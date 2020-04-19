package cn.watsontech.core.service.impl;

import cn.watsontech.core.service.AdminMessageService;
import cn.watsontech.core.service.intf.BaseService;
import cn.watsontech.core.service.mapper.AdminMessageMapper;
import cn.watsontech.core.web.spring.security.entity.AdminMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* Created by Watson Song on 2020/03/02.
*/
@Service
@Transactional
public class AdminMessageServiceImpl extends BaseService<AdminMessage, Long> implements AdminMessageService {

    @Autowired
    public AdminMessageServiceImpl(AdminMessageMapper mapper){
        super(mapper);
    }

    @Override
    public int insertMessage(AdminMessage message) {
        return insertSelective(message);
    }
}