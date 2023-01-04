package io.distributed.lock.config.curator;

import io.distributed.lock.DistributedLockConstants;
import io.distributed.lock.DistributedLockI;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * zookeeper锁属性
 *
 * @author wangxiaofeng
 * @date 2023/1/4 9:58
 */
@Data
@Component
@ConfigurationProperties(prefix = DistributedLockConstants.PREFIX + ".zookeeper")
public class CuratorProperties {

    String connectString;

    int sessionTimeoutMs;

    int connectionTimeoutMs;

    int elapsedTimeMs;

    int retryCount;

    boolean startBlock = false;

    String path = DistributedLockI.PATH;
}
