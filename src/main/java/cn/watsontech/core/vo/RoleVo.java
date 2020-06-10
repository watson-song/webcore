package cn.watsontech.core.vo;

import cn.watsontech.core.web.spring.security.entity.Permission;
import cn.watsontech.core.web.spring.security.entity.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by Watson on 2020/6/8.
 */
@Data
public class RoleVo extends Role {
    public RoleVo() {
        super();
    }

    public RoleVo(Role role, List<Permission> permissions) {
        super();

        BeanUtils.copyProperties(role, this);
        this.permissions = permissions;
    }

    @ApiModelProperty(value = "权限列表")
    private List<Permission> permissions;

}
