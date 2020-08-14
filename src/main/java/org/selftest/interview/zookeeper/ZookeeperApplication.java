package org.selftest.interview.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.selftest.interview.zookeeper.lock.DistributeLock;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class ZookeeperApplication {

    private static final String connectionString = "10.180.7.7:2181";
    private static final int sessionTimeout = 5000;
    private static final CountDownLatch zkCountDownLatch = new CountDownLatch(1);

    public static void main(String[] args) {

        final CountDownLatch countDownLatch = new CountDownLatch(5);

        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                DistributeLock lock= null;
                try {
                    lock = new DistributeLock();
                    countDownLatch.countDown();
                    countDownLatch.await();
                    lock.lock();
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(lock!=null){
                        try {
                            lock.unlock();
                        } catch (KeeperException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }


}

