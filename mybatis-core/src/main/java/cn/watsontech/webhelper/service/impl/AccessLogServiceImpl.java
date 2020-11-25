package cn.watsontech.webhelper.service.impl;

import cn.watsontech.webhelper.service.AccessLogService;
import cn.watsontech.webhelper.service.intf.BaseService;
import cn.watsontech.webhelper.service.mapper.AccessLogMapper;
import cn.watsontech.webhelper.web.spring.security.entity.AccessLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* Created by Watson Song on 2020/03/05.
*/
@Service
@Transactional
public class AccessLogServiceImpl extends BaseService<AccessLog, Long> implements AccessLogService {

    @Autowired
    public AccessLogServiceImpl(AccessLogMapper mapper){
        super(mapper);
    }

}