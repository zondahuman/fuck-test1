package com.fuck.test.basic.cache.guava;

import com.google.common.cache.*;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class CacheBuilderTest {

    @Test
    public void test1(){
        Cache<String, String> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)  // 数据写入10分钟后过期
                .build();
    }


    @Test
    public void test2(){
        //CacheLoader的方式创建
        LoadingCache<String,Object> cache= CacheBuilder.newBuilder()
                /*
                 加附加的功能
                 */
                //最大个数       //统计命中率       //移除通知
                .maximumSize(3).recordStats().removalListener(new RemovalListener<Object, Object>() {

                    @Override
                    public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
                        //移除的key 移除的原因
                        System.out.println(removalNotification.getKey()+":"+removalNotification.getCause());
                    }
                })
                .build(new CacheLoader<String, Object>() {

                    //读取数据源
                    @Override
                    public Object load(String key) throws Exception {
                        return Constants.map.get(key);
                    }
                });

        //打印输出统计
        System.out.println(cache.stats().toString());

    }






}
