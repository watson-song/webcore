package cn.watsontech.webhelper.web.spring.security.authentication;

import cn.watsontech.webhelper.service.AdminService;
import cn.watsontech.webhelper.web.form.AdminRegisterForm;
import cn.watsontech.webhelper.web.spring.aop.annotation.Access;
import cn.watsontech.webhelper.web.spring.aop.annotation.AccessParam;
import cn.watsontech.webhelper.web.spring.security.IUserLoginService;
import cn.watsontech.webhelper.web.spring.security.IUserType;
import cn.watsontech.webhelper.web.spring.security.LoginUser;
import cn.watsontech.webhelper.web.spring.security.UserTypeFactory;
import cn.watsontech.webhelper.web.spring.security.entity.Admin;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Watson on 2020/02/20.
 */
@Service
@Log4j2
public class AccountService implements UserDetailsService {
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
    JdbcTemplate jdbcTemplate;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserTypeFactory userTypeFactory;

    //默认系统预设登录查询属性
    final String[] defaultLoginSelectProperties = new String[]{"id", "username", "nickName", "gender", "avatarUrl", "mobile", "lastLoginDate", "lastLoginIp", "isEnabled", "expired", "locked", "credentialsExpired", "extraData", "createdTime"};

    @Override
    public LoginUser loadUserByUsername(@AccessParam String userInfo) throws UsernameNotFoundException {
        String[] usernameAndType = splitUsernameAndType(userInfo, userTypeFactory);
        String username = usernameAndType[0];
        IUserType userType = userTypeFactory.valueOf(usernameAndType[1]);

        IUserLoginService service = userTypeFactory.getLoginUserService(userType);
        Assert.notNull(service, "未找到用户登录服务类，用户类型："+userType);

        String[] selectProperties = service.defaultLoginSelectProperties();
        if (selectProperties==null||selectProperties.length==0) {
            selectProperties = defaultLoginSelectProperties;
        }

        String[] withPasswordProperties = ArrayUtils.add(selectProperties, "password");//添加密码字段
        LoginUser loadedUser = loadAccountInfo("username", username, userType, withPasswordProperties, false);

        return loadedUser;
    }

    /**
     * 根据用户名加载授权认证信息
     * @param userInfo 用户名@用户类型  watson@admin, watson@worker, watson@user
     * @必要方法
     **/
    @Access(value = "${access.loginByUsername.description}", save = "${access.loginByUsername.saveToDatabase}"/*不保存数据库*/, level = "${access.loginByUsername.logLevel}")
    public LoginUser loginByUsername(@AccessParam String userInfo, String password, String loginIp) throws UsernameNotFoundException {
        return loginByUsername(userInfo, password, null, loginIp);
    }

