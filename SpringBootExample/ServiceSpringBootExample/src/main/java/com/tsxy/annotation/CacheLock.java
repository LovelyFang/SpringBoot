package com.tsxy.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 *
 * @Author Liu_df
 * @Date 2023/11/2 9:43
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface CacheLock {

    /**
     * SpEL 表达式
     * 如果不是 则为 key 本身
     */
    String[] key() default "";

    int cacheExpireTime() default 120;

    int LockExpireTime() default 120;

    String pre() default "";

}
