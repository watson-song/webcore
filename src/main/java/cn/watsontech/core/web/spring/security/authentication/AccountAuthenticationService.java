package cn.watsontech.core.web.spring.security.authentication;

import cn.watsontech.core.service.AdminService;
import cn.watsontech.core.service.UserService;
import cn.watsontech.core.web.spring.security.IUserLoginService;
import cn.watsontech.core.web.spring.security.IUserType;
import cn.watsontech.core.web.spring.security.LoginUser;
import cn.watsontech.core.web.spring.security.UserTypeFactory;
import cn.watsontech.core.web.spring.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Watson on 31/01/2018.
 */
public class AccountAuthenticationService implements UserDetailsService {

    @Autowired
    UserTypeFactory userTypeFactory;

    @Autowired
    JdbcTemplate jdbcTemplate;

    //登录用户服务
    Map<IUserType, IUserLoginService> loginUserServiceList = new HashMap<>();

    public AccountAuthenticationService(Map<IUserType, IUserLoginService> loginUserServiceList) {
        if (!CollectionUtils.isEmpty(loginUserServiceList)) {
            this.loginUserServiceList.putAll(loginUserServiceList);
        }
    }

    @Autowired
    public void afterPropertiesSet(UserService userService, AdminService adminService) {
        loginUserServiceList.put(LoginUser.Type.admin, adminService);
        loginUserServiceList.put(LoginUser.Type.user, userService);
    }

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

        IUserLoginService service = loginUserServiceList.get(userType);
        Assert.notNull(service, "不能识别的用户类型，"+userType);

        LoginUser loadedUser = service.loadUserByUsername(username);
//        Service<? extends LoginUser, Long> service = getUserService(userType);
//        Condition condition = AccountService.getUserCondition(userType);
//        condition.createCriteria().andEqualTo("username", username).andEqualTo("enabled", true).andEqualTo("locked", false);
//        LoginUser loadedUser = service.selectFirstByCondition(condition);

        if (loadedUser==null) {
            throw new UsernameNotFoundException("数据库中未找到用户("+userInfo+")");
        }

        //加载管理员角色/权限
//        if (userType == LoginUser.Type.admin) {
//            loadedUser.setRoles(jdbcTemplate.queryForList(String.format("select b.name, b.label as title from ref_admin_role a left join tb_role b on a.role_id=b.id where a.admin_id=%s and b.enabled = 1", loadedUser.getId())));
//        }

        return loadedUser;
    }
}
