package cn.watsontech.core.service;


import cn.watsontech.core.service.intf.MessageService;
import cn.watsontech.core.web.spring.security.entity.UserMessage;
import cn.watsontech.core.service.intf.Service;

/**
* Created by Watson Song on 2020/02/26.
*/
public interface UserMessageService extends Service<UserMessage, Long>, MessageService<UserMessage> {

}