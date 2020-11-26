package cn.watsontech.webhelper.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;

import java.util.logging.Logger;

/**
 * Copyright to watsontech
 * Created by Watson on 2019/12/24.
 */
@Aspect
public class DataSourceAspect implements Ordered {
    Logger log = Logger.getLogger(getClass().getName());

    @Override
    public int getOrder() {
        return -10;
    }

    @Before("readPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        UseDatabase useDatabase = methodSignature.getMethod().getAnnotation(UseDatabase.class);
        log.info(String.format("DataSourceAspect doBefore 类方法：%s，注解useDatabase: %s", methodSignature.toShortString(), useDatabase));

        if(useDatabase!=null) {
            DBContextHolder.set(useDatabase.dbType());
        }
    }

    @Pointcut("@annotation(cn.watsontech.webhelper.datasource.UseDatabase)")
    public void readPointcut() {}

    @Pointcut("bsInsertPointcut() || bsUpdatePointcut() || bsDeletePointcut()")
    public void baseServicePointcut() {}
    @Pointcut("execution(public * cn.watsontech.webhelper.service.BaseService.insert*(..))")
    public void bsInsertPointcut() {}
    @Pointcut("execution(public * cn.watsontech.webhelper.service.BaseService.update*(..))")
    public void bsUpdatePointcut() {}
    @Pointcut("execution(public * cn.watsontech.webhelper.service.BaseService.delete*(..))")
    public void bsDeletePointcut() {}
}