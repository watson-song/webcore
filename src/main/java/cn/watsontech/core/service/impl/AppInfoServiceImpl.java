package cn.watsontech.core.service.impl;

import cn.watsontech.core.service.AppInfoService;
import cn.watsontech.core.service.intf.BaseService;
import cn.watsontech.core.service.mapper.AppInfoMapper;
import cn.watsontech.core.web.spring.security.entity.AppInfo;
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