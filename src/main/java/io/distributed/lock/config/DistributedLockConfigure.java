package io.distributed.lock.config;

import io.distributed.lock.DistributedLock;
import io.distributed.lock.config.curator.CuratorLock;
import io.distributed.lock.config.redisson.RedissonLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式锁配置
 *
 * @author wangxiaofeng
 * @date 2023/1/4 9:56
 */
@Configuration
public class DistributedLockConfigure {

    @Bean
    @ConditionalOnBean(value = DistributedLockProperties.class)
    public DistributedLock distributedLockService(DistributedLockProperties distributedLockProperties,
                                                  @Autowired(required = false) RedissonLock redissonLock,
                                                  @Autowired(required = false) CuratorLock curatorLock) {
        DistributedLock distributedLock = new DistributedLock();
        distributedLock.setMode(distributedLockProperties.getMode());
        distributedLock.setRedisLock(redissonLock);
        distributedLock.setZookeeperLock(curatorLock);
        return distributedLock;
    }
}
