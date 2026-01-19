package com.fuck.test1.basic;

import org.junit.Test;

import java.util.BitSet;

public class BitMapTest {

    @Test
    public void test1(){
        BitSet bitMap = new BitSet(100);
        bitMap.set(5);
        bitMap.set(10);
        bitMap.set(15);
        System.out.println("bitMap.set(5)="+bitMap.get(5));
        System.out.println("bitMap.set(10)="+bitMap.get(10));
        System.out.println("bitMap.set(15)="+bitMap.get(15));
        bitMap.clear(15);
        System.out.println("bitMap.set(15)="+bitMap.get(15));
    }

    /**
     * 2. 关键操作（基于位运算）
     * BitMap的核心操作通过位运算实现，以下是具体逻辑（以Java的BitSet为例，基于long数组，每long占64位）：
     * 添加元素（set）：计算元素对应的数组下标和位偏移量，将对应位设置为1。
     * 公式：arrayIndex = num >> 6（等价于num / 64）；bitOffset = num & 0x3F（等价于num % 64）；bits[arrayIndex] |= 1L << bitOffset。
     * 查询元素（get）：检查对应位是否为1。
     * 公式：return (bits[arrayIndex] & (1L << bitOffset)) != 0。
     * 删除元素（clear）：将对应位设置为0。
     * 公式：bits[arrayIndex] &= ~(1L << bitOffset)。
     */
    @Test
    public void test2(){
        Integer num1 = 128;
        Integer num2 = num1 >> 4; //位运算向右除以2的四次方
        System.out.println("num1="+num1+",num1 >> 4, num2="+num2);

        Integer num3 = num1 << 4;//位运算向左乘以2的四次方
        System.out.println("num1="+num1+",num1 << 4, num3="+num3);

    }



//    words[wordIndex] |= (1L << bitIndex);
    @Test
    public void test3(){
        Integer num1 = 128;
        Integer num2 = num1 >> 4;
        System.out.println("num1="+num1+", num2="+num2);


    }


}
