package org.selftest.interview.zookeeper.queue;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 *  Curator分布式队列实现
 *
 * @author guozhipeng
 * @date 2020/8/13 20:17
 */
public class CustomizeQueue {

    private static final String CONNECT_IP_PORT = "10.180.7.7:2181";
    private static final Logger logger = LoggerFactory.getLogger(DistributedQueueExample.class);
    private static final String PATH = "/example/customize-queue";

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString(CONNECT_IP_PORT)
                .sessionTimeoutMs(5000).connectionTimeoutMs(5000).retryPolicy(retryPolicy)
                .build();
        curatorFramework.start();

        // 消费者监听事件[这里采用服务器主动推模式，主动拉不实现了]
        PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework, PATH, true);
        childrenCache.start(PathChildrenCache.StartMode.NORMAL);

        childrenCache.getListenable().addListener((client, event) -> {
            switch (event.getType()) {
                case CHILD_ADDED:
                    // 接收到节点新增,这里还需要判断新增节点前是否 还有更小的节点，有可能因为网络等，没有处理之前的请求
                    SortedSet<String> childrens = new TreeSet<>(client.getChildren().forPath(PATH));
                    // 这些都是比当前节点小的有序队列
                    String absolutePath = event.getData().getPath();
                    SortedSet<String> headSet = childrens.headSet(absolutePath.substring(absolutePath.indexOf("/") + 1));

                    // 接收到订单请求，处理完毕需要将zk节点移除
                    for (String node : headSet) {
                        String currentPath = PATH + "/" + node;
                        String orderId = new String(client.getData().forPath(currentPath));
                        try {
                            logger.debug("新增节点,接收到创建订单请求，开始处理订单: {}", orderId);
                            client.delete().forPath(currentPath);
                        } catch (Exception e) {
                            logger.debug("当前节点为：{}， 节点出现异常：【{}】，本次跳过处理订单: {}", node, e.getMessage(), orderId);
                            continue;
                        }
                    }
                    break;
                case CHILD_REMOVED:
                    String orderId = new String(event.getData().getData());
                    logger.debug("处理订单成功，移除订单，当前订单号为：{}, 节点为：{}", orderId, event.getData().getPath());
                    break;
                default:
                    break;
            }
        });

        // 生产者
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                String orderId = generateString(18);
                String sequencePath = PATH + "/";
                String forPath;
                try {
                    forPath = curatorFramework.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                            .forPath(sequencePath, orderId.getBytes());
                    logger.debug("生产者，创建节点：{}, 订单号：{}", forPath, orderId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        System.in.read();
        childrenCache.close();
        curatorFramework.close();
    }

    public static final String allChar = "0123456789";

    // 模拟产生一个订单
    public static String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(allChar.charAt(random.nextInt(allChar.length())));
        }
        return sb.toString();
    }
}


