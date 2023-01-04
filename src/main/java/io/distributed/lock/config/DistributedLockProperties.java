package io.distributed.lock.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 分布式锁属性
 *
 * @author wangxiaofeng
 * @date 2023/1/4 9:57
 */
@Data
@Component
@ConfigurationProperties(prefix = "distributed.lock")
public class DistributedLockProperties {

    boolean enabled = true;

    String mode;
}
