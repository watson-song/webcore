package cn.watsontech.webhelper.mybatis.intf;

import cn.watsontech.webhelper.mybatis.Mapper;
import cn.watsontech.webhelper.mybatis.mapper.BatchInsertModel;
import cn.watsontech.webhelper.mybatis.param.PageParams;
import cn.watsontech.webhelper.mybatis.util.SingleRowMapperResultSetExtractor;
import cn.watsontech.webhelper.web.spring.util.Assert;
import com.github.pagehelper.PageRowBounds;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright to watsontech
 * Created by Watson on 2019/12/18.
 */
public class BaseService<T, PK> implements Service<T, PK> {

    Mapper<T> mapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public BaseService(Mapper<T> mapper) {
        this.mapper = mapper;
    }

    @Override
    public boolean existsWithPrimaryKey(PK key) {
        return this.mapper.existsWithPrimaryKey(key);
    }

    @Override
    public long count(T model) {
        return this.mapper.selectCount(model);
    }

    @Override
    public long countByCondition(Condition condition) {
        return this.mapper.selectCountByCondition(condition);
    }

    @Override
    public int insert(T model) {
        return this.mapper.useGenerateKeyInsert(model);
    }

    @Override
    public int insertSelective(T model) {
        return this.mapper.useGenerateKeyInsertSelective(model);
    }

    @Override
    public int insertList(List<T> models) {
        return this.mapper.insertList(models);
    }

    @Override
    public int insertListIgnoreConflict(List<T> models) {
        return mapper.insertListIgnoreConflict(models);
    }

    @Override
    public int insertSelectiveListIgnoreConflict(List<String> insertPropertis, List<T> models) {
        if (models==null) return 0;
        List<T> noNullModels = models.stream().filter(model -> model!=null).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(noNullModels)) return 0;

        //取第一个元素确定其类型class，根据class获取EntityColumn列表值
        T firstModel = noNullModels.get(0);
        Set<EntityColumn> columnList = EntityHelper.getColumns(firstModel.getClass());

        List<String> insertColumns = new ArrayList<>();
        for (EntityColumn column : columnList) {
            if (!column.isId() && column.isInsertable()) {
                if (insertPropertis.contains(column.getProperty())) {
                    insertColumns.add(column.getColumn());
                }
            }
        }

