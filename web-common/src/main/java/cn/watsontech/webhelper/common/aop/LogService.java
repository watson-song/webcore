package cn.watsontech.webhelper.common.aop;

import cn.watsontech.webhelper.common.aop.entity.AccessLog;

/**
 * Created by Watson on 2020/11/26.
 */
public interface LogService {

    /**
     * 保存操作记录
     */
    void save(AccessLog testLog);

    /**
     * 更新操作记录
     */
    void update(long id, AccessLog testLog);
}
