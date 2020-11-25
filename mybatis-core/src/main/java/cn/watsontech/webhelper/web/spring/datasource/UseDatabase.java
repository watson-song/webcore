package cn.watsontech.webhelper.web.spring.datasource;

import java.lang.annotation.*;

/**
 * Copyright to watsontech
 * Created by Watson on 2019/12/24.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface UseDatabase {

    /**
     * 数据库类型, 默认使用 slave
     */
    DBTypeEnum dbType() default DBTypeEnum.SLAVE;

    /**
     * 注解ID
     */
    String id() default "";
}
