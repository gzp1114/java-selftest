package org.selftest.interview.multithreading.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 竞争条件实现
 *
 * Java多线程中，当两个或以上的线程对同一个数据进行操作的时候，
 * 可能会产生“竞争条件”的现象。这种现象产生的根本原因是因为多个线程在对同一个数据进行操作，
 * 此时对该数据的操作是非“原子化”的，可能前一个线程对数据的操作还没有结束，
 * 后一个线程又开始对同样的数据开始进行操作，这就可能会造成数据结果的变化未知
 *
 *
 */
@Slf4j
public class CompetitiveConditionsApplication {

    static Object o1 = new Object();
    static Object o2 = new Object();

    public static void main(String[] args) {
//        MyThread t = new MyThread();

        MyLockThread t = new MyLockThread();


        /**
         * 两个线程是在对同一个对象进行操作
         */
        Thread ta = new Thread(t, "Thread-A");
        Thread tb = new Thread(t, "Thread-B");
        ta.start();
        tb.start();
    }

    /**
     * 出现竞争条件
     *
     * Thread-A → a = 8
     Thread-B → a = 7
     Thread-A → a = 6
     Thread-B → a = 5
     Thread-A → a = 4
     Thread-B → a = 3
     Thread-A → a = 2
     Thread-B → a = 1
     Thread-A → a = 0
     Thread-B → a = 0
     *
     * 在线程A对数据进行了操作之后，他还没有来得及数据进行下一次的操作，此时线程B也对数据进行了操作，导致数据a一次性被减了两次，
     * 以至于a为9的时候的值根本没有打印出来，a为0的时候却被打印了两次
     */
    static class MyThread implements Runnable {
        int a = 10;
        // synchronized 关键字对方法进行加锁
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                a -= 1;
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {}
                System.out.println(Thread.currentThread().getName() + " → a = " + a);
            }
        }
    }

    /**
     * 线程枷锁的方式
     *
     * 在线程对数据进行操作之前先给此操作加一把锁，那么在此线程对数据进行操作的时候，其他的线程无法对此数据进行操作，只能“阻塞”在一边等待当前线程对数据操作结束后再对数据进行下一次的操作，
     * 当前线程在数据的操作完成之后会解开当前的锁以便下一个线程操作此数据
     *
     */
    static class MyLockThread implements Runnable {
        // 声明锁
        private Lock lock = new ReentrantLock();

        // 变量 a 被两个线程共同操作，可能会造成线程竞争
        int a = 10;
        @Override
        public void run() {
            // 加锁
            lock.lock();
            for (int i = 0; i < 5; i++) {
                a -= 1;
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {}
                System.out.println(Thread.currentThread().getName() + " → a = " + a);
            }
            // 解锁
            lock.unlock();
        }
    }

    /**
     * synchronized加锁
     */
    static class MySynchronizedThread implements Runnable {
        int a = 10;
        // synchronized 关键字对方法进行加锁
        @Override
        public synchronized void run() {
            for (int i = 0; i < 5; i++) {
                a -= 1;
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {}
                System.out.println(Thread.currentThread().getName() + " → a = " + a);
            }
        }
    }

}

