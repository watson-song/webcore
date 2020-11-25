package cn.watsontech.webhelper.service.impl;

import cn.watsontech.webhelper.service.AdminMessageService;
import cn.watsontech.webhelper.service.intf.BaseService;
import cn.watsontech.webhelper.service.mapper.AdminMessageMapper;
import cn.watsontech.webhelper.web.spring.security.entity.AdminMessage;
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