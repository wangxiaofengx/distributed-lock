package io.distributed.lock;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 分布式锁接口
 *
 * @author wangxiaofeng
 * @date 2023/1/4 10:15
 */
public interface DistributedLockI extends Lock {

    String PATH = "/" + UUID.randomUUID();

    void lock(long time, TimeUnit unit);
}
