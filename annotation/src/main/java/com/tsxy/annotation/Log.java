package com.tsxy.annotation;

import com.tsxy.constant.BusinessTypeEnum;
import java.lang.annotation.*;

/**
 * @Author Liu_df
 * @Date 2022/10/29 19:30
 */
@Target(ElementType.METHOD) // 注解只能用于方法
@Retention(RetentionPolicy.RUNTIME) // 修饰注解的生命周期
@Documented
public @interface Log {

    String value() default "";
    /**
     * 模块
     */
    String title() default "测试模块";

    /**
     * 功能
     */
    BusinessTypeEnum businessType() default BusinessTypeEnum.OTHER;
}
