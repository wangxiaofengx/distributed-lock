package io.distributed.lock;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 分布式锁接口
 *
 * @author wangxiaofeng
 * @date 2023/1/4 10:15
 */
public interface DistributedLockI extends Lock {

    String PATH = "/" + DistributedLockI.class.getName();

    String DEFAULT_LOCK_NAME = "default";

    void lock(long time, TimeUnit unit);

    void lock(String name);

    void lock(String name, long time, TimeUnit unit);

    void lockInterruptibly(String name) throws InterruptedException;

    boolean tryLock(String name);

    boolean tryLock(String name, long time, TimeUnit unit) throws InterruptedException;

    void unlock(String name);

    Condition newCondition(String name);
}
