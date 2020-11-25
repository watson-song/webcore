//package cn.watsontech.core.web.spring.security.authentication;
//
//import cn.watsontech.webhelper.service.AdminService;
//import cn.watsontech.webhelper.web.spring.security.LoginUser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.webhelper.JdbcTemplate;
//import org.springframework.security.webhelper.userdetails.UserDetailsService;
//import org.springframework.security.webhelper.userdetails.UsernameNotFoundException;
//
///**
// * Created by Watson on 31/01/2018.
// */
//public class AccountAuthenticationService implements UserDetailsService {
//
//    @Autowired
//    AccountService accountService;
//
//    @Autowired
//    AdminService adminService;
//
//    @Autowired
//    JdbcTemplate jdbcTemplate;
//
//    /**
//     * 根据用户名加载授权认证信息
//     * @param userInfo 用户名@用户类型  watson@admin, watson@worker, watson@user
//     * @必要方法
//     **/
//    @Override
//    public LoginUser loadUserByUsername(String userInfo) throws UsernameNotFoundException {
//        LoginUser loadedUser = accountService.loginByUserId(userInfo);
//
////        String[] usernameAndType = AccountService.splitUsernameAndType(userInfo);
////        String username = usernameAndType[0];
////        LoginUser.Type userType = LoginUser.Type.valueOf(usernameAndType[1]);
////
////        Condition condition = AccountService.getUserCondition(userType);
////        Service<? extends LoginUser, Long> service = getUserService(userType);
////        condition.createCriteria().andEqualTo("username", username).andEqualTo("enabled", true).andEqualTo("locked", false);
////        LoginUser loadedUser = service.selectFirstByCondition(condition);
////
////        if (loadedUser==null) {
////            throw new UsernameNotFoundException("数据库中未找到用户("+userInfo+")");
////        }
////
////        //加载管理员角色/权限
////        if (userType== LoginUser.Type.admin) {
////            loadedUser.setRoles(jdbcTemplate.queryForList(String.format("select b.name, b.label as title from ref_admin_role a left join tb_role b on a.role_id=b.id where a.admin_id=%s and b.enabled = 1", loadedUser.getId())));
////        }
//
//        return loadedUser;
//    }
//
//}
