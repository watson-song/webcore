package cn.watsontech.webhelper.service.manually;

import cn.watsontech.webhelper.mybatis.param.PageParams;
import cn.watsontech.webhelper.service.mapper.manually.AdminManuallyMapper;
import cn.watsontech.webhelper.vo.AdminListVo;
import cn.watsontech.webhelper.web.spring.security.entity.Admin;
import com.github.pagehelper.PageRowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Watson on 2020/2/26.
 */
@Service
public class AdminManualService {

    @Autowired
    AdminManuallyMapper manuallyMapper;

    /**
     * 获取管理员信息
     * @param id 管理员id
     */
    public Admin getAdminInfo(long id) {
        return manuallyMapper.selectAdminInfoById(id);
    }

    /**
     * 获取管理员信息列表
     * @param keywords 仅搜索用户名username
     */
    public List<AdminListVo> listAdminInfos(String keywords, PageParams pageParams) {
        PageRowBounds rowBounds = new PageRowBounds(pageParams.getOffset()!=null?pageParams.getOffset():0, pageParams.getLimit()!=null?pageParams.getLimit():20);
        rowBounds.setCount(true);
        return manuallyMapper.listAdminInfos(keywords!=null?("%"+keywords+"%"):keywords, rowBounds);
    }

    /**
     * 获取所有当前管理员权限
     */
    public List<Map<String, Object>> getAllPermissions(long adminId) {
        return manuallyMapper.selectAllPermissionsByAdminId(adminId);
    }

    /**
     * 获取所有当前管理员角色
     */
    public List<Map<String, Object>> getAllRoles(long adminId) {
        return manuallyMapper.selectAllRolesByAdminId(adminId);
    }

    public int updateLastLoginData(String ip, long userId) {
        return manuallyMapper.updateLastLoginDate(userId, ip);
    }

    public int countUnreadMessages(long userId) {
        return manuallyMapper.countUnreadMessage(userId);
    }
}
