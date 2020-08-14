package org.selftest.interview.zookeeper.lock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.selftest.interview.zookeeper.ZookeeperClient;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZookeeperLock {

    //根节点
    private static final String ROOT_LOCK = "/nodeLock";
    //zk实例
    private ZooKeeper zooKeeper;
    //会话超时时间
    private int sessionTimeOut = 5000;
    //记录锁节点id
    private String lockId;

    CountDownLatch countDownLatch = new CountDownLatch(1);

    //节点初始化数据
    private final static byte[] date = {1};

    public ZookeeperLock() throws IOException, InterruptedException {
        this.zooKeeper = ZookeeperClient.getZkClient();
    }

    //获取锁
    public boolean lock() throws KeeperException, InterruptedException {
        //创建临时有序节点获取节点id
        lockId = zooKeeper.create(ROOT_LOCK + "/", date,
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(Thread.currentThread().getName() +
                "===>成功创建节点" + "===>节点id为【" + lockId+"】开始竞争锁");

        //获取锁逻辑
        //1、获取根节点下所有子节点
        List<String> childNodes = zooKeeper.getChildren(ROOT_LOCK, true);
        //2、对子节点进行排序,从小到大
        SortedSet<String> sortedSet = new TreeSet<String>();
        for (String child:childNodes) {
            sortedSet.add(ROOT_LOCK + "/" + child);

        }
        //3、拿到最小节点
        String first = sortedSet.first();
        //4、如果当前节点是最小节点则表示获取到锁
        if(lockId.equals(first)){
            System.out.println(Thread.currentThread().getName() +
                    "===>成功获得锁" + "===>节点id为【" + lockId+"】");
        }
        //5、如果没有获取锁，则需要拿到比当前节点的上一个节点对它监听
        //取得比当前节点小的的节点集合
        SortedSet<String> lessthenLockId = sortedSet.headSet(lockId);
        if(!lessthenLockId.isEmpty()){
            //拿到比当前节点小的上一个节点
            String lastNode = lessthenLockId.last();
            //对这个节点进行监控
            zooKeeper.exists(lastNode, new LockWacher(countDownLatch));

            countDownLatch.await(sessionTimeOut, TimeUnit.MILLISECONDS);

            System.out.println(Thread.currentThread().getName() +
                    "===>成功获得锁" + "===>节点id为【" + lockId+"】");
        }

        return true;
    }

    //释放锁
    public boolean unlock() throws KeeperException, InterruptedException {
        System.out.println(Thread.currentThread().getName() +
                "===>开始释放锁" + "===>节点id为【" + lockId+"】");
        zooKeeper.delete(lockId,-1);
        System.out.println(Thread.currentThread().getName() +
                "===>释放锁成功" + "===>节点id为【" + lockId+"】");
        return true;
    }

}
