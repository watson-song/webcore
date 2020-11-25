package cn.watsontech.webhelper.service.impl;

import cn.watsontech.webhelper.service.UserMessageService;
import cn.watsontech.webhelper.service.intf.BaseService;
import cn.watsontech.webhelper.service.mapper.UserMessageMapper;
import cn.watsontech.webhelper.web.spring.security.entity.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* Created by Watson Song on 2020/03/02.
*/
@Service
@Transactional
public class UserMessageServiceImpl extends BaseService<UserMessage, Long> implements UserMessageService {

    @Autowired
    public UserMessageServiceImpl(UserMessageMapper mapper){
        super(mapper);
    }

    @Override
    public int insertMessage(UserMessage message) {
        return insertSelective(message);
    }
}