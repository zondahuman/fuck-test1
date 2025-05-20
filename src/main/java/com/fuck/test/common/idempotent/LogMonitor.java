package com.fuck.test.common.idempotent;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.concurrent.TimeUnit;

/**
 *  https://blog.csdn.net/Rcain_R/article/details/138769984
 */
public class LogMonitor implements Runnable {

    private volatile boolean running = true;

    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        while (running) {
            long usedMemory = memoryBean.getHeapMemoryUsage().getUsed();
            System.out.printf("Current heap memory usage: %d bytes%n", usedMemory);

            try {
                TimeUnit.SECONDS.sleep(5); // 每5秒检查一次
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 保留中断状态
                break;
            }
        }
        System.out.println("Log Monitor thread is stopping.");
    }

    public static void main(String[] args) {
        Thread logMonitorThread = new Thread(new LogMonitor());
        logMonitorThread.setDaemon(true); // 设置为守护线程
        logMonitorThread.start();

        // 主线程逻辑
        for (int i = 0; i < 10; i++) {
            System.out.println("Main thread working...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Main thread finished.");
    }
}

