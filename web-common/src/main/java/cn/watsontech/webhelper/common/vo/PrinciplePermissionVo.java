package cn.watsontech.webhelper.common.vo;

import cn.watsontech.webhelper.common.entity.Permission;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import java.util.*;

/**
 * Created by Watson on 2020/6/8.
 */
public class PrinciplePermissionVo {

    @ApiModelProperty(value="id")
    @JsonIgnore
    @Transient
    @JSONField(serialize = false)
    private Long id;

    @ApiModelProperty(value="系统标识")
    private String name;

    @ApiModelProperty(value="名称")
    @Transient
    @JsonIgnore
    @JSONField(serialize = false)
    private String label;

    @Transient
    @JsonIgnore
    @JSONField(serialize = false)
    private String parentId;

    @Transient
    @JsonIgnore
    @JSONField(serialize = false)
    private Long adminId;

    @ApiModelProperty(value = "子权限列表")
    private Set<PrinciplePermissionVo> children;

    public PrinciplePermissionVo() {}
    public PrinciplePermissionVo(Long id, String name, String label) {
        this(id, name, label, null);
    }
    public PrinciplePermissionVo(Long id, String name, String label, Set<PrinciplePermissionVo> children) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.children = children;
    }
    public PrinciplePermissionVo(Permission permission, Collection<PrinciplePermissionVo> permissions) {
        this(permission, new HashSet<>(permissions));
    }
    public PrinciplePermissionVo(Permission permission, Set<PrinciplePermissionVo> permissions) {
        if (permission!=null) {
            this.setId(permission.getId());
            this.setName(permission.getName());
            this.setLabel(permission.getLabel());
        }

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

    public Set<PrinciplePermissionVo> getChildren() {
        return children;
    }

    public void setChildren(Set<PrinciplePermissionVo> children) {
        this.children = children;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==null) return false;

        if (obj instanceof PrinciplePermissionVo) {
            PrinciplePermissionVo objPrinciplePermission = ((PrinciplePermissionVo) obj);
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

            //id和name都相等，最后检查children是否全匹配
            Set<PrinciplePermissionVo> thisChildren = getChildren();
            Set<PrinciplePermissionVo> objChildren = objPrinciplePermission.getChildren();
            int sizeOfChildren = sizeOfCollection(thisChildren);
            if (sizeOfChildren!=sizeOfCollection(objChildren)) return false;

            if (sizeOfChildren>0) {
                Iterator<PrinciplePermissionVo> objChildrenIterator = objChildren.iterator();
                Iterator<PrinciplePermissionVo> thisChildrenIterator = thisChildren.iterator();
                while (thisChildrenIterator.hasNext()) {
                    PrinciplePermissionVo thisChild = thisChildrenIterator.next();
                    PrinciplePermissionVo objChild = objChildrenIterator.next();

                    if (thisChild!=null) {
                        if (!thisChild.equals(objChild)) {
                            return false;
                        }
                    }
                }
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

        if (getChildren()!=null) {
            Iterator<PrinciplePermissionVo> iterator = getChildren().iterator();
            StringBuilder childHashCodeValueBuilder = new StringBuilder();
            while (iterator.hasNext()) {
                PrinciplePermissionVo principlePermissionVo = iterator.next();
                childHashCodeValueBuilder.append(principlePermissionVo.toString()).append("|");
            }

            int childHashCodeValueLength = childHashCodeValueBuilder.length();
            if (childHashCodeValueLength>0) {
                childHashCodeValueBuilder.deleteCharAt(childHashCodeValueLength-1);
                hashCodeValue += ">" + childHashCodeValueBuilder.toString();
            }
        }

        return hashCodeValue;
    }

    private int sizeOfCollection(Collection objs) {
        if (CollectionUtils.isEmpty(objs)) return 0;
        return objs.size();
    }

    public static void main(String[] args) {
        PrinciplePermissionVo a = new PrinciplePermissionVo(1l, "a", "大人");
        PrinciplePermissionVo b = new PrinciplePermissionVo(1l, "a", "大人2");
        PrinciplePermissionVo c = new PrinciplePermissionVo(19l, "a19", "大人19");
        PrinciplePermissionVo d = new PrinciplePermissionVo(null, "a19", "大人20");
        System.out.println("id和name相同，label不同："+a.equals(b));

        a = new PrinciplePermissionVo(1l, "a", "大人");
        b = new PrinciplePermissionVo(1l, "a1", "大人2");
        System.out.println("id相同，name不相同："+a.equals(b));

        a = new PrinciplePermissionVo(1l, "a", "大人");
        b = new PrinciplePermissionVo(2l, "a", "大人2");
        System.out.println("id不通，name相同："+a.equals(b));

        a = new PrinciplePermissionVo(1l, "a", "大人");
        b = new PrinciplePermissionVo(2l, "a1", "大人2");
        System.out.println("id和name都不相同："+a.equals(b));

        a = new PrinciplePermissionVo(null, "a", "大人");
        b = new PrinciplePermissionVo(2l, "a", "大人2");
        System.out.println("id有为空，name都相同："+a.equals(b));

        a = new PrinciplePermissionVo(null, null, "大人");
        b = new PrinciplePermissionVo(2l, "a", "大人2");
        System.out.println("id有为空，name有为空："+a.equals(b));

        a = new PrinciplePermissionVo(null, null, "大人");
        b = new PrinciplePermissionVo(2l, null, "大人2");
        System.out.println("id有为空，name均为空："+a.equals(b));

        a = new PrinciplePermissionVo(1l, "a", "大人", null);
        b = new PrinciplePermissionVo(1l, "a1", "大人2", new HashSet<>(Arrays.asList(a)));
        System.out.println("id相同，children不同："+a.equals(b));

        a = new PrinciplePermissionVo(1l, "a", "大人", new HashSet<>(Arrays.asList(c)));
        b = new PrinciplePermissionVo(1l, "a1", "大人2", new HashSet<>(Arrays.asList(a)));
        System.out.println("id相同，children不同："+a.equals(b));

        a = new PrinciplePermissionVo(1l, "a", "大人", new HashSet<>(Arrays.asList(c)));
        b = new PrinciplePermissionVo(1l, "a1", "大人2", new HashSet<>(Arrays.asList(d)));
        System.out.println("id相同，children相同："+a.equals(b));

        a = new PrinciplePermissionVo(1l, "a", "大人");
        c = new PrinciplePermissionVo(19l, "a19", "大人19");
        b = new PrinciplePermissionVo(1l, "a", "大人2", new HashSet(Arrays.asList(c)));
        d = new PrinciplePermissionVo(null, "a19", "大人20");

        List<PrinciplePermissionVo> permissionVos = Arrays.asList(a, b, c, d);
        Set<PrinciplePermissionVo> permissionVoSet = new HashSet<>(permissionVos);
        Iterator<PrinciplePermissionVo> perIterator = permissionVoSet.iterator();
        int i = 0;
        while(perIterator.hasNext()) {
            PrinciplePermissionVo permissionVo = perIterator.next();
            System.out.println((i++)+":"+permissionVo.toString());
        }
    }

    /**
     * 检查是否有权限，注意：先检查当前权限，如未找到会继续检查所有children
     * @param permission 目标权限
     * @return 如检查到了某权限则返回true，否则返回false
     */
    public boolean check(PrinciplePermissionVo permission) {
        boolean isValid = false;
        if (permission!=null) {

            //先比较id，然后name
            if (getId()!=null&&permission.getId()!=null) {
                isValid = getId().equals(permission.getId());
            }else if (getName()!=null) {
                isValid = getName().equalsIgnoreCase(permission.getName());
            }

            //name或id不相等，继续比较children
            if (!isValid) {

                //若还有children则遍历所有child查看是否包含当前permission
                if (!CollectionUtils.isEmpty(getChildren())) {
                    isValid = getChildren().stream().anyMatch(child -> child.check(permission));
                }
            }
        }

        return isValid;
    }
}