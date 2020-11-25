package cn.watsontech.webhelper.mybatis.intf;

/**
 * 实体可记录状态
 * Created by Watson on 2020/2/25.
 */
public interface Stateful {
    String getNo();
    Long getId();

    String getState();
    String getStateDesc();
}
