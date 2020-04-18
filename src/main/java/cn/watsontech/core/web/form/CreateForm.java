package cn.watsontech.core.web.form;

import cn.watsontech.core.mybatis.CreatedEntity;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;

/**
 * 实体新建form基类
 * Created by Watson on 2020/3/3.
 */
public abstract class CreateForm<T extends CreatedEntity> {

    /**
     * 获取对象类
     */
    protected abstract T newObject();

    /**
     * 根据objectclass生成实体
     */
    @ApiModelProperty(hidden = true)
    public T getObject() {
        T object = newObject();
        BeanUtils.copyProperties(this, object);

        return object;
    }

    /**
     * 根据objectclass生成实体
     */
    public T getObject(long createdBy, String createdByName) {
        T object = getObject();
        object.setCreatedBy(createdBy);
        object.setCreatedByName(createdByName);
        return object;
    }
}
