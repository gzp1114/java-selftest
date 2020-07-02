package org.selftest.interview.multithreading.lock;

import lombok.extern.slf4j.Slf4j;

/**
 * 死锁实现
 */
@Slf4j
public class DeadLockApplication {

    static Object o1 = new Object();
    static Object o2 = new Object();

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (o1) {
                    System.out.println("线程1锁o1");
                    try {
                        Thread.sleep(1000);//让当前线程睡眠，保证让另一线程得到o2，防止这个线程启动一下连续获得o1和o2两个对象的锁。
                        synchronized (o2) {
                            System.out.println("线程1锁o2");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (o2) {
                    System.out.println("线程2锁o2");
                    synchronized (o1) {
                        System.out.println("线程2锁o1");
                    }
                }
            }
        }).start();
    }


}