        return mapper.insertSelectiveListIgnoreConflict(new BatchInsertModel(insertColumns, noNullModels));
    }

    @Override
    public int deleteByPrimaryKey(PK id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByIds(List<PK> ids) {
        return this.mapper.deleteByIds(StringUtils.collectionToCommaDelimitedString(ids));
    }

    @Override
    public int deleteByCondition(Condition condition) {
        return this.mapper.deleteByCondition(condition);
    }

    @Override
    public int updateByCondition(T model, Condition condition) {
        return this.mapper.updateByCondition(model, condition);
    }

    @Override
    public int updateByConditionSelective(T model, Condition condition) {
        return this.mapper.updateByConditionSelective(model, condition);
    }

    @Override
    public int updateByPrimaryKey(T model) {
        return this.mapper.updateByPrimaryKey(model);
    }

    @Override
    public int updateByPrimaryKeySelective(T model) {
        return this.mapper.updateByPrimaryKeySelective(model);
    }

    @Override
    public T selectByPrimaryKey(PK id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    @Override
    public T selectOne(T model) {
        return this.mapper.selectOne(model);
    }

    @Override
    public List<T> selectAll() {
        return this.mapper.selectAll();
    }

    @Override
    public T selectFirst(T model) {
        List<T> objects = this.mapper.selectByRowBounds(model, new RowBounds(0, 1));
        return CollectionUtils.isEmpty(objects)?null:objects.get(0);
    }

    @Override
    public T selectFirstByCondition(Condition condition) {
        List<T> objects = selectByCondition(condition);
        return CollectionUtils.isEmpty(objects)?null:objects.get(0);
    }

    @Override
    public List<T> select(T model) {
        return this.mapper.select(model);
    }

    @Override
    public List<T> selectForStartPage(T model, Integer pageNum, Integer pageSize) {
        return this.mapper.selectByRowBounds(model, new RowBounds((pageNum-1)*pageSize, pageSize));
    }

    @Override
    public List<T> selectByIds(List<PK> ids) {
        return this.mapper.selectByIds(StringUtils.collectionToCommaDelimitedString(ids));
    }

    @Override
    public List<T> selectByCondition(Condition condition) {
        return this.mapper.selectByCondition(condition);
    }

    @Override
    public List<T> selectByConditionForStartPage(Condition condition, Integer pageNum, Integer pageSize) {
        return selectByConditionForOffsetAndLimit(condition, (pageNum-1)*pageSize, pageSize, false);
    }

    @Override
    public List<T> selectByConditionForStartPage(Condition condition, Integer pageNum, Integer pageSize, Boolean count) {
        return selectByConditionForOffsetAndLimit(condition, (pageNum-1)*pageSize, pageSize, count);
    }

    @Override
    public List<T> selectByConditionForOffsetAndLimit(Condition condition, Integer offset, Integer limit, Boolean count) {
        PageRowBounds rowBounds = new PageRowBounds(offset, limit);
        rowBounds.setCount(count);
        return this.mapper.selectByExampleAndRowBounds(condition, rowBounds);
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * 根据sql语句查询
     */
    @Override
    public <T> T queryForObject(String sql, Class<T> requiredType, @Nullable Object... args) {
        return jdbcTemplate.queryForObject(sql, requiredType, args);
    }

    /**
     * 根据sql语句查询单列单行数据，注意：多好请设置 limit 1；
     */
    @Override
    public <T> T queryForSingleColumn(Class<T> returnClass, String sql, Object[] args) {
        return jdbcTemplate.query(sql, args, new SingleRowMapperResultSetExtractor<>(new SingleColumnRowMapper<>(returnClass)));
    }

    /**
     * 根据sql语句查询
     */
    @Override
    public Map<String, Object> queryForMap(String sql, @Nullable Object... args) {
        List<Map<String, Object>> results = queryForList(sql, args);
        if (CollectionUtils.isEmpty(results)) {
            return null;
        }

        if (results.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, results.size());
        }
        return results.iterator().next();
    }

    /**
     * 根据sql语句查询
     */
    @Override
    public <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType) {
        return jdbcTemplate.queryForList(sql, args, elementType);
    }

    /**
     * 根据sql语句查询
     */
    @Override
    public List<Map<String, Object>> queryForList(String sql, Object[] args) {
        return jdbcTemplate.queryForList(sql, args);
    }

    /**
     * 保存单表多项数据
     * @param tableName 表名称
     * @param columns 列名
     * @param datas 要插入的数据，每行数据不能少于列数，空值需要传
     * @param ignoreConflict 是否忽略冲突
     *
     * 使用该方法请打开jdbc的批量驱动参数：rewriteBatchedStatements=true 批量操作batchInsert/update/delete
     */
    @Override
    public int[] batchInsertTable(String tableName, List<String> columns, List<Object[]> datas, boolean ignoreConflict) {
        String[] paramMarks = new String[columns.size()];
        Arrays.fill(paramMarks, "?");

        return jdbcTemplate.batchUpdate(String.format("insert %s into `%s` (%s) values (%s)", ignoreConflict ? "ignore":"", tableName, StringUtils.collectionToDelimitedString(columns, ",", "`", "`"), StringUtils.arrayToCommaDelimitedString(paramMarks)), datas);
    }

    /**
     * 保存单表多项数据
     * @param tableName 表名称
     * @param columns 列名
     * @param datas 要插入的数据，每行数据不能少于列数，空值需要传
     * @param ignoreConflict 是否忽略冲突
     *
     * 使用该方法请打开jdbc的批量驱动参数：rewriteBatchedStatements=true 批量操作batchInsert/update/delete
     */
    @Override
    public int insertTable(String tableName, List<String> columns, List<Object> datas, boolean ignoreConflict) {
        String[] paramMarks = new String[columns.size()];
        Arrays.fill(paramMarks, "?");

        return jdbcTemplate.update(String.format("insert %s into `%s` (%s) values (%s)", ignoreConflict ? "ignore":"", tableName, StringUtils.collectionToDelimitedString(columns, ",", "`", "`"), StringUtils.arrayToCommaDelimitedString(paramMarks)), datas.toArray());
    }

    @Override
    public int insertTable(String tableName, Map<String, Object> datas, boolean ignoreConflict) {
        Assert.notNull(datas, "datas cannot be null");

        Set<String> keys = datas.keySet();
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        for (String key:keys) {
            columns.add(key);
            values.add(datas.get(key));
        }
        return insertTable(tableName, columns, values, ignoreConflict);
    }

    @Override
    public int updateTable(String sql, Object... args) {
        return jdbcTemplate.update(sql, args);
    }

    @Override
    public void executeSql(String sql) {
        jdbcTemplate.execute(sql);
    }

    protected Condition wrapCondition(Class claz) {
        return wrapCondition(claz, null, true);
    }

    @Override
    public Condition wrapCondition(Class claz, String[] properties) {
        return wrapCondition(claz, properties, true);
    }

    @Override
    public Condition wrapCondition(Class claz, String[] properties, boolean withEnabledFilter) {
        Condition condition = new Condition(claz);
        if (properties!=null) {
            condition.selectProperties(properties);
        }else {
            condition.excludeProperties("createdBy", "createdByName", "version", "modifiedBy", "modifiedTime");
        }

        if (withEnabledFilter) {
            condition.createCriteria().andEqualTo("enabled", true);
        }
        return condition;
    }

    /**
     * 删除时候用到的条件
     * @param claz 需要删除的实体类
     * @param id 需要删除的实体id
     * @param <PKI> 主键类型
     */
    protected <PKI> Condition wrapCondition(Class claz, PKI id) {
        Condition condition = new Condition(claz);
        condition.createCriteria().andEqualTo("id", id);
        return condition;
    }

    /**
     * 删除时候用到的条件
     * @param claz 需要删除的实体类
     * @param id 需要删除的实体id
     * @param createdBy 创建人实体id
     * @param <PKI> 主键类型
     * @param <PKC> 创建人主键类型
     */
    protected <PKI, PKC> Condition wrapCondition(Class claz, PKI id, PKC createdBy) {
        Condition condition = new Condition(claz);
        condition.createCriteria().andEqualTo("id", id).andEqualTo("createdBy", createdBy);
        return condition;
    }

    public static PageRowBounds wrapPageRowBounds(PageParams params) {
        return wrapPageRowBounds(params, true);
    }

    public static PageRowBounds wrapPageRowBounds(PageParams params, boolean count) {
        PageRowBounds rowBounds = new PageRowBounds(params.getOffset()!=null?params.getOffset():0, params.getLimit()!=null?params.getLimit():20);
        rowBounds.setCount(count);
        return rowBounds;
    }

}
