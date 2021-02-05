package cn.watsontech.webhelper.common.service.user;

import cn.watsontech.webhelper.common.entity.User;
import cn.watsontech.webhelper.common.security.LoginUser;
import cn.watsontech.webhelper.common.service.mapper.user.UserMapper;
import cn.watsontech.webhelper.common.service.mapper.user.manually.UserManuallyMapper;
import cn.watsontech.webhelper.common.vo.PrinciplePermissionVo;
import cn.watsontech.webhelper.mybatis.intf.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Map;
import java.util.Set;


/**
* Created by Watson Song on 2020/03/06.
*/
@Service
@Transactional
public class UserServiceImpl extends BaseService<User, Long> implements UserService {

    UserManuallyMapper manuallyMapper;

    @Autowired
    public UserServiceImpl(UserMapper mapper, UserManuallyMapper manuallyMapper){
        super(mapper);
        this.manuallyMapper = manuallyMapper;
    }

    @Override
    public LoginUser loadUserByUsername(String username) {
        return loadUserByUserIdentity("username", username, defaultLoginSelectProperties(), true);
    }

    @Override
    public LoginUser loadUserByUsername(String username, String[] selectProperties, boolean checkEnabled) {
        return loadUserByUserIdentity("username", username, selectProperties, checkEnabled);
    }

    @Override
    public LoginUser loadUserByUserIdentity(String identity, Object identityValue, String[] selectProperties, boolean checkEnabled) {
        Condition condition = new Condition(User.class);
        condition.selectProperties(selectProperties);
        Example.Criteria criteria = condition.createCriteria().andEqualTo(identity, identityValue);
        if (checkEnabled) {
            criteria.andEqualTo("isEnabled", true).andEqualTo("locked", false);
        }
        LoginUser loginUser = selectFirstByCondition(condition);

        if (loginUser!=null) {
            loginUser.setUnreadMessages(countUnreadMessages(loginUser.getId()));
            loginUser.setRoles(loadUserRoles(loginUser.getId()));
            loginUser.setPermissions(loadUserPermissions(loginUser.getId()));
        }
        return loginUser;
    }

    @Override
    public int countUnreadMessages(Long userId) {
        return manuallyMapper.countUnreadMessage(userId);
    }

    @Override
    public Set<Map<String, Object>> loadUserRoles(Long userId) {
        return null;
    }

    @Override
    public Set<PrinciplePermissionVo> loadUserPermissions(Long userId) {
        return null;
    }

    @Override
    public int updateLastLoginData(String loginIp, Long userId) {
        return manuallyMapper.updateLastLoginDate(userId, loginIp);
    }

    @Value("${loginService.defaultLoginSelectProperties.user:id,username,password,nickName,gender,email,avatarUrl,mobile,lastLoginDate,lastLoginIp,isEnabled,isVip,expired,locked,credentialsExpired,extraData,openid,email,logged,version}")
    private String[] defaultLoginSelectProperties;

    @Override
    public String[] defaultLoginSelectProperties() {
        //new String[]{"id", "username", "password", "nickName", "gender", "email", "avatarUrl", "mobile", "lastLoginDate", "lastLoginIp", "isEnabled", "expired", "locked", "credentialsExpired", "extraData", "openid", "email", "logged", "version"};
        return defaultLoginSelectProperties;
    }
}