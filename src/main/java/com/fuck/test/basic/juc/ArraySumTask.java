package com.fuck.test.basic.juc;

import java.util.concurrent.RecursiveTask;

public class ArraySumTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 1000; // 阈值，当数组长度小于等于该值时，采用普通方式求和
    private final int[] array;
    private final int low, high;

    public ArraySumTask(int[] array) {
        this(array, 0, array.length);
    }

    private ArraySumTask(int[] array, int low, int high) {
        this.array = array;
        this.low = low;
        this.high = high;
    }

    @Override
    protected Long compute() {
        if (high - low <= THRESHOLD) {
            // 数组长度小于等于阈值，采用普通方式求和
            long sum = 0;
            for (int i = low; i < high; i++) {
                sum += array[i];
            }
            return sum;
        } else {
            // 数组长度大于阈值，拆分任务并递归处理
            int mid = low + (high - low) / 2;
            ArraySumTask leftTask = new ArraySumTask(array, low, mid);
            ArraySumTask rightTask = new ArraySumTask(array, mid, high);
            leftTask.fork(); // 拆分左半部分任务并异步执行
            rightTask.fork(); // 拆分右半部分任务并异步执行
            return leftTask.join() + rightTask.join(); // 等待左右两部分任务处理完成并合并结果
        }
    }
}


