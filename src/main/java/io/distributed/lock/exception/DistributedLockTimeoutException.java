package io.distributed.lock.exception;

/**
 * 分布式锁超时异常
 *
 * @author wangxiaofeng
 * @date 2023/1/4 14:54
 */
public class DistributedLockTimeoutException extends DistributedLockException {
    public DistributedLockTimeoutException() {
        super();
    }

    public DistributedLockTimeoutException(String message) {
        super(message);
    }

    public DistributedLockTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public DistributedLockTimeoutException(Throwable cause) {
        super(cause);
    }

    protected DistributedLockTimeoutException(String message, Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
