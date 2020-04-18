package com.watsontech.core.mybatis.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;

import java.util.List;

/**
 * Copyright to watsontech
 * Created by Watson on 2019/12/27.
 */
public interface BatchInsertMapper<T> {

    /**
     * 批量插入，忽略插入异常；
     * 支持批量插入的数据库可以使用，例如MySQL,H2等，另外该接口限制实体包含`id`属性并且必须为自增列
     *
     * @param recordList
     * @return
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @InsertProvider(type = BatchInsertProvider.class, method = "dynamicSQL")
    int insertListIgnoreConflict(List<T> recordList);


    @Options(useGeneratedKeys = true, keyProperty = "id")
    @InsertProvider(type = BatchInsertProvider.class, method = "dynamicSQL")
    int insertSelectiveListIgnoreConflict(BatchInsertModel<T> batchInsertModel);
}