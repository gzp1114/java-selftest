package org.selftest.interview.zookeeper.lock;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

public class LockWacher implements Watcher {

    private CountDownLatch countDownLatch;

    public LockWacher(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        //监听节点是否被删除
        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            countDownLatch.countDown();
        }
    }
}
