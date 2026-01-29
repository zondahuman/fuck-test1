package com.fuck.test1.basic;


import org.apache.commons.collections4.bloomfilter.BloomFilter;
import org.apache.commons.collections4.bloomfilter.Shape;
import org.apache.commons.collections4.bloomfilter.SimpleBloomFilter;
import org.junit.Test;

public class ApacheBloomFilterTest {



    @Test
    public void test3(){
        // 创建一个形状为 (1000, 3) 的 BloomFilter，表示有 1000 个位，使用 3 个哈希函数
        Shape shape = Shape.fromNM(10, 8);
        BloomFilter bloomFilter = new SimpleBloomFilter(shape);
    }

    @Test
    public void test4(){
        // 创建一个形状为 (1000, 3) 的 BloomFilter，表示有 1000 个位，使用 3 个哈希函数
        Shape shape = Shape.fromNM(10, 8);
        SimpleBloomFilter bloomFilter = new SimpleBloomFilter(shape);
//        bloomFilter.contains();
    }


}
