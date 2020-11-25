package cn.watsontech.webhelper.web.spring.datasource;

import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Copyright to watsontech
 * Created by Watson on 2020/02/09.
 */
@Log4j2
public class DataSourceRouting extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DBContextHolder.get();
    }

}
