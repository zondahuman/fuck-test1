package com.fuck.test.common.scene;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * https://blog.csdn.net/weixin_45433817/article/details/130773405
 */
public class JedisBloomFilter {


    @Test
    public void testAPacheCommonBloomFilter() {
        Jedis jedis = new Jedis("locahost");
//        jedis.brCreate("myfilter",100,0.01);
//        jedis.bfAdd("myfilter","哈哈");
//        jedis.bfAdd("myfilter","嘿嘿");
//        jedis.bfAdd("myfilter","呵呵");

        System.out.println(jedis.exists("myfilter","哈哈"));//true
        System.out.println(jedis.exists("myfiter","嘻嘻"));//false

    }

}
