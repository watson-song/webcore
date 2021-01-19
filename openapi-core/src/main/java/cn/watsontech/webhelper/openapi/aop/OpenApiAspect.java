package cn.watsontech.webhelper.openapi.aop;

import cn.watsontech.webhelper.common.util.RequestUtils;
import cn.watsontech.webhelper.openapi.aop.annotation.OpenApi;
import cn.watsontech.webhelper.openapi.entity.AppInfo;
import cn.watsontech.webhelper.openapi.params.base.OpenApiParams;
import cn.watsontech.webhelper.openapi.params.base.PublicApiParams;
import cn.watsontech.webhelper.openapi.service.AppInfoService;
import cn.watsontech.webhelper.utils.Md5Util;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Watson on 2019/12/11.
 */
@Aspect
@Component
public class OpenApiAspect {
    protected static final Log log = LogFactory.getLog(OpenApiAspect.class);

    @Autowired
    AppInfoService openAppInfoService;

    //	ThreadLocal是什么
//	早在JDK 1.2的版本中就提供Java.lang.ThreadLocal，ThreadLocal为解决多线程程序的并发问题提供了一种新的思路。使用这个工具类可以很简洁地编写出优美的多线程程序。
//	当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
//	从线程的角度看，目标变量就象是线程的本地变量，这也是类名中“Local”所要表达的意思。
//	所以，在Java中编写线程局部变量的代码相对来说要笨拙一些，因此造成线程局部变量没有在Java开发者中得到很好的普及。
//	ThreadLocal的接口方法
//	ThreadLocal类接口很简单，只有4个方法，我们先来了解一下：
//	void set(Object value)设置当前线程的线程局部变量的值。
//	public Object get()该方法返回当前线程所对应的线程局部变量。
//	public void remove()将当前线程局部变量的值删除，目的是为了减少内存的占用，该方法是JDK 5.0新增的方法。需要指出的是，当线程结束后，对应该线程的局部变量将自动被垃圾回收，所以显式调用该方法清除线程的局部变量并不是必须的操作，但它可以加快内存回收的速度。
//	protected Object initialValue()返回该线程局部变量的初始值，该方法是一个protected的方法，显然是为了让子类覆盖而设计的。这个方法是一个延迟调用方法，在线程第1次调用get()或set(Object)时才执行，并且仅执行1次。ThreadLocal中的缺省实现直接返回一个null。
//	值得一提的是，在JDK5.0中，ThreadLocal已经支持泛型，该类的类名已经变为ThreadLocal<T>。API方法也相应进行了调整，新版本的API方法分别是void set(T value)、T get()以及T initialValue()。
    ThreadLocal<PublicApiParams> publicApiParamThreadLocal = new ThreadLocal<PublicApiParams>();

    /**
     * 定义一个切入点.
     */
    @Pointcut("@annotation(cn.watsontech.webhelper.web.spring.aop.annotation.OpenApi)")
    public void openApi() {}

    /**
     * 前置通知，方法调用前被调用
     *
     * @param joinPoint
     */
    @Before("openApi()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String requestUrl = request.getRequestURI();

        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        OpenApi openApi = methodSignature.getMethod().getAnnotation(OpenApi.class);

        log.info(String.format("OpenApiAspect-%s.doBefore()，%s, %s", openApi.value(), RequestUtils.getIpAddress(request), requestUrl));

        PublicApiParams apiParams = null;

        //参与签名的额外参数
        List<OpenApiParams> extraApiParams = new ArrayList<>();
        Object[] args = joinPoint.getArgs();
        if (args!=null&&args.length>0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof PublicApiParams) {
                    apiParams = (PublicApiParams) args[i];
                }

                if (args[i] instanceof OpenApiParams) {
                    extraApiParams.add((OpenApiParams) args[i]);
                }
            }
        }

        Assert.notNull(apiParams, "非法请求");
        apiParams.setRequestId(RandomStringUtils.randomAlphanumeric(12));
        publicApiParamThreadLocal.set(apiParams);

        AppInfo openAppinfoVo = openAppInfoService.selectByAppId(apiParams.getAppid());
        Assert.notNull(openAppinfoVo, "非法请求：未识别的appid");

        String appSecret = openAppinfoVo.getSecret();

        int maximumDelayTime = 30000;//默认30s
        if (openAppinfoVo.getAllowDelay()!=null) {
            maximumDelayTime = openAppinfoVo.getAllowDelay()*1000;//30s
        }

        String needSignParamString = apiParams.getNeedSignParamString(extraApiParams);
        Assert.isTrue(needSignParamString!=null&&!needSignParamString.equals(""), "请求签名参数列表为空");

        String signedString = Md5Util.MD5Encode(String.format("%s&appSecret=%s&url=%s", needSignParamString, appSecret, requestUrl)).toUpperCase();
        long currentTimestamp = System.currentTimeMillis();
        log.info(String.format("OpenApiAspect.doBefore 加密字符串：%s，已签名字符串：%s, 当前时间戳：%s", needSignParamString, signedString, currentTimestamp));

        Assert.isTrue(signedString.equals(apiParams.getSign()), "非法请求：签名错误");
        long timestampGap = currentTimestamp-apiParams.getTimestamp();
        Assert.isTrue(Math.abs(timestampGap)<maximumDelayTime/*&&timestampGap>0*/, "非法请求：无效的时间戳");
    }

    @After("openApi()")
    public void doAfter(JoinPoint joinPoint) {
        PublicApiParams apiParams = publicApiParamThreadLocal.get();
        log.info(String.format("OpenApiAspect.doAfter()，请求ID：%s", apiParams.getRequestId()));
    }

    /**
     * 后置异常通知
     * 定义一个名字，该名字用于匹配通知实现方法的一个参数名，当目标方法抛出异常返回后，将把目标方法抛出的异常传给通知方法；
     * throwing 限定了只有目标方法抛出的异常与通知方法相应参数异常类型时才能执行后置异常通知，否则不执行，
     * 对于throwing对应的通知方法参数为Throwable类型将匹配任何异常。
     *
     * @param joinPoint
     * @param exception
     */
    @AfterThrowing(value = "openApi()", throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint, Throwable exception) {
        PublicApiParams apiParams = publicApiParamThreadLocal.get();

        log.info(String.format("OpenApiAspect-%s-[异常].doAfterThrowingAdvice() - requestId:%s", apiParams.getAppid(),apiParams.getRequestId()));
    }

}
