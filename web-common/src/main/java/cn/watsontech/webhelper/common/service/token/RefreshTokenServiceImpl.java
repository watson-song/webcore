package cn.watsontech.webhelper.common.service.token;

import cn.watsontech.webhelper.common.entity.RefreshToken;
import cn.watsontech.webhelper.common.service.mapper.token.RefreshTokenMapper;
import cn.watsontech.webhelper.mybatis.intf.BaseService;
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