package com.watsontech.core.web.spring.datasource;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * Copyright to watsontech
 * Created by Watson on 2019/12/24.
 */
@Aspect
@Component
@Log4j2
public class DataSourceAspect implements Ordered {

    @Override
    public int getOrder() {
        return -10;
    }

    @Before("readPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        UseDatabase useDatabase = methodSignature.getMethod().getAnnotation(UseDatabase.class);
        log.debug("DataSourceAspect doBefore 类方法：{}，注解useDatabase: {}", methodSignature.toShortString(), useDatabase);

        if(useDatabase!=null) {
            DBContextHolder.set(useDatabase.dbType());
        }
    }

    @Pointcut("@annotation(com.chuanputech.taoanks.core.datasource.UseDatabase)")
    public void readPointcut() {}

//    @Before("baseServicePointcut()")
//    public void doBeforeBaseService(JoinPoint joinPoint) {
//        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
//        log.debug("DataSourceAspect doBeforeBaseServiceImpl 类方法：{}, 参数：{}", methodSignature.toLongString(), joinPoint.getArgs());
//
//        DBContextHolder.set(DBTypeEnum.MASTER);
//    }

    @Pointcut("bsInsertPointcut() || bsUpdatePointcut() || bsDeletePointcut()")
    public void baseServicePointcut() {}

    @Pointcut("execution(public * com.chuanputech.taoanks.core.service.BaseService.insert*(..))")
    public void bsInsertPointcut() {}
    @Pointcut("execution(public * com.chuanputech.taoanks.core.service.BaseService.update*(..))")
    public void bsUpdatePointcut() {}
    @Pointcut("execution(public * com.chuanputech.taoanks.core.service.BaseService.delete*(..))")
    public void bsDeletePointcut() {}
}