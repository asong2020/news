package com.asong.cloud;

import java.util.concurrent.atomic.AtomicInteger;

public class TestAtomicInteger {
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    public static void testWithAtomic(){
        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                        System.out.println(atomicInteger.incrementAndGet());

                }
            }).start();
        }
    }

    public static void main(String[] args) {
        testWithAtomic();
    }

}
