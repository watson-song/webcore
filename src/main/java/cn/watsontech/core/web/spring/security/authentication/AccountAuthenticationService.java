package cn.watsontech.core.web.spring.security.authentication;

import cn.watsontech.core.web.spring.security.IUserLoginService;
import cn.watsontech.core.web.spring.security.IUserType;
import cn.watsontech.core.web.spring.security.LoginUser;
import cn.watsontech.core.web.spring.security.UserTypeFactory;
import cn.watsontech.core.web.spring.util.Assert;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by Watson on 31/01/2018.
 */
@Log4j2
public class AccountAuthenticationService implements UserDetailsService {

    @Autowired
    UserTypeFactory userTypeFactory;

    /**
     * 根据用户名加载授权认证信息
     * @param userInfo 用户名@用户类型  watson@admin, watson@worker, watson@user
     * @必要方法
     **/
    @Override
    public LoginUser loadUserByUsername(String userInfo) throws UsernameNotFoundException {
        Assert.notNull(userTypeFactory, "用户类型工厂方法未配置");

        String[] usernameAndType = AccountService.splitUsernameAndType(userInfo, userTypeFactory);
        String username = usernameAndType[0];

        IUserType userType = userTypeFactory.valueOf(usernameAndType[1]);

        IUserLoginService service = userTypeFactory.getLoginUserService(userType);
        Assert.notNull(service, "不能识别的用户类型，"+userType);

        LoginUser loadedUser = service.loadUserByUsername(username);

        if (loadedUser==null) {
            throw new UsernameNotFoundException("数据库中未找到用户("+userInfo+")");
        }

        return loadedUser;
    }
}
