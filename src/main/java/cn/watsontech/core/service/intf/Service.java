package cn.watsontech.core.service.intf;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.Map;

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

    List<T> selectByConditionForStartPage(Condition condition, Integer pageNum, Integer pageSize, Boolean count);

    List<T> selectByConditionForOffsetAndLimit(Condition condition, Integer offset, Integer limit, Boolean count);

    JdbcTemplate getJdbcTemplate();

    <T> T queryForObject(String sql, Class<T> requiredType, @Nullable Object... args) throws DataAccessException;

    Map<String, Object> queryForMap(String sql, @Nullable Object... args) throws DataAccessException;

    <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType) throws DataAccessException;

    /**
     * 保存单表多项数据
     * @param tableName 表名称
     * @param columns 列名
     * @param datas 要插入的数据，每行数据不能少于列数，空值需要传
     * @param ignoreConflict 是否忽略冲突
     *
     * 使用该方法请打开jdbc的批量驱动参数：rewriteBatchedStatements=true 批量操作batchInsert/update/delete
     */
    int[] batchInsertTable(String tableName, List<String> columns, List<Object[]> datas, boolean ignoreConflict);

    /**
     * 保存单表多项数据
     * @param tableName 表名称
     * @param columns 列名
     * @param datas 要插入的数据，每行数据不能少于列数，空值需要传
     * @param ignoreConflict 是否忽略冲突
     *
     * 使用该方法请打开jdbc的批量驱动参数：rewriteBatchedStatements=true 批量操作batchInsert/update/delete
     */
    int insertTable(String tableName, List<String> columns, List<Object> datas, boolean ignoreConflict);

    int insertTable(String tableName, Map<String, Object> datas, boolean ignoreConflict);

    int updateTable(String sql, Object... args);

    /**
     * 参见
     * @see wrapCondition(claz, properties, withEnabledFilter=true)
     */
    Condition wrapCondition(Class claz, String[] properties);

    /**
     * 返回查询条件
     * @param claz 类必填
     * @param properties 若非空，则查询当前实体的参数列表，若为空，则查询当前实体除了（createdBy、createdByName、version、modifiedBy、modifiedTime）以外的所有属性
     * @param withEnabledFilter 是否包含enabled=true的查询条件
     * @return 查询条件
     */
    Condition wrapCondition(Class claz, String[] properties, boolean withEnabledFilter);
}
