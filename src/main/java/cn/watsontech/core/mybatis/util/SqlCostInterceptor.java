package cn.watsontech.core.mybatis.util;

import lombok.extern.log4j.Log4j2;
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
@Log4j2
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
            log.debug("执行SQL耗时 : [{}ms ]", System.currentTimeMillis() - startTime);
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
