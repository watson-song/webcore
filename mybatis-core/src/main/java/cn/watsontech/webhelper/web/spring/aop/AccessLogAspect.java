package cn.watsontech.webhelper.web.spring.aop;

import cn.watsontech.webhelper.service.AccessLogService;
import cn.watsontech.webhelper.utils.HttpUtils;
import cn.watsontech.webhelper.utils.MapBuilder;
import cn.watsontech.webhelper.web.spring.aop.annotation.Access;
import cn.watsontech.webhelper.web.spring.aop.annotation.AccessParam;
import cn.watsontech.webhelper.web.spring.security.LoginUser;
import cn.watsontech.webhelper.web.spring.security.entity.AccessLog;
import cn.watsontech.webhelper.web.spring.security.entity.Admin;
import cn.watsontech.webhelper.web.spring.util.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
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
public class AccessLogAspect implements EmbeddedValueResolverAware {

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

    @Nullable
    private StringValueResolver embeddedValueResolver;

    /**
     * Controller层切点 注解拦截
     */
    @Pointcut("@annotation(cn.watsontech.webhelper.web.spring.aop.annotation.Access)")
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
        AccessLog testLog = wrapAccessLog(joinPoint, request, user);

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
        AccessLog testLog = getThreadLocalObject(joinPoint, null, request, user);
        if (testLog!=null) {
            // 1.直接执行保存操作
            // this.logService.createSystemLog(log);

            // 2.优化:异步保存日志
            // new SaveLogThread(log, logService).start();

            // 3.再优化:通过线程池来执行日志保存
            Access access = getAccessAnnotation(joinPoint);
            threadPoolTaskExecutor.execute(new SaveLogThread(testLog, isAccessShouldSave(access)));
            putIntoThreadLocal(logThreadLocal, joinPoint, testLog);
        }
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
        LoginUser user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication!=null) {
            Object principal = authentication.getPrincipal();
            if (principal!=null) {
                if (principal instanceof LoginUser) {
                    user = (LoginUser) principal;
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
        if (testLog!=null) {
            long beginTime = testLog.getCreatedTime().getTime();
            testLog.setTotalTimes(System.currentTimeMillis() - beginTime);

            if (testLog.getCreatedBy()==null&&user!=null) {
                testLog.setCreatedBy(user.getId());
                testLog.setCreatedByName(user.getFullName());
            }
        }

        return testLog;
    }

    private AccessLog wrapAccessLog(JoinPoint joinPoint, HttpServletRequest request, LoginUser user) {
        return wrapAccessLog(joinPoint, null, request, user);
    }

    private AccessLog wrapAccessLog(JoinPoint joinPoint, String levelType, HttpServletRequest request, LoginUser user) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Map<String, String[]> params = new HashMap<>();
        String method = "GET";
        String requestUri = "/未知路径";
        String remoteAddr = "未知ip地址";
        if (requestAttributes!=null) {

            remoteAddr = HttpUtils.getRealIp(request);// 请求的IP
            requestUri = request.getRequestURI();// 请求的Uri
            method = request.getMethod(); // 请求的方法类型(post/get)
            params = request.getParameterMap(); // 请求提交的参数
        }

        AccessParamValue accessParamValue = null;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();

        Access accessAnnotation = AnnotationUtils.findAnnotation(signatureMethod, Access.class);
        levelType = accessAnnotation.level();
        if (levelType!=null&&embeddedValueResolver!=null) {
            levelType = embeddedValueResolver.resolveStringValue(levelType);
        }

        try {
            accessParamValue = getControllerMethodDescription(signatureMethod, joinPoint.getArgs(), accessAnnotation);
        } catch (Exception e) {
            e.printStackTrace();
            accessParamValue = new AccessParamValue("未知",null);
        }

        AccessLog testLog = new AccessLog();
        testLog.setLevel(levelType);
        testLog.setIp(remoteAddr);
        testLog.setUrl(requestUri);
        testLog.setMethod(method);
        if (accessParamValue!=null) {
            testLog.setTitle(accessParamValue.getDescription());
            testLog.setGroupId(accessParamValue.getGroupId());
        }
        testLog.setVersion(1);
        testLog.setParams(StringUtils.getMapToParams(params));
        if (user!=null) {
            testLog.setCreatedBy(user.getId());
            testLog.setCreatedByName(user.getFullName());
        }
        testLog.setCreatedTime(new Date());
        testLog.setTotalTimes(0l);
        return testLog;
    }

    @AfterReturning(pointcut="accessLogPointCut()", returning="returnValue")
    public void doAfterRturning(JoinPoint joinPoint, Object returnValue) {
        LoginUser user = getCurrentLoginUser();
        AccessLog testLog = wrapAccessLog(joinPoint, request, user);

        putIntoThreadLocal(logThreadLocal, joinPoint, testLog);
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

            Access access = getAccessAnnotation(joinPoint);
            new UpdateLogThread(testLog, isAccessShouldSave(access)).start();
        }
    }

    private boolean isAccessShouldSave(Access access) {
        boolean result = false;

        if (access!=null) {
            String saveValue = access.save();
            //支持可配置log语句

            if (this.embeddedValueResolver != null) {
                saveValue = this.embeddedValueResolver.resolveStringValue(saveValue);
            }

            try {
                result = Boolean.parseBoolean(saveValue);
            } catch (Exception ex) {//eat it}
            }
        }

        return result;
    }

    static class AccessParamValue {
        String description;
        String groupId;

        public AccessParamValue(String description, String groupId) {
            this.description = description;
            this.groupId = groupId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param method 方法
     * @return 方法描述
     */
    private AccessParamValue getControllerMethodDescription(Method method, Object[] joinPointArgs, Access logAnnotation) {
        String description = method.getName();
        Object logGroupId = null;
        Object[] descriptionParams = new Object[]{};
        if (logAnnotation !=null) {
            description = logAnnotation.description();
            //支持可配置log语句
            if (this.embeddedValueResolver != null) {
                description = this.embeddedValueResolver.resolveStringValue(description);
            }

            Parameter[] params = method.getParameters();
            if (params!=null) {
                List<Object> tempDescParams = new ArrayList<>();
                Object logParamValue = null;
                String[] logParamFieldes = null;
                Object logArg = null;
                for (int i = 0; i < params.length; i++) {
                    AccessParam logParam = AnnotationUtils.findAnnotation(params[i], AccessParam.class);
                    if (logParam!=null) {
                        logArg = joinPointArgs[i];
                        logParamFieldes = logParam.fields();
                        if (logParamFieldes!=null&&logParamFieldes.length>0) {
                            String groupField = logParam.groupIdField();
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

                                if (groupField!=null&&groupField.equalsIgnoreCase(logParamFieldes[j])) {
                                    logGroupId = logParamValue;
                                }
                            }
                        }else {
                            tempDescParams.add(logArg);
                            if (logParam.isGroupId()) {
                                //若是组id，则设置logGroupId
                                logGroupId = logArg;
                            }
                        }
                    }
                }

                if (tempDescParams.size()>0) {
                    descriptionParams = new Object[tempDescParams.size()];
                    tempDescParams.toArray(descriptionParams);
                }
            }
        }

        return new AccessParamValue(descriptionParams!=null&&descriptionParams.length>0?String.format(description, descriptionParams):description, logGroupId!=null?logGroupId.toString():null);
    }

    private Access getAccessAnnotation(JoinPoint joinPoint) {
        /**
         * Returns the signature at the join point. 返回连接点处的签名。
         * getStaticPart().getSignature() returns the same object 返回相同的对象
         */
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return AnnotationUtils.findAnnotation(method, Access.class);
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.embeddedValueResolver = resolver;
    }

    /**
     * 保存日志线程
     *
     * @author xuxiaowei
     *
     */
    private class SaveLogThread implements Runnable {
        private final AccessLog testLog;
        private final boolean save;

        public SaveLogThread(AccessLog testLog, boolean saveDb) {
            this.testLog = testLog;
            this.save = saveDb;
        }

        @Override
        public void run() {
            if (save) {
                if(testLog.getId()==null) {
                    logService.insertSelective(testLog);
                }else {
                    log.warn("当前记录已存在，记录：{}", testLog);
                }
            }

            log.info("访问日志：{}", testLog);
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
        private boolean save;

        public UpdateLogThread(AccessLog testLog, boolean save) {
            super(UpdateLogThread.class.getSimpleName());
            this.testLog = testLog;
            this.save = save;
        }

        @Override
        public void run() {
            if (save) {
                logService.updateByPrimaryKeySelective(testLog);
            }

            log.info("更新访问日志：{}", testLog);
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
    private Object getFieldValue(Object obj, String name) throws InvocationTargetException, IllegalAccessException {
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