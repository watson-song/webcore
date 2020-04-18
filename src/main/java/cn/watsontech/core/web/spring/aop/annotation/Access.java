package cn.watsontech.core.web.spring.aop.annotation;

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
     * @return
     * @SystemControllerLog(description = "登入系统")
     */
    @AliasFor("value")
    String description() default "";

    @AliasFor("description")
    String value() default "";

}
