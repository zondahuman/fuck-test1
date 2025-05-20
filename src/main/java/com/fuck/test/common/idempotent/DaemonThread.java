package com.fuck.test.common.idempotent;

public class DaemonThread {

    public static void main(String[] args) {
        Thread thread = new Thread();
        thread.setDaemon(true);
        thread.start();



    }



}
