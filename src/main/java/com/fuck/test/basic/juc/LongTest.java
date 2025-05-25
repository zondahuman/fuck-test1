package com.fuck.test.basic.juc;

import org.junit.Test;

import java.util.concurrent.atomic.LongAdder;

public class LongTest {

    @Test
    public void test1() {
        LongAdder longAddr = new LongAdder();
        longAddr.add(2);
        System.out.println("longAddr="+longAddr);
    }





}
