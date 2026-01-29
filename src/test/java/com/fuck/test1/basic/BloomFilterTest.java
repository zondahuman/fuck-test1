package com.fuck.test1.basic;


import org.apache.commons.collections4.bloomfilter.BloomFilter;
import org.apache.commons.collections4.bloomfilter.BloomFilterExtractor;
import org.apache.commons.collections4.bloomfilter.Shape;
import org.apache.commons.collections4.bloomfilter.SimpleBloomFilter;

public class BloomFilterTest {



    public void test3(){
        // 创建一个形状为 (1000, 3) 的 BloomFilter，表示有 1000 个位，使用 3 个哈希函数
        Shape shape = Shape.fromNM(10, 8);
        BloomFilter bloomFilter = new SimpleBloomFilter(shape);


    }
}
