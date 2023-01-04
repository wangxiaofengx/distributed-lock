package io.distributed.lock.annotation;

import io.distributed.util.SpringProxyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.concurrent.locks.Lock;

/**
 * 分布式锁扫描类
 *
 * @author wangxiaofeng
 * @date 2023/1/3 15:39
 */
public class DistributedLockScanner extends AbstractAutoProxyCreator
        implements InitializingBean, ApplicationContextAware,
        DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLockScanner.class);


    private ApplicationContext applicationContext;

    DistributedLockInterceptor distributedLockInterceptor;

    Lock lock;

    public DistributedLockScanner(Lock lock) {
        setProxyTargetClass(true);
        this.lock = lock;
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {

        return new Object[]{distributedLockInterceptor};
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {

        try {
            Class<?> serviceInterface = SpringProxyUtils.findTargetClass(bean);
            Class<?>[] interfacesIfJdk = SpringProxyUtils.findInterfaces(bean);

            if (!existsAnnotation(new Class[]{serviceInterface})
                    && !existsAnnotation(interfacesIfJdk)) {
                return bean;
            }

            distributedLockInterceptor = new DistributedLockInterceptor(this.lock);

            if (!AopUtils.isAopProxy(bean)) {
                bean = super.wrapIfNecessary(bean, beanName, cacheKey);
            } else {
                AdvisedSupport advised = SpringProxyUtils.getAdvisedSupport(bean);
                Advisor[] advisor = buildAdvisors(beanName, getAdvicesAndAdvisorsForBean(null, null, null));
                for (Advisor avr : advisor) {
                    advised.addAdvisor(0, avr);
                }
            }
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.setBeanFactory(applicationContext);
    }

    private boolean existsAnnotation(Class<?>[] classes) {
        for (Class<?> clazz : classes) {
            if (clazz == null) {
                continue;
            }
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);
                if (distributedLock != null) {
                    return true;
                }
            }
        }
        return false;
    }
}
