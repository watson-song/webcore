package cn.watsontech.webhelper.common.aop.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Watson on 2019/12/16.
 */

@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Access {

    /**
     * 获取描述description
     *
     */
    @AliasFor("value")
    String description() default "";

    @AliasFor("description")
    String value() default "";

    /**
     * 日志级别
     * @return
     */
    String level() default "info";

    /**
     * 日志标签
     */
    String tag() default "";

    /**
     * 是否保存数据库， 默认为true
     * @return true 保存数据库， false只打log
     */
    String save() default "true";
}
