package cn.watsontech.core.service.impl;

import cn.watsontech.core.service.SiteOptionService;
import cn.watsontech.core.service.intf.BaseService;
import cn.watsontech.core.service.mapper.SiteOptionMapper;
import cn.watsontech.core.web.spring.security.entity.SiteOption;
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