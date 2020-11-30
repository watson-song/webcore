package cn.watsontech.webhelper.common.service.siteoption;

import cn.watsontech.webhelper.common.entity.SiteOption;
import cn.watsontech.webhelper.common.service.mapper.siteoption.SiteOptionMapper;
import cn.watsontech.webhelper.mybatis.intf.BaseService;
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