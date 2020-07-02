package org.selftest.interview.lock;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * @author guozhipeng
 * @date 2020/6/3 20:09
 */
public class MyLock implements Lock {

    AtomicInteger state = new AtomicInteger(0);
    Thread ownerThread = new Thread();
    LinkedBlockingQueue<Thread> queue = new LinkedBlockingQueue<>();

    @Override
    public void lock() {
        if(!tryLock()){
            //排队
            queue.add(Thread.currentThread());
            for (;;){
                if(tryLock()){
                    queue.poll();
                    return;
                }else {
                    //等待唤醒
                    LockSupport.park();
                }
            }

        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        if(state.get() == 0){
            //CAS操作
            if(state.compareAndSet(0,1)){
                ownerThread = Thread.currentThread();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        //必须当前线程可以
        if(ownerThread != Thread.currentThread()){
            throw new RuntimeException("非法调用");
        }

        if(state.decrementAndGet() == 0){
            ownerThread = null;
            //通知队列等待线程
            Thread waiterThread = queue.peek();
            if(null != waiterThread){
                LockSupport.unpark(waiterThread);
            }

        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
