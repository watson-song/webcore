package cn.watsontech.core.mybatis.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * Copyright to watsontech
 * Created by Watson on 2019/12/27.
 */
@RegisterMapper
public interface UseGenerateKeyInsertMapper<T> {

    /**
     * 使用可返回主键的方法插入；
     *
     * @param record
     * @return
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @InsertProvider(type = UseGenerateKeyInsertProvider.class, method = "dynamicSQL")
    int useGenerateKeyInsertSelective(T record);

    /**
     * 保存一个实体，null的属性也会保存，不会使用数据库默认值
     *
     * @param record
     * @return
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @InsertProvider(type = UseGenerateKeyInsertProvider.class, method = "dynamicSQL")
    int useGenerateKeyInsert(T record);
}