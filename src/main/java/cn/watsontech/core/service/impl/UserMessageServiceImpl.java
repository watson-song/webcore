package cn.watsontech.core.service.impl;

import cn.watsontech.core.service.UserMessageService;
import cn.watsontech.core.service.intf.BaseService;
import cn.watsontech.core.service.mapper.UserMessageMapper;
import cn.watsontech.core.web.spring.security.entity.UserMessage;
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