    /**
     * 根据用户名加载授权认证信息
     * @param userInfo 用户名@用户类型  watson@admin, watson@worker, watson@user
     * @param selectProperties 登录自定义查询属性，若未定义，则默认使用系统预设值 {"id", "username", "nickName", "gender", "avatarUrl", "mobile", "lastLoginDate", "lastLoginIp", "enabled", "expired", "locked", "credentialsExpired", "extraData"};
     * @必要方法
     **/
    @Access("用户(%s)使用密码登录")
    public LoginUser loginByUsername(@AccessParam String userInfo, String password, String[] selectProperties, String loginIp) throws UsernameNotFoundException {
        String[] usernameAndType = splitUsernameAndType(userInfo, userTypeFactory);
        String username = usernameAndType[0];
        IUserType userType = userTypeFactory.valueOf(usernameAndType[1]);

        IUserLoginService service = userTypeFactory.getLoginUserService(userType);
        Assert.notNull(service, "未找到用户登录服务类，用户类型："+userType);

        if (selectProperties==null||selectProperties.length==0) {
            //若未提供selectProperties，则使用loginUserService提供参数
            selectProperties = service.defaultLoginSelectProperties();
        }
        if (selectProperties==null||selectProperties.length==0) {
            selectProperties = defaultLoginSelectProperties;
        }

        String[] withPasswordProperties = ArrayUtils.add(selectProperties, "password");//添加密码字段
        LoginUser loadedUser = loadAccountInfo("username", username, userType, withPasswordProperties, false);

        preAuthenticationChecks.check(loadedUser);
        Assert.isTrue(passwordEncoder.matches(password, loadedUser.getPassword()), "密码不正确");
        postAuthenticationChecks.check(loadedUser);

        //更新登录时间
        service.updateLastLoginData(loginIp, loadedUser.getId());

        SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(loadedUser));
        return loadedUser;
    }

    protected Authentication createNewAuthentication(LoginUser user) {
        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        return newAuthentication;
    }

    /**
     * 根据用户openId获取用户详情
     * @param openId
     * @throws UsernameNotFoundException
     */
    @Access(value = "${access.loginByOpenId.description}", save = "${access.loginByOpenId.saveToDatabase}"/*不保存数据库*/, level = "${access.loginByOpenId.logLevel}")
    public LoginUser loginByOpenId(@AccessParam String openId) throws UsernameNotFoundException {
        return loginByOpenId(openId, null);
    }

    @Access("小程序用户(%s)自动登录")
    public LoginUser loginByOpenId(@AccessParam String openId, String[] selectProperties) throws UsernameNotFoundException {
        LoginUser.Type userType = LoginUser.Type.user;

        IUserLoginService service = userTypeFactory.getLoginUserService(userType);
        Assert.notNull(service, "未找到用户登录服务类，用户类型："+userType);

        LoginUser loadedUser = loadAccountInfo("openid", openId, userType, selectProperties, false, false);

        if (loadedUser!=null) {
            preAuthenticationChecks.check(loadedUser);
            postAuthenticationChecks.check(loadedUser);
            SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(loadedUser));
        }

        return loadedUser;
    }

    /**
     * 根据token中包含的信息获取用户详情
     * @param userInfo userId@userType, ex. 0@admin
     * @throws UsernameNotFoundException
     */
    @Access(value = "${access.loginByUserId.description}", save = "${access.loginByUserId.saveToDatabase}"/*不保存数据库*/, level = "${access.loginByUserId.logLevel}")
    public LoginUser loginByUserId(@AccessParam String userInfo) throws UsernameNotFoundException {
        return loginByUserId(userInfo, null);
    }

    /**
     * 根据token中包含的信息获取用户详情
     * @param userInfo userId@userType, ex. 0@admin
     * @throws UsernameNotFoundException
     */
    public LoginUser loginByUserId(@AccessParam String userInfo, String[] selectProperties) throws UsernameNotFoundException {
        String[] usernameAndType = splitUsernameAndType(userInfo, userTypeFactory);
        Long accountId = Long.parseLong(usernameAndType[0]);
        IUserType userType = userTypeFactory.valueOf(usernameAndType[1]);

        LoginUser loadedUser = loadAccountInfo("id", accountId, userType, selectProperties, false);

        preAuthenticationChecks.check(loadedUser);
        postAuthenticationChecks.check(loadedUser);

        return loadedUser;
    }

    /**
     * 从数据库加载用户信息
     * @param accountId 账号id
     * @param userType 账号类型，admin/user/worker
     */
    public LoginUser loadLoginAccount(Long accountId, IUserType userType) {
        return loadLoginAccount(accountId, userType, null);
    }
    /**
     * 从数据库加载用户信息
     * @param accountId 账号id
     * @param userType 账号类型，admin/user/worker
     */
    public LoginUser loadLoginAccount(Long accountId, IUserType userType, String[] selectProperties) {
        IUserLoginService service = userTypeFactory.getLoginUserService(userType);
        Assert.notNull(service, "未找到用户登录服务类，用户类型："+userType);

        LoginUser loadedUser = loadAccountInfo("id", accountId, userType, selectProperties, false);

        preAuthenticationChecks.check(loadedUser);
        postAuthenticationChecks.check(loadedUser);

        return loadedUser;
    }

    /**
     * 从数据库加载用户信息
     * @param accountId 账号id
     * @param userType 账号类型，admin/user/worker
     */
    private LoginUser loadLoginAccountInternal(Long accountId, IUserType userType) {
        return loadAccountInfo("id", accountId, userType, new String[]{"id", "username", "nickName", "gender", "avatarUrl", "mobile", "lastLoginDate", "extraData", "createdTime"}, true);
    }

    private LoginUser loadAccountInfo(String conditionKey, Object conditionValue, IUserType userType, String[] selectProperties, boolean checkEnabled) {
        return loadAccountInfo(conditionKey, conditionValue, userType, selectProperties, checkEnabled, true);
    }

    private LoginUser loadAccountInfo(String conditionKey, Object conditionValue, IUserType userType, String[] selectProperties, boolean checkEnabled, boolean notFoundCheck) {
        LoginUser loadedUser = null;
        if (conditionKey==null||conditionValue==null) {
            return null;
        }

        IUserLoginService service = userTypeFactory.getLoginUserService(userType);
        Assert.notNull(service, "未找到用户登录服务类，用户类型："+userType);

        if (selectProperties==null||selectProperties.length==0) {
            //若未提供selectProperties，则使用loginUserService提供参数
            selectProperties = service.defaultLoginSelectProperties();
        }
        if (selectProperties==null||selectProperties.length==0) {
            selectProperties = defaultLoginSelectProperties;
        }

        loadedUser = service.loadUserByUserIdentity(conditionKey, conditionValue, selectProperties, checkEnabled);

        if (notFoundCheck) {
            if (loadedUser==null) {
//                throw new UsernameNotFoundException("数据库中未找到用户("+conditionValue+"@"+userType+")");
                throw new UsernameNotFoundException("用户名未找到("+conditionValue+")");
            }
        }

        return loadedUser;
    }

    public static String[] splitUsernameAndType(String usernameAndType, UserTypeFactory userTypeFactory) {
        int atIndex = usernameAndType.lastIndexOf("@");
        IUserType userType = LoginUser.Type.user;

        String username = usernameAndType;
        if(atIndex>0) {
            String userTypeString = usernameAndType.substring(atIndex+1);
            try {
                userType = userTypeFactory.valueOf(userTypeString);
                //如果 @后为admin/user/worker (userType)则截取用户名，否则用户名为全称
                username = usernameAndType.substring(0, atIndex);
            }catch (Exception e) {
                //忽略异常
            }
        }
        return new String[]{username, userType.name()};
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
