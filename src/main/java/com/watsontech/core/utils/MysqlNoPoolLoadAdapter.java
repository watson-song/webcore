package com.watsontech.core.utils;

import com.watsontech.core.mybatis.util.SingleRowMapperResultSetExtractor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by watson on 2020/4/18.
 */
public class MysqlNoPoolLoadAdapter extends NoHelper.NoPoolLoadAdapter {
    JdbcTemplate jdbcTemplate;

    public MysqlNoPoolLoadAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertNewNoRecord(String id, int limit) {
        return jdbcTemplate.update("INSERT ignore INTO `tb_nohelper` (`id`, `no`) VALUES (?, ?)", id, limit);
    }

    @Override
    public int updateNoRecord(String id, int limit, Long version) {
        return jdbcTemplate.update("UPDATE `tb_nohelper` set `no`=no+?,`version`=`version`+1 where `id`=? and `version`=?", limit, id, version);
    }

    @Override
    public Map<String, Long> queryCurrentIndexAndVersionForUpdate(String id) {
        return jdbcTemplate.query("select `no`,`version` from tb_nohelper where `id`=? for UPDATE", new Object[]{id}, new ResultSetExtractor<Map<String, Long>>() {
            @Override
            public Map<String, Long> extractData(ResultSet rs) throws SQLException, DataAccessException {
                rs.next();
                return MapBuilder.builder().putNext("no", rs.getLong("no")).putNext("version", rs.getLong("version"));
            }
        });
    }

    @Override
    public Long loadFromSource(String id) {
        return jdbcTemplate.query("select no from tb_nohelper where id=?", new SingleRowMapperResultSetExtractor<Long>(new SingleColumnRowMapper<Long>(Long.class)), id);
    }
}