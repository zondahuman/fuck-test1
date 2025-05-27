package com.fuck.test.basic.juc;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinPoolExample {

    public static void main(String[] args) throws Exception {
        int[] array = new int[1000000]; // 创建一个包含100万个元素的数组
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(100); // 随机生成数组元素的值
        }
        ForkJoinPool pool = new ForkJoinPool(); // 创建ForkJoinPool对象（默认使用处理器的核心数作为线程池大小）
        ArraySumTask task = new ArraySumTask(array); // 创建求和任务对象并传入数组作为参数（这里也可以传入数组的一部分作为参数来实现更细粒度的拆分）
        Long sum = pool.invoke(task); // 提交任务并等待处理完成（也可以使用submit方法提交任务并获取一个Future对象来异步获取结果）
        System.out.println("Sum: " + sum); // 输出结果（这里应该输出数组中所有元素的和）
        pool.shutdown(); // 关闭ForkJoinPool（释放资源）
    }


}
