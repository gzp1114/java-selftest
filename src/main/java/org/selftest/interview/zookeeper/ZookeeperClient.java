package org.selftest.interview.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperClient {

    /**
     * zk地址
     */
    private final static String ZKIP = "10.180.7.7:2181";

    //会话超时时间
    private static int SESSIONTIMEOUT = 5000;

    public static ZooKeeper getZkClient() throws IOException, InterruptedException {

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        // 去连接zookeeper server, 创建会话的时候，是异步进行的
        // 所有要给一个监听器，说告诉我们什么时候是真正的完成跟zookeeper server 连接
        ZooKeeper zooKeeper = new ZooKeeper(ZKIP, SESSIONTIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                //这里判断连接状态是不是连接成功的
                if(watchedEvent.getState()==Event.KeeperState.SyncConnected){
                    countDownLatch.countDown();
                }
            }
        });

        countDownLatch.await();
        return zooKeeper;
    }

}
