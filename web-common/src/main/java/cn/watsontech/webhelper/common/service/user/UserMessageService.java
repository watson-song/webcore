package cn.watsontech.webhelper.common.service.user;


import cn.watsontech.webhelper.common.entity.UserMessage;
import cn.watsontech.webhelper.common.service.MessageService;
import cn.watsontech.webhelper.mybatis.intf.Service;

/**
* Created by Watson Song on 2020/02/26.
*/
public interface UserMessageService extends Service<UserMessage, Long>, MessageService<UserMessage> {

}