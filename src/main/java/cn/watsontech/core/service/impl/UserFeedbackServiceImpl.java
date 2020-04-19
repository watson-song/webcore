package cn.watsontech.core.service.impl;

import cn.watsontech.core.service.UserFeedbackService;
import cn.watsontech.core.service.intf.BaseService;
import cn.watsontech.core.service.mapper.UserFeedbackMapper;
import cn.watsontech.core.web.spring.security.entity.UserFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* Created by Watson Song on 2020/03/05.
*/
@Service
@Transactional
public class UserFeedbackServiceImpl extends BaseService<UserFeedback, Long> implements UserFeedbackService {

    @Autowired
    public UserFeedbackServiceImpl(UserFeedbackMapper mapper){
        super(mapper);
    }

}