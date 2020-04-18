package cn.watsontech.core.mybatis.mapper;

import java.util.List;

/**
 * Copyright to watsontech
 * 批量插入参数
 * Created by Watson on 2020/2/5.
 */
public class BatchInsertModel<T>{
    List<String> insertColumns; //插入列属性名
    List<T> list;

    public BatchInsertModel(List<T> models) {
        this.list = models;
    }

    public BatchInsertModel(List<String> insertColumns, List<T> models) {
        this.insertColumns = insertColumns;
        this.list = models;
    }

    public List<String> getInsertColumns() {
        return insertColumns;
    }

    public void setInsertColumns(List<String> insertColumns) {
        this.insertColumns = insertColumns;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
