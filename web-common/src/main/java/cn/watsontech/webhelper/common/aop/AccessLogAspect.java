package cn.watsontech.webhelper.common.aop;

import cn.watsontech.webhelper.common.aop.annotation.Access;
import cn.watsontech.webhelper.common.aop.annotation.AccessParam;
import cn.watsontech.webhelper.common.aop.entity.AccessLog;
import cn.watsontech.webhelper.common.security.LoginUser;
import cn.watsontech.webhelper.common.security.authentication.AccountService;
import cn.watsontech.webhelper.common.util.RequestUtils;
import cn.watsontech.webhelper.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringValueResolver;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
public class AccessLogAspect implements EmbeddedValueResolverAware {
    protected static final Log log = LogFactory.getLog(AccessLogAspect.class);

    /**
     * 本地线程日志
     */
    private static final ThreadLocal<Long> logThreadLocal = new NamedThreadLocal("AccessLogAspectThreadLocal");

    /**
     * 线程池任务执行程序
     */
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 日志储存service
     */
    @Autowired
    private LogService logService;

    @Nullable
    private StringValueResolver embeddedValueResolver;

    /**
     * Controller层切点 注解拦截
     */
    @Pointcut("@annotation(cn.watsontech.webhelper.common.aop.annotation.Access)")
    public void accessLogPointCut() {
        log.info("+++++++++++++Controller层切点 注解拦截+++++++++++++");
    }

    /**
     * 环绕通知 用于拦截Controller层记录用户的操作的开始时间
     *
     * @param joinPoint 切点
     */
    @Around("accessLogPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        logThreadLocal.set(System.currentTimeMillis());
        result = joinPoint.proceed();

        LoginUser user = getCurrentLoginUser();
        if (user==null) {
            org.aspectj.lang.Signature signature = joinPoint.getSignature();
            //检查是否是登陆接口
            if (signature.getDeclaringType()==AccountService.class) {
                if (signature.getName().startsWith("loginBy")) {
                    if (result instanceof LoginUser) {
                        user = (LoginUser) result;
                    }
                }
            }
        }

        saveLog(joinPoint, null, user);
        return result;
    }

    private void saveLog(JoinPoint joinPoint, Throwable exception, LoginUser user) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Access access = AnnotationUtils.findAnnotation(method, Access.class);
        AccessLog testLog = wrapAccessLog(method, joinPoint.getArgs(), access, exception, user);
        logThreadLocal.remove();

        // 1.直接执行保存操作
        // this.logService.createSystemLog(log);

        // 2.优化:异步保存日志
        // new SaveLogThread(log, logService).start();

        // 3.再优化:通过线程池来执行日志保存
        threadPoolTaskExecutor.execute(new SaveLogThread(testLog, isAccessShouldSave(access)));
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

        return user;
    }

    private AccessLog wrapAccessLog(Method signatureMethod, Object[] methodArgs, Access accessAnnotation, Throwable exception, LoginUser user) {
        HttpServletRequest request = RequestUtils.getHttpServletRequest();
        String remoteAddr = RequestUtils.getIpAddress(request);// 请求的IP
        String requestBrowser = RequestUtils.getBrowser(request);// 请求的浏览器类型
        String requestUri = request.getRequestURI();// 请求的Uri
        String method = request.getMethod(); // 请求的方法类型(post/get)

        AccessParamValue accessParamValue = null;
        String levelType = accessAnnotation.level();
        String exMessage = null;
        if (exception!=null) {
            levelType = "error";
            exMessage = exception.getMessage();
            if (exMessage==null) {
                exMessage = exception.toString();
            }
        }

        if (levelType!=null&&embeddedValueResolver!=null) {
            levelType = embeddedValueResolver.resolveStringValue(levelType);
        }

        try {
            accessParamValue = getControllerMethodDescription(signatureMethod, methodArgs, accessAnnotation);
        } catch (Exception e) {
            e.printStackTrace();
            accessParamValue = new AccessParamValue("未知",null, null);
        }

        AccessLog testLog = new AccessLog();
        testLog.setLevel(levelType);
        testLog.setIp(remoteAddr);
        testLog.setUrl(requestUri);
        testLog.setBrowser(requestBrowser);
        testLog.setMethod(method);
        testLog.setException(exMessage);
        if (accessParamValue!=null) {
            testLog.setTitle(accessParamValue.getDescription());
            testLog.setGroupId(accessParamValue.getGroupId());
            testLog.setGroupField(accessParamValue.getGroupField());
        }
        testLog.setVersion(1);
        testLog.setParams(getParameters(signatureMethod, methodArgs));
        if (user!=null) {
            testLog.setCreatedBy(user.getId());
            testLog.setCreatedByName(user.getFullName());
        }
        testLog.setCreatedTime(new Date());
        testLog.setTotalTimes(System.currentTimeMillis()-logThreadLocal.get());
        return testLog;
    }

    /**
     * 根据方法和传入的参数获取请求参数
     */
    private String getParameters(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!StringUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }

        if (!CollectionUtils.isEmpty(argList)) {
            if (argList.size()==1) {
                return JSON.toJSONString(argList.get(0));
            }

            return JSON.toJSONString(argList);
        }
        return "";
    }

    /**
     * 异常通知
     */
    @AfterThrowing(pointcut = "accessLogPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        LoginUser user = getCurrentLoginUser();

        saveLog(joinPoint, e, user);
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
        String groupField;

        public AccessParamValue(String description, String groupId, String groupField) {
            this.description = description;
            this.groupId = groupId;
            this.groupField = groupField;
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

        public String getGroupField() {
            return groupField;
        }

        public void setGroupField(String groupField) {
            this.groupField = groupField;
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * 注意：如果有多个参数被标记为groupId但话，groupField和groupId为最后一个生效
     * @param method 方法
     * @return 方法描述
     */
    private AccessParamValue getControllerMethodDescription(Method method, Object[] joinPointArgs, Access logAnnotation) {
        String description = method.getName();
        Object logGroupId = null;
        String logGroupField = null;
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
                                    //更新groupId和groupField
                                    logGroupId = logParamValue;
                                    logGroupField = groupField;
                                }
                            }
                        }else {
                            tempDescParams.add(logArg);
                            if (logParam.isGroupId()) {
                                //若是组id，则设置logGroupId
                                logGroupId = logArg;
                                logGroupField = logParam.groupIdField();
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

        return new AccessParamValue(descriptionParams!=null&&descriptionParams.length>0?String.format(description, descriptionParams):description, logGroupId!=null?logGroupId.toString():null, logGroupField);
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.embeddedValueResolver = resolver;
    }

    /**
     * 保存日志线程
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
                if (testLog!=null) {
                    if (testLog.getId()!=null) {
                        logService.update(testLog.getId(), testLog);
                    }else {
                        logService.save(testLog);
                    }
                }
            }

            log.info(String.format("访问日志：%s", testLog));
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