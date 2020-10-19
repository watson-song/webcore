package cn.watsontech.core.openapi.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * 示例service
 * Created by Watson on 2020/02/09.
 */
@Service
@Log4j2
public class OpenApiService {

    /**
     * 发送邮件
     * @param form
     */
    public boolean sendMail(Object form) {
        return true;
    }

}