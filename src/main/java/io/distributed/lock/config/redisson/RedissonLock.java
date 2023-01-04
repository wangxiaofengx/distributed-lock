package io.distributed.lock.config.redisson;

import io.distributed.lock.DistributedLockI;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * redis锁实现
 *
 * @author wangxiaofeng
 * @date 2023/1/4 9:58
 */
public class RedissonLock implements DistributedLockI {

    public static final String MODE = "redis";

    RedissonClient redissonClient;
    String path;
    RLock lock;

    public RedissonLock(RedissonClient redissonClient, String path) {
        this.redissonClient = redissonClient;
        this.path = path;
        this.lock = redissonClient.getLock(this.path);
    }

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void lock(long time, TimeUnit unit) {
        this.lock.lock(time, unit);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        lock.lockInterruptibly();
    }

    @Override
    public boolean tryLock() {
        return lock.tryLock();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return lock.tryLock(time, unit);
    }

    @Override
    public void unlock() {
        lock.unlock();
    }

    @Override
    public Condition newCondition() {
        return lock.newCondition();
    }

    public void destroy() {
        this.redissonClient.shutdown();
    }
}
