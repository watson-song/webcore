package cn.watsontech.webhelper.common.form;//

import cn.watsontech.webhelper.common.entity.Role;
import cn.watsontech.webhelper.mybatis.form.CreateForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("角色信息表单")
public class CreateRoleForm extends CreateForm<Role> {

    @ApiModelProperty("label")
    private String label;

    @ApiModelProperty("name")
    private String name;

    @ApiModelProperty("tag")
    private String tag;

    @ApiModelProperty(value = "状态：0禁用，1可以", hidden = true)
    private Integer status;

    @ApiModelProperty("账号类型：0超级管理员，1管理员，2运营，支持更多自定义；type是归属类型。type=0是超级管理员可以看见的，=1是集团管理员可以看见的，=2是商场管理员可见；")
    private Integer type;

    @ApiModelProperty(value = "0系统自带，1用户创建的；builtinType是 是否是后期建的，=0是系统自带不可以删除的，=1是后期建的可以删除", hidden = true)
    private Boolean builtinType;

    @ApiModelProperty(value = "权限ID集合")
    private List<Long> permissions;

    @ApiModelProperty("是否已启用")
    private Boolean enabled;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getBuiltinType() {
        return builtinType;
    }

    public void setBuiltinType(Boolean builtinType) {
        this.builtinType = builtinType;
    }

    public List<Long> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Long> permissions) {
        this.permissions = permissions;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    protected Role newObject() {
        return new Role();
    }
}
