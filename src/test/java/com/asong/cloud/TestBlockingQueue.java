package com.asong.cloud;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class TestBlockingQueue {

    public static void main(String[] args) {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        new Thread(new Producer(queue)).start();
        new Thread(new Cosumer(queue),"Consumer1").start();
        new Thread(new Cosumer(queue),"Consumer2").start();
    }

}

/**
 * 模拟一个生产者
 */
class Producer implements Runnable{
    private BlockingQueue<String> q;

    public Producer(BlockingQueue<String> q)
    {
        this.q = q;
    }
    @Override
    public void run() {
        try{
            for (int i = 0; i <10 ; i++) {
                Thread.sleep(1000);
                q.put(String.valueOf(i));
            }
        }catch (Exception E)
        {
            E.printStackTrace();
        }
    }
}

/**
 * 模拟一个消费者
 */
class Cosumer implements Runnable{

    private BlockingQueue<String> q;
    public Cosumer(BlockingQueue<String> q)
    {
        this.q = q;
    }
    @Override
    public void run() {
        try{
            while(true)
            {
                System.out.println(Thread.currentThread().getName() + ":" + q.take());
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}