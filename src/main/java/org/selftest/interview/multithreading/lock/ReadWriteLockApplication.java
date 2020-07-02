package org.selftest.interview.multithreading.lock;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 你需要实现一个高效的缓存，它允许多个用户读，但只允许一个用户写，
 * 以此来保持它的完整性，你会怎样去实现它？
 *
 * Lock读写锁机制可以实现
 */
@Slf4j
public class ReadWriteLockApplication {


    public static void main(String[] args) {
        final TheData theData = new TheData();

        for(int i=0;i<4;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    theData.put(ThreadLocalRandom.current().nextInt(1000));
                }
            }).start();
        }

        for(int i=0;i<4;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    theData.get();
                }
            }).start();
        }

    }


    static class  TheData{
        private Integer data = 0;
        private ReadWriteLock rwLock = new ReentrantReadWriteLock();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        public void get(){
            rwLock.readLock().lock();//读锁开启，读进程均可进入
            try{//用try finally来防止因异常而造成的死锁
                System.out.println(Thread.currentThread().getName()+"read lock is ready.."+sdf.format(new Date()));
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName()+"read data is"+data);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }finally{
                rwLock.readLock().unlock();//读锁解锁
            }
        }

        public void put(Integer data){
            rwLock.writeLock().lock();//写锁开启，这时只有一个写线程进入
            try{//用try finally来防止因异常而造成的死锁
                System.out.println(Thread.currentThread().getName()+"write lock is ready.."+sdf.format(new Date()));
                Thread.sleep(1000);
                this.data = data;
                System.out.println(Thread.currentThread().getName()+"write data is"+data);

            }catch (InterruptedException e) {
                e.printStackTrace();
            }finally{
                rwLock.writeLock().unlock();//写锁解锁
            }
        }
    }

}

