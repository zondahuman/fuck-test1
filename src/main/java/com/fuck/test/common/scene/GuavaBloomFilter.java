package com.fuck.test.common.scene;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.Test;

import java.nio.charset.Charset;

public class GuavaBloomFilter {

    public static void main(String[] args) {
        BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), 100, 0.01);
        bloomFilter.put("test1");
        bloomFilter.put("zhangsan");
        bloomFilter.put("sun");
        Boolean flag1 = bloomFilter.mightContain("test1");
        Boolean flag2 = bloomFilter.mightContain("zhangsan");
        Boolean flag3 = bloomFilter.mightContain("sun");
        Boolean flag4 = bloomFilter.mightContain("iu12");
        System.out.println("flag1=" + flag1 + ",flag2=" + flag2 + ",flag3=" + flag3 + ",flag4=" + flag4);

    }



}
