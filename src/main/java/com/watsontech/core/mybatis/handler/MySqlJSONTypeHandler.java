package com.watsontech.core.mybatis.handler;

/**
 * Created by watson on 2019/8/12.
 */
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(JSON.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class MySqlJSONTypeHandler extends BaseTypeHandler<JSON> {
    /**
     * 设置非空参数
     * @param ps
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSON parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, String.valueOf(parameter.toJSONString()));
    }

    /**
     * 根据列名，获取可以为空的结果
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    @Override
    public JSON getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String sqlJson = rs.getString(columnName);
        if (null != sqlJson){
            try {
                return JSONObject.parseObject(sqlJson);
            }catch (Exception ex) {
                return JSONArray.parseArray(sqlJson);
            }
        }
        return null;
    }

    /**
     * 根据列索引，获取可以为空的结果
     * @param rs
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public JSON getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String sqlJson = rs.getString(columnIndex);
        if (null != sqlJson){
            try {
                return JSONObject.parseObject(sqlJson);
            }catch (Exception ex) {
                return JSONArray.parseArray(sqlJson);
            }
        }
        return null;
    }

    @Override
    public JSON getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String sqlJson = cs.getString(columnIndex);
        if (null != sqlJson){
            try {
                return JSONObject.parseObject(sqlJson);
            }catch (Exception ex) {
                return JSONArray.parseArray(sqlJson);
            }
        }
        return null;
    }
}

