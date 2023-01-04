# distributed-lock
[![Apache License 2](https://img.shields.io/badge/license-ASF2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)
* 支持zookeeper锁和redis锁
* 内部集成了curator框架和redisson框架
* 开箱即用，无业务代码侵入
* 支持spring boot


## Quick start
#### Maven
```
    <dependency>
       <groupId>org.distributed.lock.spring.boot</groupId>
       <artifactId>distributed-lock-spring-boot-starter</artifactId>
       <version>1.0.0</version>
    </dependency>
```

#### application.yml

```
distributed:
  lock:
    mode: zookeeper
    zookeeper:
      connectString: 127.0.0.1:2181
      sessionTimeoutMs: 60000
      connectionTimeoutMs: 5000
      elapsedTimeMs: 5000
      maxRetries: 5
    redis:
      single:
        address: 127.0.0.1:6379
```

#### Java annotation mode
```
@DistributedLock
public void business(){
    // business code
}
```

#### Java code mode
```
@Autowired
private DistributedLockI distributedLockI

public void business(){
    distributedLockI.lock();
    try {
         // business code
    } finally {
        distributedLockI.unlock();
    }
}
```