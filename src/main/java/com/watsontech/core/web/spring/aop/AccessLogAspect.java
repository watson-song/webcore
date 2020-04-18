package com.watsontech.core.web.spring.aop;

import com.watsontech.core.web.spring.aop.annotation.Access;
import com.watsontech.core.web.spring.aop.annotation.AccessParam;
import com.watsontech.core.web.spring.security.LoginUser;
import com.watsontech.core.web.spring.security.entity.AccessLog;
import com.watsontech.core.web.spring.security.entity.Admin;
import com.watsontech.core.service.AccessLogService;
import com.watsontech.core.web.spring.util.StringUtils;
import com.watsontech.core.utils.HttpUtils;
import com.watsontech.core.utils.MapBuilder;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 系统日志切点类
 *
 * Created by Watson on 2020/02/09.
 */
@Aspect
@Component
@Log4j2
public class AccessLogAspect {

    /**
     * 本地线程日志
     */
    private static final ThreadLocal<Map<JoinPoint, AccessLog>> logThreadLocal = new NamedThreadLocal<>("AccessLogAspectThreadLocal");

    /**
     * 全局变量，同一个请求
     */
    @Autowired(required = false)
    private HttpServletRequest request;

    /**
     * 线程池任务执行程序
     */
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 日志储存service
     */
    @Autowired
    private AccessLogService logService;

