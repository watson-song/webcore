package cn.watsontech.webhelper.common.vo;

import cn.watsontech.webhelper.common.entity.Permission;
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

    public PermissionVo(Permission permission, List<PrinciplePermissionVo> permissions) {
        super();

        BeanUtils.copyProperties(permission, this);
        this.children = permissions;
    }

    @ApiModelProperty(value = "子权限列表")
    private List<PrinciplePermissionVo> children;

    public List<PrinciplePermissionVo> getChildren() {
        return children;
    }

    public void setChildren(List<PrinciplePermissionVo> children) {
        this.children = children;
    }
}
