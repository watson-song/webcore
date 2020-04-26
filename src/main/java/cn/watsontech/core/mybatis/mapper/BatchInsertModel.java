package cn.watsontech.core.mybatis.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright to watsontech
 * 批量插入参数
 * Created by Watson on 2020/2/5.
 */
public class BatchInsertModel<T> extends ArrayList<T> {
    List<String> insertColumns; //插入列属性名

    public BatchInsertModel(List<T> models) {
        super(models);
    }

    public BatchInsertModel(List<String> insertColumns, List<T> models) {
        super(models);
        this.insertColumns = insertColumns;
    }

    public List<String> getInsertColumns() {
        return insertColumns;
    }

    public void setInsertColumns(List<String> insertColumns) {
        this.insertColumns = insertColumns;
    }

}
