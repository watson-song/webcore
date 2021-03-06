package cn.watsontech.webhelper.common.vo;

import cn.watsontech.webhelper.common.entity.Role;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by Watson on 2020/6/8.
 */
public class RoleVo extends Role {
    public RoleVo() {
        super();
    }

    public RoleVo(Role role, List<PermissionVo> permissions) {
        super();

        BeanUtils.copyProperties(role, this);
        this.permissions = permissions;
    }

    @ApiModelProperty(value = "权限列表")
    private List<PermissionVo> permissions;

    public List<PermissionVo> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionVo> permissions) {
        this.permissions = permissions;
    }
}
