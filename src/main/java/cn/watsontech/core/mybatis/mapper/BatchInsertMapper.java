package cn.watsontech.core.mybatis.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * Copyright to watsontech
 * Created by Watson on 2019/12/27.
 */
@RegisterMapper
public interface BatchInsertMapper<T> {

    /**
     * 批量插入，忽略插入异常；
     * 支持批量插入的数据库可以使用，例如MySQL,H2等，另外该接口限制实体包含`id`属性并且必须为自增列
     *
     * 注意！！！：该方法当前存在bug，不能返回所有批量插入实体主键，仅能返回第一个实体主键，请谨慎使用
     * @param recordList
     * @return 成功条目数量
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @InsertProvider(type = BatchInsertProvider.class, method = "dynamicSQL")
    int insertListIgnoreConflict(List<T> recordList);


    /**
     * 批量插入，忽略插入异常；
     * 支持批量插入的数据库可以使用，例如MySQL,H2等，另外该接口限制实体包含`id`属性并且必须为自增列
     *
     * 注意！！！：该方法当前存在bug，不能返回所有批量插入实体主键，仅能返回第一个实体主键，请谨慎使用
     * @param batchInsertModel 要插入的实体数据
     * @return 成功条目数量
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @InsertProvider(type = BatchInsertProvider.class, method = "dynamicSQL")
    int insertSelectiveListIgnoreConflict(BatchInsertModel<T> batchInsertModel);
}