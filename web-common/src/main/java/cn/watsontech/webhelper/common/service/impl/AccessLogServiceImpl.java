package cn.watsontech.webhelper.common.service.impl;

import cn.watsontech.webhelper.common.aop.entity.AccessLog;
import cn.watsontech.webhelper.common.service.AccessLogService;
import cn.watsontech.webhelper.common.service.mapper.AccessLogMapper;
import cn.watsontech.webhelper.mybatis.intf.BaseService;
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