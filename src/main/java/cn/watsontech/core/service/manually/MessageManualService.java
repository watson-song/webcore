package cn.watsontech.core.service.manually;

import cn.watsontech.core.service.mapper.manually.MessageManualMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Watson on 2020/2/26.
 */
@Service
public class MessageManualService {

    @Autowired
    MessageManualMapper manualMapper;

    /**
     * 加载所有 未读消息
     */
    public int countAdminUnreadMessages(long userId) {
        return manualMapper.countAdminUnreadMessage(userId);
    }

    public int countUserUnreadMessages(long userId) {
        return manualMapper.countUserUnreadMessage(userId);
    }

}
