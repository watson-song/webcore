package cn.watsontech.webhelper.common.vo;

import cn.watsontech.webhelper.common.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import java.util.*;

/**
 * Created by Watson on 2020/6/8.
 */
public class PrincipleRoleVo {

    @ApiModelProperty(value="id")
    @Transient
    private Long id;

    @ApiModelProperty(value="系统标识")
    private String name;

    @ApiModelProperty(value="名称")
    @JsonIgnore
    private String label;

    public PrincipleRoleVo() {}
    public PrincipleRoleVo(Long id, String name, String label) {
        this.id = id;
        this.name = name;
        this.label = label;
    }
    public PrincipleRoleVo(Role role) {
        if (role!=null) {
            this.id = role.getId();
            this.name = role.getName();
            this.label = role.getLabel();
        }
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

    @Override
    public boolean equals(Object obj) {
        if (obj==null) return false;

        if (obj instanceof PrincipleRoleVo) {
            PrincipleRoleVo objPrinciplePermission = ((PrincipleRoleVo) obj);
            String objName = objPrinciplePermission.getName();
            Long objId = objPrinciplePermission.getId();

            boolean isEquals = false;
            if (objId!=null&&getId()!=null) {
                //优先检查当前ID是否匹配
                if (!objId.equals(getId())) return false;
            }else if (StringUtils.hasLength(objName)&&StringUtils.hasLength(getName())) {
                //其次检查当前name是否匹配
                if (!objName.equalsIgnoreCase(getName())) return false;
            }else {
                //id至少一个为空，或name至少一个为空,都为空都时候 则相等
                return objId==getId()&&objName==getName();
            }

            return true;
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        String hashCodeValue = getId()+":"+getName();

        return hashCodeValue;
    }

    public static void main(String[] args) {
        PrincipleRoleVo a = new PrincipleRoleVo(1l, "a", "大人");
        PrincipleRoleVo b = new PrincipleRoleVo(1l, "a", "大人2");
        PrincipleRoleVo c = new PrincipleRoleVo(19l, "a19", "大人19");
        PrincipleRoleVo d = new PrincipleRoleVo(null, "a19", "大人20");
        System.out.println("id和name相同，label不同："+a.equals(b));

        a = new PrincipleRoleVo(1l, "a", "大人");
        b = new PrincipleRoleVo(1l, "a1", "大人2");
        System.out.println("id相同，name不相同："+a.equals(b));

        a = new PrincipleRoleVo(1l, "a", "大人");
        b = new PrincipleRoleVo(2l, "a", "大人2");
        System.out.println("id不通，name相同："+a.equals(b));

        a = new PrincipleRoleVo(1l, "a", "大人");
        b = new PrincipleRoleVo(2l, "a1", "大人2");
        System.out.println("id和name都不相同："+a.equals(b));

        a = new PrincipleRoleVo(null, "a", "大人");
        b = new PrincipleRoleVo(2l, "a", "大人2");
        System.out.println("id有为空，name都相同："+a.equals(b));

        a = new PrincipleRoleVo(null, null, "大人");
        b = new PrincipleRoleVo(2l, "a", "大人2");
        System.out.println("id有为空，name有为空："+a.equals(b));

        a = new PrincipleRoleVo(null, null, "大人");
        b = new PrincipleRoleVo(2l, null, "大人2");
        System.out.println("id有为空，name均为空："+a.equals(b));

        a = new PrincipleRoleVo(1l, "a", "大人");
        c = new PrincipleRoleVo(19l, "a19", "大人19");
        d = new PrincipleRoleVo(null, "a19", "大人20");

        List<PrincipleRoleVo> permissionVos = Arrays.asList(a, b, c, d);
        Set<PrincipleRoleVo> permissionVoSet = new HashSet<>(permissionVos);
        Iterator<PrincipleRoleVo> perIterator = permissionVoSet.iterator();
        int i = 0;
        while(perIterator.hasNext()) {
            PrincipleRoleVo permissionVo = perIterator.next();
            System.out.println((i++)+":"+permissionVo.toString());
        }
    }

    /**
     * 检查是否有权限，注意：先检查当前权限，如未找到会继续检查所有children
     * @param role 目标角色
     * @return 如检查到了某权限则返回true，否则返回false
     */
    public boolean check(PrincipleRoleVo role) {
        boolean isValid = false;
        if (role!=null) {

            //先比较id，然后name
            if (getId()!=null&&role.getId()!=null) {
                isValid = getId().equals(role.getId());
            }else if (getName()!=null) {
                isValid = getName().equalsIgnoreCase(role.getName());
            }
        }

        return isValid;
    }
}