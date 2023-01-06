package io.distributed.lock.config.redisson;

import io.distributed.lock.DistributedLockI;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
    public static final String DEFAULT_LOCK_NAME = DistributedLockI.DEFAULT_LOCK_NAME;

    Map<String, RLock> lockMap = new ConcurrentHashMap<>();

    RedissonClient redissonClient;
    String path;

    public RedissonLock(RedissonClient redissonClient, String path) {
        this.redissonClient = redissonClient;
        this.path = path;
    }

    @Override
    public void lock() {
        this.lock(DEFAULT_LOCK_NAME);
    }

    @Override
    public void lock(long time, TimeUnit unit) {
        this.lock(DEFAULT_LOCK_NAME, time, unit);
    }

    @Override
    public void lock(String name) {
        this.getLock(name).lock();
    }

    @Override
    public void lock(String name, long time, TimeUnit unit) {
        this.getLock(name).lock(time, unit);
    }

    @Override
    public void lockInterruptibly(String name) throws InterruptedException {
        this.getLock(name).lockInterruptibly();
    }

    @Override
    public boolean tryLock(String name) {
        return this.getLock(name).tryLock();
    }

    @Override
    public boolean tryLock(String name, long time, TimeUnit unit) throws InterruptedException {
        return this.getLock(name).tryLock(time, unit);
    }

    @Override
    public void unlock(String name) {
        this.getLock(name).unlock();
    }

    @Override
    public Condition newCondition(String name) {
        return this.getLock(name).newCondition();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        this.lockInterruptibly(DEFAULT_LOCK_NAME);
    }

    @Override
    public boolean tryLock() {
        return this.tryLock(DEFAULT_LOCK_NAME);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return this.tryLock(DEFAULT_LOCK_NAME, time, unit);
    }

    @Override
    public void unlock() {
        this.unlock(DEFAULT_LOCK_NAME);
    }

    @Override
    public Condition newCondition() {
        return this.newCondition(DEFAULT_LOCK_NAME);
    }

    public RLock getLock(String name) {
        RLock rLock = this.lockMap.get(name);
        if (rLock != null) {
            return rLock;
        }
        String path = this.path + "-" + name;
        return this.lockMap.computeIfAbsent(name, k -> redissonClient.getLock(path));
    }


    public void destroy() {
        this.redissonClient.shutdown();
    }
}
