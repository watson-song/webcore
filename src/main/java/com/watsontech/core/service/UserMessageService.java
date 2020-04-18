package com.watsontech.core.service;


import com.watsontech.core.web.spring.security.entity.UserMessage;
import com.watsontech.core.service.intf.MessageService;
import com.watsontech.core.service.intf.Service;

/**
* Created by Watson Song on 2020/02/26.
*/
public interface UserMessageService extends Service<UserMessage, Long>, MessageService<UserMessage> {

}