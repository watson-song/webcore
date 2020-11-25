package cn.watsontech.webhelper.service;


import cn.watsontech.webhelper.service.intf.MessageService;
import cn.watsontech.webhelper.web.spring.security.entity.AdminMessage;
import cn.watsontech.webhelper.service.intf.Service;

/**
* Created by Watson Song on 2020/02/26.
*/
public interface AdminMessageService extends Service<AdminMessage, Long>, MessageService<AdminMessage> {

}