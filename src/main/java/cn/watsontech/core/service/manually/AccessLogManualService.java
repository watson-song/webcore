package cn.watsontech.core.service.manually;

import cn.watsontech.core.service.mapper.manually.AccessLogManualMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Watson on 2019/12/21.
 */
@Service
public class AccessLogManualService {
    @Autowired
    AccessLogManualMapper accessLogManualMapper;

    public int selectCount4SystemLog() {
        return accessLogManualMapper.selectCount();
    }

}
