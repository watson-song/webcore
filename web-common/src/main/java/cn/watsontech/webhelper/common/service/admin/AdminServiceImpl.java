package cn.watsontech.webhelper.common.service.admin;

import cn.watsontech.webhelper.common.entity.Admin;
import cn.watsontech.webhelper.common.security.LoginUser;
import cn.watsontech.webhelper.common.service.mapper.admin.AdminMapper;
import cn.watsontech.webhelper.common.service.mapper.admin.manually.AdminManuallyMapper;
import cn.watsontech.webhelper.common.vo.AdminListVo;
import cn.watsontech.webhelper.mybatis.intf.BaseService;
import cn.watsontech.webhelper.mybatis.param.PageParams;
import com.github.pagehelper.PageRowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;


/**
* Created by Watson Song on 2020/03/06.
*/
@Service
@Transactional
public class AdminServiceImpl extends BaseService<Admin, Long> implements AdminService {

    AdminManuallyMapper manuallyMapper;

    @Autowired
    public AdminServiceImpl(AdminMapper mapper, AdminManuallyMapper manuallyMapper){
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
        Condition condition = new Condition(Admin.class);
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
    public List<Map<String, Object>> loadUserRoles(Long adminId) {
        return manuallyMapper.selectAllRolesByAdminId(adminId);
    }

    @Override
    public List<Map<String, Object>> loadUserPermissions(Long adminId) {
        return manuallyMapper.selectAllPermissionsByAdminId(adminId);
    }

    @Override
    public int updateLastLoginData(String loginIp, Long userId) {
        return manuallyMapper.updateLastLoginDate(userId, loginIp);
    }

    /**
     * 获取管理员信息
     * @param id 管理员id
     */
    @Override
    public Admin getAdminInfo(long id) {
        return manuallyMapper.selectAdminInfoById(id);
    }

    /**
     * 获取管理员信息列表
     * @param keywords 仅搜索用户名username
     */
    @Override
    public List<AdminListVo> listAdminInfos(String keywords, PageParams pageParams) {
        PageRowBounds rowBounds = new PageRowBounds(pageParams.getOffset()!=null?pageParams.getOffset():0, pageParams.getLimit()!=null?pageParams.getLimit():20);
        rowBounds.setCount(true);
        return manuallyMapper.listAdminInfos(keywords!=null?("%"+keywords+"%"):keywords, rowBounds);
    }

    @Override
    public String[] defaultLoginSelectProperties() {
        return new String[]{"id", "username", "password", "nickName", "gender", "email", "avatarUrl", "mobile", "lastLoginDate", "lastLoginIp", "isEnabled", "expired", "locked", "credentialsExpired", "extraData", "type", "department", "title", "version"};
    }
}