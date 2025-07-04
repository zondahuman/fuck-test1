package com.fuck.test.basic.cache.guava;

import com.google.common.cache.*;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * https://blog.csdn.net/weixin_45427648/article/details/130396890
 */
public class GuavaLoadingCacheTest {

    @Test
    public void test1() {
        Cache<String, String> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)  // 数据写入10分钟后过期
                .build();
    }


    @Test
    public void test2() throws InterruptedException, ExecutionException {
        //CacheLoader的方式创建
        LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
                /*
                 加附加的功能
                 */
                //最大个数       //统计命中率       //移除通知
                .maximumSize(3).recordStats().removalListener(new RemovalListener<Object, Object>() {

                    @Override
                    public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
                        //移除的key 移除的原因
                        System.out.println("移除的key 移除的原因, " + removalNotification.getKey() + ":" + removalNotification.getCause());
                    }
                })
                .expireAfterWrite(1, TimeUnit.SECONDS)  // 数据写入10分钟后过期
                .build(new CacheLoader<String, Object>() {

                    //读取数据源
                    @Override
                    public Object load(String key) throws Exception {
                        return Constants.map.get(key);
                    }
                });
        System.out.println("----1----," + cache.get("1"));
        System.out.println("----2----," + cache.get("2"));
        //打印输出统计
        System.out.println(cache.stats().toString());
        Thread.sleep(5000);
        System.out.println(cache.stats().toString());
        System.out.println("----1----after," + cache.get("1"));
        System.out.println("----2----after," + cache.get("2"));
    }


    @Test
    public void test3() throws InterruptedException, ExecutionException {
        LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
                /*
                加附加的功能
                */
                //最大个数
                .maximumSize(3)
                .recordStats().removalListener(new RemovalListener<Object, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
                        //移除的key 移除的原因
                        System.out.println("移除的key 移除的原因, " + removalNotification.getKey() + ":" + removalNotification.getCause());
                    }
                })
                .expireAfterAccess(3, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Object>() {
                    //读取数据源
                    @Override
                    public Object load(String key) throws Exception {
                        return Constants.map.get(key);
                    }
                });
        //读取缓存中的1的数据 缓存有就读取 没有就返回null
        System.out.println("before---cache.get=" + cache.get("5"));
        System.out.println("before---cache.getIfPresent=" + cache.getIfPresent("5"));
        Thread.sleep(5000);
        System.out.println("after---cache.getIfPresent=" + cache.getIfPresent("5"));
    }


    @Test
    public void test4() throws InterruptedException {
        //缓存中的数据 如果3秒内没有访问则删除
        LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
                .maximumSize(3)
                .expireAfterAccess(3, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Object>() {
                    //读取数据源
                    @Override
                    public Object load(String key) throws Exception {
                        return Constants.map.get(key);
                    }
                });

        Thread.sleep(1000);
        //访问1 1被访问
        cache.getIfPresent("1");
        //歇了2.1秒
        Thread.sleep(2100);

    }

    @Test
    public void test5() throws InterruptedException, ExecutionException {
        LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
                /*
                加附加的功能
                */
                //最大个数
                .maximumSize(3)
                .recordStats().removalListener(new RemovalListener<Object, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
                        //移除的key 移除的原因
                        System.out.println("移除的key 移除的原因, " + removalNotification.getKey() + ":" + removalNotification.getCause());
                    }
                })
                .expireAfterAccess(3, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Object>() {
                    //读取数据源
                    @Override
                    public Object load(String key) throws Exception {
                        return Constants.map.get(key);
                    }
                });
        //读取缓存中的1的数据 缓存有就读取 没有就返回null
        System.out.println("before---cache.get=" + cache.get("5"));
        System.out.println("before---cache.getIfPresent=" + cache.getIfPresent("5"));
        Thread.sleep(5000);
        System.out.println("after---cache.get=" + cache.get("5"));
        System.out.println("after---cache.getIfPresent=" + cache.getIfPresent("5"));

    }


    @Test
    public void test6() {
        LoadingCache<String, Object> caches = CacheBuilder.newBuilder()
                .maximumSize(100)
                .refreshAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Object>() {
                    @Override
                    public Object load(String key) throws Exception {
                        return Constants.map.get(key);
                    }
                });
        try {
//            System.out.println(caches.get("key-zorro"));
            System.out.println(caches.get("1"));
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void test7() {
        ListeningExecutorService backgroundRefreshPools =
                MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(20));
        LoadingCache<String, Object> caches = CacheBuilder.newBuilder()
                .maximumSize(100)
                .refreshAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Object>() {
                    @Override
                    public Object load(String key) throws Exception {
                        return Constants.map.get(key);
                    }
                    @Override
                    public ListenableFuture<Object> reload(String key,
                                                           Object oldValue) throws Exception {
                        return backgroundRefreshPools.submit(new Callable<Object>() {
                            @Override
                            public Object call() throws Exception {
                                return Constants.map.get(key);
                            }
                        });
                    }
                });
        try {
//            System.out.println(caches.get("key-zorro"));
            System.out.println(caches.get("1"));
            System.out.println(caches.stats());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }


}
