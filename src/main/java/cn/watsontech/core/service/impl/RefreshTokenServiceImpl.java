package cn.watsontech.core.service.impl;

import cn.watsontech.core.service.RefreshTokenService;
import cn.watsontech.core.service.intf.BaseService;
import cn.watsontech.core.service.mapper.RefreshTokenMapper;
import cn.watsontech.core.web.spring.security.entity.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* Created by Watson Song on 2020/03/06.
*/
@Service
@Transactional
public class RefreshTokenServiceImpl extends BaseService<RefreshToken, Long> implements RefreshTokenService {

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenMapper mapper){
        super(mapper);
    }

}