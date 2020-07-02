package org.selftest.interview.multithreading.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * 死锁实现
 */
@Slf4j
public class StartRunApplication {

    public static void main(String[] args) {

        Thread t1 = new Thread() {
            @Override
            public void run() {
                pong();
            }
        };
        t1.start();
        System.out.print("ping");

//        Thread t2 = new Thread() {
//            @Override
//            public void run() {
//                pong();
//            }
//        };
//        t2.run();
//        System.out.print("ping");

    }

    static void pong(){
        System.out.print("pang");
    }


}

