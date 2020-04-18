package com.watsontech.core.mybatis;

/**
 * Created by Watson on 2020/3/4.
 */
public interface CreatedEntity<T> {

    /**
     * 获取创建人ID
     *
     * @return created_by - 创建人ID
     */
    Long getCreatedBy();

    /**
     * 设置创建人ID
     *
     * @param createdBy 创建人ID
     */
    T setCreatedBy(Long createdBy);

    /**
     * 获取创建人名称
     *
     * @return created_by_name - 创建人名称
     */
    String getCreatedByName();

    /**
     * 设置创建人名称
     *
     * @param createdByName 创建人名称
     */
    T setCreatedByName(String createdByName);

    /**
     * 获取版本号
     *
     * @return version - 版本号
     */
    Integer getVersion();

    /**
     * 设置版本号
     *
     * @param version 版本号
     */
    T setVersion(Integer version);
}
