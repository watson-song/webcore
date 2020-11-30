package cn.watsontech.webhelper.common.service.user;

import cn.watsontech.webhelper.common.entity.UserMessage;
import cn.watsontech.webhelper.common.service.mapper.user.UserMessageMapper;
import cn.watsontech.webhelper.mybatis.intf.BaseService;
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