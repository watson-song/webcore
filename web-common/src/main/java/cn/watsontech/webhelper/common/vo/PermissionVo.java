package cn.watsontech.webhelper.common.vo;

import cn.watsontech.webhelper.common.entity.Permission;
import cn.watsontech.webhelper.common.entity.Role;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by Watson on 2020/6/8.
 */
public class PermissionVo extends Permission {
    public PermissionVo() {
        super();
    }

    public PermissionVo(Role role, List<Permission> permissions) {
        super();

        BeanUtils.copyProperties(role, this);
        this.children = permissions;
    }

    @ApiModelProperty(value = "子权限列表")
    private List<Permission> children;

}
