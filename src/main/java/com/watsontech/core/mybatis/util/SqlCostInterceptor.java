package com.watsontech.core.mybatis.util;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;

import java.sql.Statement;
import java.util.Properties;

/**
 * Sql执行时间记录拦截器
 *
 * Created by Watson on 2019/12/16.
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "query", args = { Statement.class, ResultHandler.class }),
        @Signature(type = StatementHandler.class, method = "update", args = { Statement.class }),
        @Signature(type = StatementHandler.class, method = "batch", args = { Statement.class }) })
public class SqlCostInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        long startTime = System.currentTimeMillis();

        try {
            return invocation.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long sqlCost = endTime - startTime;
            System.out.println("执行SQL耗时 : [" + sqlCost + "ms ] ");
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
