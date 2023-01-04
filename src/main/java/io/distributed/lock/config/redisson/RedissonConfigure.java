package io.distributed.lock.config.redisson;

import io.distributed.lock.DistributedLockConstants;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redis锁配置
 *
 * @author wangxiaofeng
 * @date 2023/1/4 9:57
 */
@Configuration
public class RedissonConfigure {

    @Bean(destroyMethod = "destroy")
    @ConditionalOnBean(value = RedissonProperties.class)
    @ConditionalOnProperty(prefix = DistributedLockConstants.PREFIX, name = "mode", havingValue = RedissonLock.MODE)
    public RedissonLock redissonClient(RedissonProperties redissonProperties) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + redissonProperties.single.address);
        RedissonClient redissonClient = Redisson.create();
        RedissonLock redissonLock = new RedissonLock(redissonClient, redissonProperties.path);
        return redissonLock;
    }
}
