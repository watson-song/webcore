package cn.watsontech.core.service.intf;

import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * Copyright to watsontech
 * Created by Watson on 2019/12/18.
 */
public interface Service<T, PK> {

    long count(T model);

    boolean existsWithPrimaryKey(PK key);

    long countByCondition(Condition condition);

    int insert(T model);

    int insertSelective(T model);

    /**
     * 批量插入
     * @param models 插入的实体对象
     * @return 插入成功记录条数
     */
    int insertList(List<T> models);

    /**
     * 批量插入忽略冲突
     * @param models 插入的实体对象
     * @return 插入成功记录条数
     */
    int insertListIgnoreConflict(List<T> models);

    /**
     * 按属性批量插入数据并忽略冲突
     * @param insertProperties 需要插入的对象属性值
     * @param models 插入的实体对象
     * @return 插入成功记录条数
     */
    int insertSelectiveListIgnoreConflict(List<String> insertProperties, List<T> models);

    int deleteByPrimaryKey(PK id);

    int deleteByIds(List<PK> ids);

    int deleteByCondition(Condition condition);

    int updateByCondition(T model, Condition condition);

    int updateByConditionSelective(T model, Condition condition);

    int updateByPrimaryKey(T model);

    int updateByPrimaryKeySelective(T model);

    T selectByPrimaryKey(PK id);

    T selectOne(T model);

    List<T> selectAll();

    T selectFirst(T model);

    T selectFirstByCondition(Condition condition);

    List<T> select(T model);

    List<T> selectForStartPage(T model, Integer pageNum, Integer pageSize);

    List<T> selectByIds(List<PK> ids);

    List<T> selectByCondition(Condition condition);

    List<T> selectByConditionForStartPage(Condition condition, Integer pageNum, Integer pageSize);
}
