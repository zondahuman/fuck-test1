package com.fuck.test.basic.cache.redlock;

import org.redisson.Redisson;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import java.util.concurrent.TimeUnit;

/**
 * https://www.zhihu.com/question/585348002/answer/119679560708
 */
public class RedLockDemo {
    public static void main(String[] args) {
        // 创建 Redisson 客户端配置
        Config config = new Config();
        config.useClusterServers()
                .addNodeAddress("redis://127.0.0.1:6379",
                        "redis://127.0.0.1:6380",
                        "redis://127.0.0.1:6381");
        // 创建 Redisson 客户端实例
        RedissonClient redissonClient = Redisson.create(config);

        RLock rLock = redissonClient.getLock("resource") ;
        // 创建 RedLock 对象
        RedissonRedLock redLock = new RedissonRedLock(rLock);
        try {
            // 尝试获取分布式锁，最多尝试 5 秒获取锁，并且锁的有效期为 5000 毫秒
            boolean lockAcquired = redLock.tryLock(5, 5000, TimeUnit.MILLISECONDS);
            if (lockAcquired) {
                // 加锁成功，执行业务代码...
            } else {
                System.out.println("Failed to acquire the lock!");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while acquiring the lock");
        } finally {
            // 无论是否成功获取到锁，在业务逻辑结束后都要释放锁
            if (redLock.isLocked()) {
                redLock.unlock();
            }
            // 关闭 Redisson 客户端连接
            redissonClient.shutdown();
        }
    }
}


