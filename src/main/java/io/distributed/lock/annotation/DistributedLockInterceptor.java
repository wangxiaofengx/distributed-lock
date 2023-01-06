package io.distributed.lock.annotation;

import io.distributed.lock.DistributedLockI;
import io.distributed.lock.exception.DistributedLockTimeoutException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

/**
 * 分布式锁拦截器
 *
 * @author wangxiaofeng
 * @date 2023/1/4 9:54
 */
class DistributedLockInterceptor implements MethodInterceptor {

    DistributedLockI lock;

    public DistributedLockInterceptor(DistributedLockI lock) {
        this.lock = lock;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        Class<?> targetClass = methodInvocation.getThis() != null ? AopUtils.getTargetClass(methodInvocation.getThis()) : null;
        Method specificMethod = ClassUtils.getMostSpecificMethod(methodInvocation.getMethod(), targetClass);
        if (specificMethod != null && !specificMethod.getDeclaringClass().equals(Object.class)) {
            final Method method = BridgeMethodResolver.findBridgedMethod(specificMethod);
            final DistributedLock distributedLock = getAnnotation(method, targetClass, DistributedLock.class);
            if (distributedLock != null) {
                String name = distributedLock.name();
                if (distributedLock.timeout() > 0) {
                    boolean lockSuccess = this.lock.tryLock(name, distributedLock.timeout(), distributedLock.unit());
                    if (!lockSuccess) {
                        throw new DistributedLockTimeoutException("distributed lock acquire timeout");
                    }
                } else {
                    this.lock.lock(name);
                }

                try {
                    Object proceed = methodInvocation.proceed();
                    return proceed;
                } finally {
                    this.lock.unlock(name);
                }
            }
        }
        return methodInvocation.proceed();
    }

    public <T extends Annotation> T getAnnotation(Method method, Class<?> targetClass, Class<T> annotationClass) {
        return Optional.ofNullable(method).map(m -> m.getAnnotation(annotationClass))
                .orElse(Optional.ofNullable(targetClass).map(t -> t.getAnnotation(annotationClass)).orElse(null));
    }
}
