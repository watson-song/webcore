package cn.watsontech.webhelper.common.aop.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 访问记录参数
 * Created by Watson on 2020/02/9.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface AccessParam {

    /**
     * 参数属性
     */
    @AliasFor("value")
    String[] fields() default {};

    @AliasFor("fields")
    String[] value() default {};

    /**
     * 日志分组关键字，可以是商家ID，个人ID等
     */
    String groupIdField() default "";

    /**
     * 是否是分组id
     * @return
     */
    boolean isGroupId() default false;
}
