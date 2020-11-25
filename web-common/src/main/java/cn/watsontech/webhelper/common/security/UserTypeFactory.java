package cn.watsontech.webhelper.common.security;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 用户类型工厂
 * Created by Watson on 2020/8/12.
 */
public class UserTypeFactory implements InitializingBean {

    //登录用户服务
    Map<IUserType, IUserLoginService> loginUserServiceList = new HashMap<>();

    public UserTypeFactory(Map<IUserType, IUserLoginService> loginUserServiceList) {
        if (!CollectionUtils.isEmpty(loginUserServiceList)) {
            this.loginUserServiceList.putAll(loginUserServiceList);
        }
    }

    /**
     * 获取用户类型
     * @param userType
     */
    public IUserType valueOf(String userType) {
        if (userType==null) return LoginUser.Type.user;

        Optional<IUserType> optionUserType = loginUserServiceList.keySet().stream().filter(type -> userType.equals(type.name())).findFirst();
        Assert.isTrue(optionUserType.isPresent(), "不能识别的用户类型："+userType);

        return optionUserType.get();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        loginUserServiceList.put(LoginUser.Type.admin, adminService);
//        loginUserServiceList.put(LoginUser.Type.user, userService);
    }

    /**
     * 获取用户类型对应的 登录服务
     * @param userType
     */
    public IUserLoginService getLoginUserService(IUserType userType) {
        return loginUserServiceList.get(userType);
    }
}