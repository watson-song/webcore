package cn.watsontech.core.web.spring.security.authentication;

import cn.watsontech.core.service.AdminService;
import cn.watsontech.core.service.UserService;
import cn.watsontech.core.service.manually.AdminManualService;
import cn.watsontech.core.service.manually.MessageManualService;
import cn.watsontech.core.web.form.AdminRegisterForm;
import cn.watsontech.core.web.spring.aop.annotation.Access;
import cn.watsontech.core.web.spring.aop.annotation.AccessParam;
import cn.watsontech.core.web.spring.security.LoginUser;
import cn.watsontech.core.web.spring.security.entity.Admin;
import cn.watsontech.core.web.spring.security.entity.User;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Watson on 2020/02/20.
 */
@Service
@Log4j2
public class AccountService {
    UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();
    UserDetailsChecker postAuthenticationChecks = new DefaultPostAuthenticationChecks();

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                log.debug("User account is locked, account = {}", user);

                throw new LockedException("该账号已锁定！");
            }

            if (!user.isEnabled()) {
                log.debug("User account is disabled, account = {}", user);

                throw new DisabledException("该账号已禁用！");
            }

            if (!user.isAccountNonExpired()) {
                log.debug("User account is expired, account = {}", user);

                throw new AccountExpiredException("该账号已过期！");
            }
        }
    }

    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
        public void check(UserDetails user) {
            if (!user.isCredentialsNonExpired()) {
                log.debug("User account credentials have expired, account = {}", user);

                throw new CredentialsExpiredException("该账户密码已过期！");
            }
        }
    }

    @Autowired
    AdminService adminService;
    @Autowired
    UserService userService;
    @Autowired
    MessageManualService messageManualService;
    @Autowired
    AdminManualService adminManualService;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 根据用户名加载授权认证信息
     * @param userInfo 用户名@用户类型  watson@admin, watson@worker, watson@user
     * @必要方法
     **/
    @Access("用户(%s)使用密码登录(ip地址:%s)")
    public LoginUser loginByUsername(@AccessParam String userInfo, String password, @AccessParam String loginIp) throws UsernameNotFoundException {
        String[] usernameAndType = splitUsernameAndType(userInfo);
        String username = usernameAndType[0];
        LoginUser.Type userType = LoginUser.Type.valueOf(usernameAndType[1]);

        LoginUser loadedUser = loadAccountInfo("username", username, userType, new String[]{"id", "username", "password", "nickName", "gender", "email", "avatarUrl", "mobile", "lastLoginDate", "lastLoginIp", "enabled", "expired", "locked", "credentialsExpired", "extraData"}, false);

        //加载管理员角色/权限
        loadRolesAndPermissions(loadedUser);

        preAuthenticationChecks.check(loadedUser);
        Assert.isTrue(passwordEncoder.matches(password, loadedUser.getPassword()), "密码不正确");
        postAuthenticationChecks.check(loadedUser);

        //更新登录时间
        switch (userType) {
            case admin:
                jdbcTemplate.update("update tb_admin set last_login_date=?, last_login_ip=?, login_ip=?, login_date=? where id = ?", ((Admin)loadedUser).getLastLoginDate(), ((Admin)loadedUser).getLastLoginIp(), loginIp, DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"), loadedUser.getId());
                break;
            case user:
                jdbcTemplate.update("update tb_user set last_login_date=?, last_login_ip=?, login_ip=?, login_date=? where id = ?", ((User)loadedUser).getLastLoginDate(), ((User)loadedUser).getLastLoginIp(), loginIp, DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"), loadedUser.getId());
                break;
        }

        return loadedUser;
    }

    /**
     * 加载用户的权限和角色
     * @param loadedUser
     */
    private void loadRolesAndPermissions(LoginUser loadedUser) {
        //加载管理员角色/权限
        if (loadedUser!=null) {
            if (loadedUser.getUserType() == LoginUser.Type.admin) {
                loadedUser.setRoles(adminManualService.getAllRoles(loadedUser.getId()));
                loadedUser.setPermissions(adminManualService.getAllPermissions(loadedUser.getId()));
            }
        }
    }

    /**
     * 根据用户openId获取用户详情
     * @param openId
     * @throws UsernameNotFoundException
     */
    @Access("小程序用户(%s)自动登录")
    public LoginUser loginByOpenId(@AccessParam String openId) throws UsernameNotFoundException {
        LoginUser.Type userType = LoginUser.Type.user;

        LoginUser loadedUser = loadAccountInfo("openid", openId, userType, new String[]{"id", "username", "nickName", "gender", "avatarUrl", "mobile", "lastLoginDate", "enabled", "expired", "locked", "credentialsExpired", "createdTime"}, false, false);

        if (loadedUser!=null) {
            preAuthenticationChecks.check(loadedUser);
            postAuthenticationChecks.check(loadedUser);
        }

        return loadedUser;
    }

    /**
     * 根据token中包含的信息获取用户详情
     * @param userInfo userId@userType, ex. 0@admin
     * @throws UsernameNotFoundException
     */
    @Access("用户(%s)使用令牌登录")
    public LoginUser loginByUserId(@AccessParam String userInfo) throws UsernameNotFoundException {
        String[] usernameAndType = splitUsernameAndType(userInfo);
        Long accountId = null;
        try {
            accountId = Long.parseLong(usernameAndType[0]);
        }catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        LoginUser.Type userType = LoginUser.Type.valueOf(usernameAndType[1]);

        LoginUser loadedUser = loadAccountInfo("id", accountId, userType, new String[]{"id", "username", "nickName", "gender", "avatarUrl", "mobile", "lastLoginDate", "extraData", "createdTime", "enabled", "expired", "locked", "credentialsExpired"}, false);

        preAuthenticationChecks.check(loadedUser);
        postAuthenticationChecks.check(loadedUser);

        //加载管理员角色/权限
        loadRolesAndPermissions(loadedUser);

        return loadedUser;
    }

    /**
     * 从数据库加载用户信息
     * @param accountId 账号id
     * @param userType 账号类型，admin/user/worker
     */
    public LoginUser loadLoginAccount(Long accountId, LoginUser.Type userType) {
        LoginUser loadedUser = loadAccountInfo("id", accountId, userType, new String[]{"id", "username", "nickName", "gender", "avatarUrl", "mobile", "lastLoginDate", "extraData", "createdTime", "enabled", "expired", "locked", "credentialsExpired"}, false);

        preAuthenticationChecks.check(loadedUser);
        postAuthenticationChecks.check(loadedUser);

        //加载管理员角色/权限
        loadRolesAndPermissions(loadedUser);

        return loadedUser;
    }

    /**
     * 从数据库加载用户信息
     * @param accountId 账号id
     * @param userType 账号类型，admin/user/worker
     */
    private LoginUser loadLoginAccountInternal(Long accountId, LoginUser.Type userType) {
        return loadAccountInfo("id", accountId, userType, new String[]{"id", "username", "nickName", "gender", "avatarUrl", "mobile", "lastLoginDate", "extraData", "createdTime"}, true);
    }

    private LoginUser loadAccountInfo(String conditionKey, Object conditionValue, LoginUser.Type userType, String[] selectProperties, boolean checkEnabled) {
        return loadAccountInfo(conditionKey, conditionValue, userType, selectProperties, checkEnabled, true);
    }

    private LoginUser loadAccountInfo(String conditionKey, Object conditionValue, LoginUser.Type userType, String[] selectProperties, boolean checkEnabled, boolean notFoundCheck) {
        LoginUser loadedUser = null;
        if (conditionKey!=null&&conditionValue!=null) {

            Condition condition = getUserCondition(userType);
            cn.watsontech.core.service.intf.Service<? extends LoginUser, Long> service = getUserService(userType);
            condition.selectProperties(selectProperties);
            switch (userType) {
                case admin:
                    condition.selectProperties("type", "department", "title");
                    break;
                case user:
                    condition.selectProperties("openid", "email", "logged");
                    break;
            }

            Example.Criteria criteria = condition.createCriteria().andEqualTo(conditionKey, conditionValue);
            if (checkEnabled) {
                criteria.andEqualTo("enabled", true).andEqualTo("locked", false);
            }
            loadedUser = service.selectFirstByCondition(condition);
        }

        if (loadedUser!=null) {
            loadedUser.setUnreadMessages(messageManualService.countUnreadMessages(userType, loadedUser.getId()));
        }else if (notFoundCheck) {
            throw new UsernameNotFoundException("数据库中未找到用户("+conditionValue+"@"+userType+")");
        }

        return loadedUser;
    }

    public static String[] splitUsernameAndType(String usernameAndType) {
        int atIndex = usernameAndType.lastIndexOf("@");
        LoginUser.Type userType = LoginUser.Type.user;

        String username = usernameAndType;
        if(atIndex>0) {
            String userTypeString = usernameAndType.substring(atIndex+1);
            try {
                userType = LoginUser.Type.valueOf(userTypeString);
                //如果 @后为admin/user/worker (userType)则截取用户名，否则用户名为全称
                username = usernameAndType.substring(0, atIndex);
            }catch (Exception e) {
                //忽略异常
            }
        }
        return new String[]{username, userType.name()};
    }

    private cn.watsontech.core.service.intf.Service<? extends LoginUser, Long> getUserService(LoginUser.Type userType) {
        switch (userType) {
            case admin:
                return adminService;
            default:
                return userService;
        }
    }

    public static Condition getUserCondition(LoginUser.Type userType) {
        switch (userType) {
            case admin:
                return new Condition(Admin.class);
            default:
                return new Condition(User.class);
        }
    }


    /**************************************账号注册服务****************************************************/

    /**
     * 注册worker
     */
    @Transactional
    public Admin registerAdmin(AdminRegisterForm form, LoginUser user) {
        Condition condition = new Condition(Admin.class);
        condition.createCriteria().andEqualTo("username", form.getUsername());
        Admin existAccount = adminService.selectFirstByCondition(condition);
        Assert.isNull(existAccount, "用户名已存在");

        Admin admin = new Admin();
        admin.setType(form.getType());
        admin.setUsername(form.getUsername());
        admin.setEnabled(true);
        admin.setMobile(form.getMobile());
        admin.setGender(form.getGender());
        admin.setNickName(form.getNickName());
        admin.setAvatarUrl(form.getAvatarUrl());
        admin.setEmail(form.getEmail());
        admin.setAddress(form.getAddress());
        admin.setTitle(form.getTitle());
        admin.setDepartment(form.getDepartment());
        admin.setAddress(form.getAddress());
        if (!StringUtils.isEmpty(form.getPassword())) {
            admin.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        admin.setExtraData(form.getExtraData());
        admin.setCreatedBy(user.getId());//默认0 创建
        admin.setCreatedByName(user.getUsername());
        int success = adminService.insertSelective(admin);
        Assert.isTrue(success>0, "添加管理员账号失败，请稍后再试");

        if (!CollectionUtils.isEmpty(form.getRoleIds())) {
            List<Object[]> values = new ArrayList<>();
            for(Long roleId:form.getRoleIds()) {
                values.add(new Object[]{roleId, admin.getId(), user.getId()});
            }
            jdbcTemplate.batchUpdate("INSERT ignore INTO ref_admin_role (role_id, admin_id, created_by) VALUES (?, ?, ?)", values);
        }

        return admin;
    }
}
