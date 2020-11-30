package cn.watsontech.webhelper.common.service.admin;


import cn.watsontech.webhelper.common.entity.AdminMessage;
import cn.watsontech.webhelper.common.service.MessageService;
import cn.watsontech.webhelper.mybatis.intf.Service;

/**
* Created by Watson Song on 2020/02/26.
*/
public interface AdminMessageService extends Service<AdminMessage, Long>, MessageService<AdminMessage> {

}