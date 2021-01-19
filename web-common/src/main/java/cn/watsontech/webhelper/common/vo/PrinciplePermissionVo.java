package cn.watsontech.webhelper.common.vo;

import cn.watsontech.webhelper.common.entity.Permission;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;

import javax.persistence.Transient;
import java.util.List;

/**
 * Created by Watson on 2020/6/8.
 */
public class PrinciplePermissionVo {

    @ApiModelProperty(value="id")
    @JsonIgnore
    @Transient
    private Long id;

    @ApiModelProperty(value="系统标识")
    private String name;

    @ApiModelProperty(value="名称")
    @Transient
    @JsonIgnore
    private String label;

    @ApiModelProperty(value = "子权限列表")
    private List<PrinciplePermissionVo> children;

    public PrinciplePermissionVo() {}
    public PrinciplePermissionVo(Permission permission, List<PrinciplePermissionVo> permissions) {
        BeanUtils.copyProperties(permission, this);
        this.children = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<PrinciplePermissionVo> getChildren() {
        return children;
    }

    public void setChildren(List<PrinciplePermissionVo> children) {
        this.children = children;
    }
}
