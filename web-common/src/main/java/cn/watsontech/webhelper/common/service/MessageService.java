package cn.watsontech.webhelper.common.service;

/**
 * Created by Watson on 2020/3/2.
 */
public interface MessageService<T extends Message> {
    enum MessageType {order/*订单消息*/, system/*系统消息*/}

    int insertMessage(T message);
}
