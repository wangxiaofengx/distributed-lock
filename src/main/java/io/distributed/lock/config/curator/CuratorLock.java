package io.distributed.lock.config.curator;

import io.distributed.lock.DistributedLockI;
import io.distributed.lock.exception.DistributedLockException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * zookeeper锁实现
 *
 * @author wangxiaofeng
 * @date 2023/1/4 9:57
 */
public class CuratorLock implements DistributedLockI {

    public static final String MODE = "zookeeper";
    public static final String DEFAULT_LOCK_NAME = DistributedLockI.DEFAULT_LOCK_NAME;

    Map<String, InterProcessLock> lockMap = new ConcurrentHashMap<>();

    CuratorFramework curatorFramework;
    String path;

    public CuratorLock(CuratorFramework curatorFramework, String path) {
        this.curatorFramework = curatorFramework;
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
        try {
            this.getLock(name).acquire();
        } catch (Exception e) {
            throw new DistributedLockException(e);
        }
    }

    @Override
    public void lock(String name, long time, TimeUnit unit) {
        throw new UnsupportedOperationException("zookeeper mode nonsupport lock timeout");
    }

    @Override
    public void lockInterruptibly(String name) throws InterruptedException {
        throw new UnsupportedOperationException("zookeeper mode nonsupport lockInterruptibly");
    }

    @Override
    public boolean tryLock(String name) {
        throw new UnsupportedOperationException("zookeeper mode nonsupport tryLock(),Please call tryLock(long time, TimeUnit unit)");
    }

    @Override
    public boolean tryLock(String name, long time, TimeUnit unit) throws InterruptedException {
        try {
            return this.getLock(name).acquire(time, unit);
        } catch (Exception e) {
            throw new DistributedLockException(e);
        }
    }

    @Override
    public void unlock(String name) {
        try {
            this.getLock(name).release();
        } catch (Exception e) {
            throw new DistributedLockException(e);
        }
    }

    @Override
    public Condition newCondition(String name) {
        throw new UnsupportedOperationException("zookeeper mode nonsupport newCondition");
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

    public InterProcessLock getLock(String name) {
        InterProcessLock rLock = this.lockMap.get(name);
        if (rLock != null) {
            return rLock;
        }
        String path = this.path + "-" + name;
        return this.lockMap.computeIfAbsent(name, k -> new InterProcessMutex(curatorFramework, path));
    }

    public void destroy() {
        this.curatorFramework.close();
    }
}
