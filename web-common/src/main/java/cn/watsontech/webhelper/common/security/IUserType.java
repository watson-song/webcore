package cn.watsontech.webhelper.common.security;

/**
 * 用户类型定义
 * Created by Watson on 2020/8/12.
 */
public interface IUserType {
    /**
     * 用户类型
     */
    String name();

    /**
     * 用户类型名称
     */
    String label();

    /**
     * 根据值返回对应的用户类型
     */
    IUserType valueFor(String name);
//
//    /**
//     * 获取用户类
//     */
//    Class getUserClass();
}
