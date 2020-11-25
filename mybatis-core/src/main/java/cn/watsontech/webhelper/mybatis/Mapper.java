package cn.watsontech.webhelper.mybatis;

import cn.watsontech.webhelper.mybatis.mapper.BatchInsertMapper;
import cn.watsontech.webhelper.mybatis.mapper.UseGenerateKeyInsertMapper;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Copyright to watsontech
 * 定制版MyBatis Mapper插件接口，如需其他接口参考官方文档自行添加。
 * Created by Watson on 2019/12/17.
 */
public interface Mapper<T> extends
        tk.mybatis.mapper.common.Mapper<T>,
        BatchInsertMapper<T>,
        UseGenerateKeyInsertMapper<T>,
        ConditionMapper<T>,
        IdsMapper<T>,
        MySqlMapper<T> {
}