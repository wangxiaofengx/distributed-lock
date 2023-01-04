package io.distributed.lock.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁注解
 *
 * @author wangxiaofeng
 * @date 2023/1/3 15:37
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface DistributedLock {

    /**
     * 锁超时时间
     * @return
     */
    long timeout() default 0;

    /**
     * 超时时间单位
     * @return
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
