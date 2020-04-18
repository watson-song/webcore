package com.watsontech.core.service.mapper.manually;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Watson on 2019/12/21.
 */
@Mapper
public interface AccessLogManualMapper {

    @Select("select count(1) from tb_access_log")
    int selectCount();
}
