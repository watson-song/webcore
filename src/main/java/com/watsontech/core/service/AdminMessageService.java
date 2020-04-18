package com.watsontech.core.service;


import com.watsontech.core.web.spring.security.entity.AdminMessage;
import com.watsontech.core.service.intf.MessageService;
import com.watsontech.core.service.intf.Service;

/**
* Created by Watson Song on 2020/02/26.
*/
public interface AdminMessageService extends Service<AdminMessage, Long>, MessageService<AdminMessage> {

}