package cn.watsontech.webhelper.mybatis.mapper;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

import static tk.mybatis.mapper.mapperhelper.SqlHelper.getDynamicTableName;

/**
 * Copyright to watsontech
 * 批量插入，有问题忽略
 *
 * Created by Watson on 2019/12/27.
 */
public class BatchInsertProvider extends MapperTemplate {

    public BatchInsertProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 批量插入
     *
     * @param ms
     */
    public String insertListIgnoreConflict(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append(insertIgnoreIntoTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, true, false, false));
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            if (!column.isId() && column.isInsertable()) {
                sql.append(column.getColumnHolder("record") + ",");
            }
        }
        sql.append("</trim>");
        sql.append("</foreach>");
        return sql.toString();
    }

    /**
     * 批量插入
     *
     * @param ms
     */
    public String insertSelectiveListIgnoreConflict(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append(insertIgnoreIntoTable(entityClass, tableName(entityClass)));
        sql.append(insertColumns(entityClass, true, true, true));
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"_parameter.list\" item=\"record\" separator=\",\" >");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            if (!column.isId() && column.isInsertable()) {
                sql.append(getIfPresentAndNotNull("record", column, column.getColumnHolder("record", null, ","), isNotEmpty()));
            }
        }
        sql.append("</trim>");
        sql.append("</foreach>");

//        String targetSql = sql.toString();
//        System.err.println("insertSelectiveListIgnoreConflict 生成语句为："+ targetSql);
//        return targetSql;
        return sql.toString();
    }


    /**
     * insert table()列
     *
     * @param entityClass
     * @param skipId      是否从列中忽略id类型
     * @param notNull     是否判断!=null
     * @param notEmpty    是否判断String类型!=''
     * @return
     */
    public static String insertColumns(Class<?> entityClass, boolean skipId, boolean notNull, boolean notEmpty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        sql.append(insertSelectiveColumns(entityClass));
        sql.append("</trim>");
        return sql.toString();
    }

    /**
     * 插入支持按指定列插入
     *
     * @return
     */
    public static String insertSelectiveColumns(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<choose>");
        sql.append("<when test=\"@cn.watsontech.webhelper.mybatis.mapper.BatchInsertOgnl@hasInsertSelectiveColumns(_parameter.list)\">");
        sql.append("<foreach collection=\"_parameter.list.insertColumns\" item=\"insertColumn\" separator=\",\">");
        sql.append("${insertColumn}");
        sql.append("</foreach>");
        sql.append("</when>");
        //不支持指定列的时候查询全部列
        sql.append("<otherwise>");
        sql.append(SqlHelper.getAllColumns(entityClass));
        sql.append("</otherwise>");
        sql.append("</choose>");
        return sql.toString();
    }

    /**
     * 判断是否存在_parameter.insertColumns且 !=null的条件结构
     *
     * @param entityName
     * @param column
     * @param contents
     * @param empty
     * @return
     */
    public static String getIfPresentAndNotNull(String entityName, EntityColumn column, String contents, boolean empty) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"");
//        if (StringUtil.isNotEmpty(entityName)) {
//            sql.append(entityName).append(".");
//        }
//        sql.append(column.getProperty()).append(" != null");

//      sql.append(" and ");
        sql.append(" @cn.watsontech.webhelper.mybatis.mapper.BatchInsertOgnl@hasInsertSelectiveColumn(_parameter.list, '").append(column.getColumn()).append("')");

//        if (empty && column.getJavaType().equals(String.class)) {
//            sql.append(" and ");
//            if (StringUtil.isNotEmpty(entityName)) {
//                sql.append(entityName).append(".");
//            }
//            sql.append(column.getProperty()).append(" != '' ");
//        }
        sql.append("\">");
        sql.append(contents);
        sql.append("</if>");
        return sql.toString();
    }

    /**
     * insert into tableName - 动态表名
     *
     * @param entityClass
     * @param defaultTableName
     * @return
     */
    public static String insertIgnoreIntoTable(Class<?> entityClass, String defaultTableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT IGNORE INTO ");
        sql.append(getDynamicTableName(entityClass, defaultTableName));
        sql.append(" ");
        return sql.toString();
    }
}

