package com.watsontech.core.mybatis.util;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 单行单列数据查询
 * Created by Watson on 2020/4/14.
 */
public class SingleRowMapperResultSetExtractor<T> implements ResultSetExtractor<T> {

    private final RowMapper<T> rowMapper;

    /**
     * Create a new RowMapperResultSetExtractor.
     * @param rowMapper the RowMapper which creates an object for each row
     * (just used for optimized collection handling)
     */
    public SingleRowMapperResultSetExtractor(RowMapper<T> rowMapper) {
        Assert.notNull(rowMapper, "RowMapper is required");
        this.rowMapper = rowMapper;
    }

    @Override
    public T extractData(ResultSet rs) throws SQLException {
        List<T> results = new ArrayList<>();
        int rowNum = 0;
        while (rs.next()) {
            results.add(this.rowMapper.mapRow(rs, rowNum++));
        }

        return DataAccessUtils.singleResult(results);
    }

}
