package org.selftest.interview.multithreading.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * wait()和 notify()方法来实现阻塞队列
 */
@Slf4j
public class BlockingQueueApplication {


    public static void main(String[] args) {
        final MyQueue m = new MyQueue(5);

        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                m.put("a");
                m.put("b");
                m.put("c");
                m.put("d");
                m.put("e");
                m.put("f");

            }
        });
        t1.start();

        try {
            Thread.sleep(1000);
            System.out.println("当前容器的大小："+m.size());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                Object o = m.take();
                System.out.println("取出的元素为："+o);
            }
        });
        t2.start();
        try {
            Thread.sleep(10);
            System.out.println(m.size());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    static class MyQueue {

        //1.需要装元素的集合
        private final LinkedList<Object> list = new LinkedList<Object>();

        /**
         * 2.需要一个计数器，统计加入list几个的个数
         * AtomicInteger，一个提供原子操作的Integer的类。在Java语言中
         * ，++i和i++操作并不是线程安全的，在使用的时候，不可避免的会用到
         * synchronized关键字。而AtomicInteger则通过一种线程安全的加减操作接口。
         */
        private AtomicInteger count = new AtomicInteger(0);

        //指定上限上限和下限
        private final int minSize = 0;
        private final int maxSize;

        //构造方法，构造容器最大长度
        public MyQueue(int size) {
            this.maxSize = size;
        }

        public MyQueue() {
            //最大长度默认为10
            this(10);
        }

        /**
         * 初始化一个对象用于加锁
         */
        private final Object lock = new Object();


        //返回长度
        public int size() {
            return count.get();
        }


        /**
         * 添加一个对象，如果队列满了，则阻塞
         */
        public void put(Object obj) {
            synchronized (lock) {
                //如果容器大小刚好等于最大长度，则阻塞
                while (size() == maxSize) {
                    try {
                        lock.wait();//阻塞
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                list.add(obj);
                count.incrementAndGet();//相当于i++
                System.out.println("新加入的元素为：" + obj);
                lock.notify();//通知另外一个线程去取元素
            }
        }

        /**
         * 取出一个元素，如果队列为空，则阻塞
         */
        public Object take() {
            synchronized (lock) {
                //如果容器的大小刚好等于队列最小长度，则阻塞
                while (minSize == size()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                //取出第一个元素
                Object obj = list.removeFirst();
                //计数器递减
                count.decrementAndGet();
                lock.notify();//通知另外一个线程进行添加元素
                return obj;//返回结果

            }
        }
    }
}

