package cn.watsontech.core.service.manually;

import cn.watsontech.core.service.mapper.manually.UserManuallyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Watson on 2020/2/26.
 */
@Service
public class UserManualService {

    @Autowired
    UserManuallyMapper manuallyMapper;

    /**
     * 更新用户最后登录时间和ip
     */
    public int updateLastLoginData(String ip, long userId) {
        return manuallyMapper.updateLastLoginDate(userId, ip);
    }

    /**
     * 查询未读消息数量
     * @param userId 用户id
     * @return 未读消息数
     */
    public int countUnreadMessages(long userId) {
        return manuallyMapper.countUnreadMessage(userId);
    }
}
