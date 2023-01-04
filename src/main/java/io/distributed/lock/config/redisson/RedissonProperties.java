package io.distributed.lock.config.redisson;

import io.distributed.lock.DistributedLockConstants;
import io.distributed.lock.DistributedLockI;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redis锁属性
 *
 * @author wangxiaofeng
 * @date 2023/1/4 9:59
 */
@Data
@Component
@ConfigurationProperties(prefix = DistributedLockConstants.PREFIX + ".redis")
public class RedissonProperties {

    Single single;

    String path = DistributedLockI.PATH;

    @Data
    public static class Single {
        String address;
    }
}
