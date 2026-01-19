package com.fuck.test1.common;

/**
 * https://cloud.tencent.com/developer/article/2563879
 * Java Bitmap 去重：原理、代码实现与应用
 * 这个方法的逻辑很简单：先算出数字在哪个字节（除以8），再算出在字节的哪一位（取余8），最后用位运算检查这一位是不是1。
 * 用生活例子来理解：
 * 把Bitmap想象成一排开关，每个开关代表一个数字：
 *
 *     开关亮着（1）= 数字存在
 *     开关关着（0）= 数字不存在
 */
public class BitmapDeduplication {
    // 定义 Bitmap 的大小，这里假设处理的数据范围在 0 到 999999
    private static final int BITMAP_SIZE = 1000000;
    // 字节数组用于存储 Bitmap 数据
    private byte[] bitmap = new byte[BITMAP_SIZE / 8];



    public boolean contains(int value) {
        // 计算元素对应的字节索引
        int byteIndex = value / 8;
        // 计算元素在字节中的位索引
        int bitIndex = value % 8;
        // 通过位运算检查该位是否被设置
        return (bitmap[byteIndex] & (1 << bitIndex))!= 0;
    }





}
