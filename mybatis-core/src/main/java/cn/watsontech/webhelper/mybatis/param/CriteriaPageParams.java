package cn.watsontech.webhelper.mybatis.param;

import tk.mybatis.mapper.entity.Example;

/**
 * 带搜索条件的分页参数
 * Created by Watson on 2020/03/06.
 */
public abstract class CriteriaPageParams extends BasicPageParams {

    /**
     * 子类填充搜索条件
     */
    public abstract void fillCriteria(Example.Criteria criteria);
}
