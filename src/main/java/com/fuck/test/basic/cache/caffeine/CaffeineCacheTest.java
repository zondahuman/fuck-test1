package com.fuck.test.basic.cache.caffeine;


import com.fuck.test.basic.cache.guava.Constants;
import com.github.benmanes.caffeine.cache.*;
import org.jspecify.annotations.Nullable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class CaffeineCacheTest {

    @Test
    public void test1() throws InterruptedException {
        RemovalListener<String, String> removalListener = (key, value, cause) -> {
            System.out.println("Key: " + key + ", Value: " + value + ", Cause: " + cause);
        };

        Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(10_000)          // 最大条目数
                .expireAfterWrite(3, TimeUnit.MINUTES) // 写入后过期
                .expireAfterAccess(5, TimeUnit.MINUTES) // 访问后过期
                .refreshAfterWrite(1, TimeUnit.MINUTES)  // 自动刷新（非阻塞）
                .weakKeys()                   // Key弱引用（GC敏感）
                .weakValues()                 // Value弱引用
                .recordStats()                // 开启命中率统计
                .removalListener(removalListener) // 淘汰监听
                .build();

        cache.put("key1", "value1");
        cache.put("key2", "value2");
        System.out.println("before------cache.getIfPresent=" + cache.getIfPresent("key1"));
        Thread.sleep(8000);
        System.out.println("after------cache.getIfPresent=" + cache.getIfPresent("key1"));


    }


    @Test
    public void test2() throws InterruptedException {
        RemovalListener<String, String> removalListener = (key, value, cause) -> {
            System.out.println("Key: " + key + ", Value: " + value + ", Cause: " + cause);
        };

        Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100)
                .removalListener(removalListener)
                .build();

        cache.put("key1", "value1");
        cache.put("key2", "value2");

        // 等待一段时间，让缓存项过期
        Thread.sleep(60 * 1000);

        // 手动触发缓存清理
        cache.cleanUp();
    }


    @Test
    public void test3() throws InterruptedException {
        Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100)
                .removalListener(new RemovalListener<String, String>() {
                    @Override
                    public void onRemoval(@Nullable String key, @Nullable String value, RemovalCause cause) {
                        System.out.println("Key: " + key + ", Value: " + value + ", Cause: " + cause);
                    }
                })
                .build();

        cache.put("key1", "value1");
        cache.put("key2", "value2");

        // 等待一段时间，让缓存项过期
        Thread.sleep(60 * 1000);

        // 手动触发缓存清理
        cache.cleanUp();
    }


    @Test
    public void test4() throws InterruptedException {
        RemovalListener<String, String> removalListener = (key, value, cause) -> {
            System.out.println("Key: " + key + ", Value: " + value + ", Cause: " + cause);
        };

        LoadingCache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(10_000)          // 最大条目数
                .expireAfterWrite(3, TimeUnit.MINUTES) // 写入后过期
                .expireAfterAccess(5, TimeUnit.MINUTES) // 访问后过期
                .refreshAfterWrite(1, TimeUnit.MINUTES)  // 自动刷新（非阻塞）
                .weakKeys()                   // Key弱引用（GC敏感）
                .weakValues()                 // Value弱引用
                .recordStats()                // 开启命中率统计
                .removalListener(new RemovalListener<String, String>() {
                    @Override
                    public void onRemoval(@Nullable String key, @Nullable String value, RemovalCause cause) {
                        System.out.println("Key: " + key + ", Value: " + value + ", Cause: " + cause);
                    }
                }) // 淘汰监听
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return Constants.map.get(key);
                    }
                });

        cache.put("key1", "value1");
        cache.put("key2", "value2");
        System.out.println("before------cache.get=" + cache.get("key1"));
        System.out.println("before------cache.getIfPresent=" + cache.getIfPresent("key1"));
        System.out.println("before------cache.stats()=" + cache.stats());
        Thread.sleep(8000);
        System.out.println("after------cache.get=" + cache.get("key1"));
        System.out.println("after------cache.getIfPresent=" + cache.getIfPresent("key1"));
        System.out.println("after------cache.stats()=" + cache.stats());


    }


}