    /**
     * Controller层切点 注解拦截
     */
    @Pointcut("@annotation(com.chuanputech.taoanks.core.aop.annotation.AccessLog)")
    public void accessLogPointCut() {
        log.info("+++++++++++++Controller层切点 注解拦截+++++++++++++");
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作的开始时间
     *
     * @param joinPoint 切点
     * @throws InterruptedException
     */
    @Before("accessLogPointCut()")
    public void doBefore(JoinPoint joinPoint) throws InterruptedException {
        // debug模式下 显式打印开始时间用于调试
        // 在log4j中开启本类的debug

        if (log.isDebugEnabled()) {
            log.debug("开始计时: {}  URI: {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()), request.getRequestURI());
        }

        LoginUser user = getCurrentLoginUser();
        AccessLog testLog = wrapAccessLog(joinPoint, "info", request, user);

        putIntoThreadLocal(logThreadLocal, joinPoint, testLog);
    }

    /**
     * 后置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @After("accessLogPointCut()")
    public void doAfter(JoinPoint joinPoint) {

        LoginUser user = getCurrentLoginUser();
        AccessLog testLog = getThreadLocalObject(joinPoint, "info", request, user);

        // 1.直接执行保存操作
        // this.logService.createSystemLog(log);

        // 2.优化:异步保存日志
        // new SaveLogThread(log, logService).start();

        // 3.再优化:通过线程池来执行日志保存
        threadPoolTaskExecutor.execute(new SaveLogThread(testLog));
        putIntoThreadLocal(logThreadLocal, joinPoint, testLog);
    }

    private void putIntoThreadLocal(ThreadLocal<Map<JoinPoint, AccessLog>> logThreadLocal, JoinPoint joinPoint, AccessLog log) {
        Map<JoinPoint,AccessLog> cachedMap = logThreadLocal.get();
        if (cachedMap==null) {
            logThreadLocal.set(MapBuilder.builder().putNext(joinPoint,log));
        }else {
            cachedMap.put(joinPoint, log);
        }
    }

    // 读取session中的用户
    private LoginUser getCurrentLoginUser() {
        HttpSession session = request.getSession();
        LoginUser user = (LoginUser) session.getAttribute("logged_user");
        if (user==null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication!=null) {
                Object principal = authentication.getPrincipal();
                if (principal!=null) {
                    if (principal instanceof LoginUser) {
                        user = (LoginUser) principal;
                    }
                }
            }
        }

        // 登入login操作 前置通知时用户未校验 所以session中不存在用户信息
        if (user == null) {
            user = new Admin();
        }
        return user;
    }

    private AccessLog getThreadLocalObject(JoinPoint joinPoint, String levelType, HttpServletRequest request, LoginUser user) {
        Map<JoinPoint, AccessLog> testLogMap = logThreadLocal.get();
        if (testLogMap == null) {
            testLogMap = MapBuilder.builder().putNext(joinPoint, wrapAccessLog(joinPoint, levelType, request, user));
        }

        AccessLog testLog = testLogMap.get(joinPoint);
        long beginTime = testLog.getCreatedTime().getTime();
        testLog.setTotalTimes(System.currentTimeMillis() - beginTime);

        if (testLog.getCreatedBy()==null&&user!=null) {
            testLog.setCreatedBy(user.getId());
            testLog.setCreatedByName(user.getUsername());
        }

        return testLog;
    }

    private AccessLog wrapAccessLog(JoinPoint joinPoint, String levelType, HttpServletRequest request, LoginUser user) {
        String remoteAddr = HttpUtils.getRealIp(request);// 请求的IP
        String requestUri = request.getRequestURI();// 请求的Uri
        String method = request.getMethod(); // 请求的方法类型(post/get)
        Map<String, String[]> params = request.getParameterMap(); // 请求提交的参数

        String title = "info";
        try {
            title = getControllerMethodDescription(joinPoint);
        } catch (Exception e) {
            e.printStackTrace();
        }

        AccessLog testLog = new AccessLog();
        testLog.setTitle(title);
        testLog.setLevel(levelType);
        testLog.setIp(remoteAddr);
        testLog.setUrl(requestUri);
        testLog.setMethod(method);
        testLog.setParams(StringUtils.getMapToParams(params));
        if (user!=null) {
            testLog.setCreatedBy(user.getId());
            testLog.setCreatedByName(user.getUsername());
        }
        testLog.setCreatedTime(new Date());
        testLog.setTotalTimes(0l);
        return testLog;
    }

    /**
     * 异常通知
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "accessLogPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        LoginUser user = getCurrentLoginUser();
        AccessLog testLog = getThreadLocalObject(joinPoint, "error", request, user);

        if (testLog != null) {
            testLog.setLevel("error");
            if (e.getMessage()!=null) {
                testLog.setException(e.getMessage());
            }else {
                testLog.setException(e.toString());
            }

            new UpdateLogThread(testLog).start();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) {

        /**
         * Returns the signature at the join point. 返回连接点处的签名。
         * getStaticPart().getSignature() returns the same object 返回相同的对象
         */
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Method method = signature.getMethod();

        Access logAnnotation = AnnotationUtils.findAnnotation(method, Access.class);

        String discription = method.getName();
        Object[] descriptionParams = new Object[]{};
        if (logAnnotation !=null) {
            discription = logAnnotation.description();
            Parameter[] params = method.getParameters();
            if (params!=null) {
                List<Object> tempDescParams = new ArrayList<>();
                Object logParamValue = null;
                String[] logParamFieldes = null;
                Object logArg = null;
                for (int i = 0; i < params.length; i++) {
                    AccessParam logParam = AnnotationUtils.findAnnotation(params[i], AccessParam.class);
                    if (logParam!=null) {
                        logArg = joinPoint.getArgs()[i];
                        logParamFieldes = logParam.fields();
                        if (logParamFieldes!=null&&logParamFieldes.length>0) {
                            for (int j = 0; j < logParamFieldes.length; j++) {
                                try {
                                    logParamValue = getFieldValue(logArg,logParamFieldes[j]);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                    logParamValue = "未知";
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                    logParamValue = "未知";
                                }
                                tempDescParams.add(logParamValue);
                            }
                        }else {
                            tempDescParams.add(logArg);
                        }
                    }
                }

                if (tempDescParams.size()>0) {
                    descriptionParams = new Object[tempDescParams.size()];
                    tempDescParams.toArray(descriptionParams);
                }
            }
        }

        return descriptionParams!=null&&descriptionParams.length>0?String.format(discription, descriptionParams):discription;
    }

    /**
     * 保存日志线程
     *
     * @author xuxiaowei
     *
     */
    private class SaveLogThread implements Runnable {
        private final AccessLog testLog;

        public SaveLogThread(AccessLog testLog) {
//            this.testLog = copy(testLog);
            this.testLog = testLog;
        }

        private AccessLog copy(AccessLog source) {
            AccessLog log = new AccessLog();
            BeanUtils.copyProperties(source, log, "id", "createdTime");
            return log;
        }

        @Override
        public void run() {
            if(testLog.getId()==null) {
                logService.insertSelective(testLog);
                log.warn("保存当前日志记录：{}", testLog);
            }else {
                log.warn("当前记录已存在，记录：{}", testLog);
            }
        }
    }

    /**
     * 日志更新线程
     *
     * @author xuxiaowei
     *
     */
    private class UpdateLogThread extends Thread {
        private AccessLog testLog;

        public UpdateLogThread(AccessLog testLog) {
            super(UpdateLogThread.class.getSimpleName());
            this.testLog = testLog;
        }

        @Override
        public void run() {
            logService.updateByPrimaryKeySelective(testLog);
        }
    }

    //属性get方法缓存
    static Map<String, Method> cachedFieldGetMethod = new HashMap<>();

    /**
     * 根据属性，获取get方法
     * @param obj 对象
     * @param name 属性名
     * @return 该属性值
     * @throws Exception
     */
    private static Object getFieldValue(Object obj, String name) throws InvocationTargetException, IllegalAccessException {
        Class claz = obj.getClass();
        String cachedFieldKey = claz.getName()+":"+name;
        Method result = cachedFieldGetMethod.get(cachedFieldKey);
        if (result==null) {
            Method[] m = claz.getMethods();
            for(int i = 0;i < m.length;i++){
                if(("get"+name).toLowerCase().equals(m[i].getName().toLowerCase())){
                    result = m[i];
                }
            }

            if (result!=null) {
                cachedFieldGetMethod.put(cachedFieldKey, result);
            }
        }

        return result!=null?result.invoke(obj):null;
    }
}