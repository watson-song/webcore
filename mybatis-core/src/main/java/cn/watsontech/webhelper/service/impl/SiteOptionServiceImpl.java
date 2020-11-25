package cn.watsontech.webhelper.service.impl;

import cn.watsontech.webhelper.service.SiteOptionService;
import cn.watsontech.webhelper.service.intf.BaseService;
import cn.watsontech.webhelper.service.mapper.SiteOptionMapper;
import cn.watsontech.webhelper.web.spring.security.entity.SiteOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* Created by Watson Song on 2020/03/05.
*/
@Service
@Transactional
public class SiteOptionServiceImpl extends BaseService<SiteOption, Long> implements SiteOptionService {

    @Autowired
    public SiteOptionServiceImpl(SiteOptionMapper mapper){
        super(mapper);
    }

}