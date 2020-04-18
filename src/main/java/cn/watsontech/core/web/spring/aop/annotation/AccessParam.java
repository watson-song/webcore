package cn.watsontech.core.web.spring.aop.annotation;

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
}
