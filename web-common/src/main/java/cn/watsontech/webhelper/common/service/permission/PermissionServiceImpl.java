package cn.watsontech.webhelper.common.service.permission;

import cn.watsontech.webhelper.common.entity.Permission;
import cn.watsontech.webhelper.common.service.mapper.permission.PermissionMapper;
import cn.watsontech.webhelper.mybatis.intf.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* Created by Watson Song on 2020/03/05.
*/
@Service
@Transactional
public class PermissionServiceImpl extends BaseService<Permission, Long> implements PermissionService {

    @Autowired
    public PermissionServiceImpl(PermissionMapper mapper){
        super(mapper);
    }

}