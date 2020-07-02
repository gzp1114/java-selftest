package org.selftest.interview.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author guozhipeng
 * @date 2020/6/3 20:16
 */
public class MyLockTest {

    static Lock lock = new ReentrantLock();
//    static Lock lock = new MyLock();

    public static void main(String[] args) throws InterruptedException {
        //主线程lock
        lock.lock();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    lock.lock();
                    System.out.println("拿到锁了");
                    lock.unlock();
                    System.out.println("释放锁了");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        Thread.sleep(10000L);

        lock.unlock();

    }

}
