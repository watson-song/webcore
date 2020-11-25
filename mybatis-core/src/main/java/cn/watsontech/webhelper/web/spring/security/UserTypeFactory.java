package cn.watsontech.webhelper.web.spring.security;

import cn.watsontech.webhelper.service.AdminService;
import cn.watsontech.webhelper.service.UserService;
import cn.watsontech.webhelper.web.spring.util.Assert;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 用户类型工厂
 * Created by Watson on 2020/8/12.
 */
public class UserTypeFactory implements InitializingBean {

    @Autowired
    UserService userService;
    @Autowired
    AdminService adminService;

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
        if (StringUtils.isEmpty(userType)) return LoginUser.Type.user;

        Optional<IUserType> optionUserType = loginUserServiceList.keySet().stream().filter(type -> userType.equals(type.name())).findFirst();
        Assert.isTrue(optionUserType.isPresent(), "不能识别的用户类型："+userType);

        return optionUserType.get();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        loginUserServiceList.put(LoginUser.Type.admin, adminService);
        loginUserServiceList.put(LoginUser.Type.user, userService);
    }

    /**
     * 获取用户类型对应的 登录服务
     * @param userType
     */
    public IUserLoginService getLoginUserService(IUserType userType) {
        return loginUserServiceList.get(userType);
    }
}