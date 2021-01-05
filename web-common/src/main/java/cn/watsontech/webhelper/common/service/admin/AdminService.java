package cn.watsontech.webhelper.common.service.admin;


import cn.watsontech.webhelper.common.entity.Admin;
import cn.watsontech.webhelper.common.security.IUserLoginService;
import cn.watsontech.webhelper.common.vo.AdminListVo;
import cn.watsontech.webhelper.mybatis.intf.Service;
import cn.watsontech.webhelper.mybatis.param.PageParams;
import com.github.pagehelper.PageRowBounds;

import java.util.List;

/**
* Created by Watson Song on 2020/03/06.
*/
public interface AdminService extends Service<Admin, Long>, IUserLoginService<Long> {

    /**
     * 获取管理员信息
     * @param id 管理员id
     */
    Admin getAdminInfo(long id);

    /**
     * 获取管理员信息列表
     * @param keywords 仅搜索用户名username
     */
    List<AdminListVo> listAdminInfos(String keywords, PageParams pageParams);
}