package com.fuck.test.basic.cas;

import java.util.concurrent.atomic.AtomicStampedReference;

public class CasAbaTest {


    public static void main(String[] args) {
        final AtomicStampedReference<Integer> count = new AtomicStampedReference<>(5, 1);

        for (int i = 0; i < 2; i++) {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(10);
                } catch (Exception ignore) {
                }

                boolean re = count.compareAndSet(5, 10, 1, 2);
                System.out.println(Thread.currentThread().getName() + "[recharge] compareAndSet " + re);
            });
            thread.start();
        }

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(10);
            } catch (Exception ignore) {
            }
            boolean re = count.compareAndSet(10, 5, count.getStamp(), count.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "[consume] compareAndSet " + re);
        });
        thread.start();
    }




}
