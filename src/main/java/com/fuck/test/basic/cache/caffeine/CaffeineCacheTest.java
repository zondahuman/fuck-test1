package com.fuck.test.basic.cache.caffeine;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class CaffeineCacheTest {

    @Test
    public void test1(){
        Cache<String, String> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)  // 数据写入10分钟后过期
                .build();
    }









}
