package io.distributed.lock.config.curator;

import io.distributed.lock.DistributedLockConstants;
import io.distributed.lock.DistributedLockI;
import io.distributed.lock.exception.DistributedLockException;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 集成zookeeper锁
 *
 * @author wangxiaofeng
 * @date 2023/1/4 9:55
 */
@Configuration
public class CuratorConfigure {

    @Bean
    @ConditionalOnBean(CuratorProperties.class)
    @ConditionalOnMissingBean(CuratorFramework.class)
    @ConditionalOnProperty(prefix = DistributedLockConstants.PREFIX, name = "mode", havingValue = CuratorLock.MODE)
    public CuratorFramework curatorFramework(CuratorProperties curatorProperties) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(curatorProperties.elapsedTimeMs, curatorProperties.retryCount);
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(curatorProperties.connectString).
                sessionTimeoutMs(curatorProperties.sessionTimeoutMs).
                connectionTimeoutMs(curatorProperties.connectionTimeoutMs).
                retryPolicy(retryPolicy).
                build();
        client.start();
        if (curatorProperties.startBlock) {
            try {
                client.blockUntilConnected(curatorProperties.connectionTimeoutMs, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new DistributedLockException(e);
            }
        }
        return client;
    }

    @Bean(destroyMethod = "destroy")
    @ConditionalOnBean(value = {CuratorFramework.class, CuratorProperties.class})
    @ConditionalOnProperty(prefix = DistributedLockConstants.PREFIX, name = "mode", havingValue = CuratorLock.MODE)
    public CuratorLock curatorLock(CuratorFramework client, CuratorProperties curatorProperties) {
        CuratorLock curatorLock = new CuratorLock(client, DistributedLockI.PATH + "-" + curatorProperties.path);
        return curatorLock;
    }
}
