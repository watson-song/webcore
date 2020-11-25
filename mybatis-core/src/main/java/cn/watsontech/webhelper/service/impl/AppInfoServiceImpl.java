package cn.watsontech.webhelper.service.impl;

import cn.watsontech.webhelper.service.AppInfoService;
import cn.watsontech.webhelper.service.intf.BaseService;
import cn.watsontech.webhelper.service.mapper.AppInfoMapper;
import cn.watsontech.webhelper.web.spring.security.entity.AppInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* Created by Watson Song on 2020/02/12.
*/
@Service
@Transactional
public class AppInfoServiceImpl extends BaseService<AppInfo, Long> implements AppInfoService {

    @Autowired
    public AppInfoServiceImpl(AppInfoMapper mapper){
        super(mapper);
    }

}