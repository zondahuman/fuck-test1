package com.fuck.test.common.scene;

import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import redis.clients.jedis.Jedis;

/**
 * https://blog.csdn.net/weixin_45433817/article/details/130773405
 */
public class RedissionBloomFilter {


    @Test
    public void testAPacheCommonBloomFilter() {
        Config config=new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");

        RedissonClient redissonClient = Redisson.create(config);
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("myfilter");
        bloomFilter.tryInit(100,0.01);
        //插入元素
        bloomFilter.add("哈哈");
        bloomFilter.add("嘿嘿");
        bloomFilter.add("嘻嘻");

        //判断元素是否存在
        System.out.println(bloomFilter.contains("哈哈"));//true
        System.out.println(bloomFilter.contains("啊啊"));//false
        redissonClient.shutdown();


    }

}
