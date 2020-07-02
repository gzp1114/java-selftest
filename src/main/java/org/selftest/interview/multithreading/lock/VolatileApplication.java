package org.selftest.interview.multithreading.lock;

import lombok.extern.slf4j.Slf4j;

/**
 * volatile关键字
 * 作用：保证了变量的可见性
 * volatile只能保证变量的可见性，不能保证对volatile变量操作的原子性
 */
@Slf4j
public class VolatileApplication {

    public static void main(String[] args) {
//        Work work = new Work();
//
//        new Thread(work::doWork).start();
//        new Thread(work::doWork).start();
//        new Thread(work::doWork).start();
//        new Thread(work::shutdown).start();
//        new Thread(work::doWork).start();
//        new Thread(work::doWork).start();
//        new Thread(work::doWork).start();

//        A aaa = new A();
//        new Thread(() -> aaa.reader()).start();
//        new Thread(() -> aaa.writer()).start();

        B a = new B();

        new Thread(() -> {
            for (int i = 0;i < 1000;i++) {
                a.increase();
            }
            System.out.println(a.getB());
        }).start();
        new Thread(() -> {
            for (int i = 0;i < 2000;i++) {
                a.increase();
            }
            System.out.println(a.getB());
        }).start();
        new Thread(() -> {
            for (int i = 0;i < 3000;i++) {
                a.increase();
            }
            System.out.println(a.getB());
        }).start();
        new Thread(() -> {
            for (int i = 0;i < 4000;i++) {
                a.increase();
            }
            System.out.println(a.getB());
        }).start();
        new Thread(() -> {
            for (int i = 0;i < 5000;i++) {
                a.increase();
            }
            System.out.println(a.getB());
        }).start();


    }

    static class Work {
        volatile boolean isShutDown = false;

        void shutdown() {
            isShutDown = true;
            System.out.println("shutdown!");
        }

        void doWork() {
            while (!isShutDown) {
                System.out.println("doWork");
            }
        }
    }

    static class A {
        int a = 0;
        volatile boolean flag = false;

        void writer() {
            a = 1;                   //1
            flag = true;               //2
            System.out.println("write");
        }

        void reader() {
            if (flag) {                //3
                int i =  a;           //4
                System.out.println("read true");
                System.out.println("i is :" + i);
            } else {
                int i = a;
                System.out.println("read false");
                System.out.println("i is :" + i);
            }
        }

    }

    static class B {
        volatile int a = 0;
        void increase() {
            a++;
        }
        int getB(){
            return a;
        }
    }

}

