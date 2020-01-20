package com.asong.cloud;

public class TestThreadLocal {
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        for (int i = 0; i <10 ; i++) {
            final int final1 = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadLocal.set(final1);
                    try {
                        Thread.sleep(1000);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    System.out.println("ThreadLocal: " + threadLocal.get());
                }
            }).start();
        }
    }
}
