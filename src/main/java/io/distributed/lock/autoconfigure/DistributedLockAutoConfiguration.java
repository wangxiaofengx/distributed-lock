package io.distributed.lock.autoconfigure;

import io.distributed.lock.DistributedLockConstants;
import io.distributed.lock.annotation.DistributedLockScanner;
import io.distributed.lock.DistributedLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * spring boot 自动装配类
 *
 * @author wangxiaofeng
 * @date 2023/1/4 9:55
 */
@ComponentScan(basePackages = "io.distributed.lock.config")
@ConditionalOnProperty(prefix = DistributedLockConstants.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@Configuration
public class DistributedLockAutoConfiguration {

    @Bean
    public DistributedLockScanner distributedLockScanner(DistributedLock distributedLock) {
        DistributedLockScanner distributedLockScanner = new DistributedLockScanner(distributedLock);
        return distributedLockScanner;
    }
}
