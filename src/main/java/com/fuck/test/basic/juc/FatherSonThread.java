package com.fuck.test.basic.juc;

public class FatherSonThread {

    public static InheritableThreadLocal<Integer> inheritableThreadLocal = new InheritableThreadLocal<>();

    public static void main(String[] args) {
        inheritableThreadLocal.set(0);
        new Thread1().start();
        System.out.println("main-inheritableThreadLocal="+inheritableThreadLocal.get());
        inheritableThreadLocal.set(inheritableThreadLocal.get()+1);
        System.out.println("over-inheritableThreadLocal="+inheritableThreadLocal.get());
    }

    static class Thread1 extends Thread{
        @Override
        public void run() {
            System.out.println("before-inheritableThreadLocal="+inheritableThreadLocal.get());
            inheritableThreadLocal.set(inheritableThreadLocal.get()+1);
            System.out.println("after-inheritableThreadLocal="+inheritableThreadLocal.get());
        }
    }


}
