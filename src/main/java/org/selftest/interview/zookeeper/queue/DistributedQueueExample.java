package org.selftest.interview.zookeeper.queue;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Curator 实现的分布式队列
 *
 * @author guozhipeng
 * @date 2020/8/13 20:12
 */
public class DistributedQueueExample {
    private static final String PATH = "/example/customize-queue";
    private static final String CONNECT_IP_PORT = "10.180.7.7:2181";
    private static final Logger logger = LoggerFactory.getLogger(DistributedQueueExample.class);

    public static void main(String[] args) throws Exception {
        CuratorFramework client = null;
        DistributedQueue<String> queue = null;
        try {
            //创建连接
            client = CuratorFrameworkFactory.builder().connectString(CONNECT_IP_PORT).sessionTimeoutMs(5000)
                    .connectionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
            client.start();
            logger.debug("创建连接成功：{}", client);

            // 消费者
            QueueConsumer<String> consumer = createQueueConsumer();
            // 生产者
            queue = QueueBuilder.builder(client, consumer, createQueueSerializer(), PATH).buildQueue();
            queue.start();

            for (int i = 0; i < 10; i++) {
                queue.put(" test-" + i);
            }

            TimeUnit.SECONDS.sleep(20);

        } catch (Exception ex) {
        } finally {
            CloseableUtils.closeQuietly(queue);
            CloseableUtils.closeQuietly(client);
        }
    }

    private static QueueSerializer<String> createQueueSerializer() {
        return new QueueSerializer<String>() {
            @Override
            public byte[] serialize(String item) {
                return item.getBytes();
            }

            @Override
            public String deserialize(byte[] bytes) {
                return new String(bytes);
            }

        };
    }

    private static QueueConsumer<String> createQueueConsumer() {
        return new QueueConsumer<String>() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                logger.debug("状态改变，当前状态：{}", newState.name());
            }

            @Override
            public void consumeMessage(String message) throws Exception {
                logger.debug("消费者消费消息：{}", message);
            }
        };
    }
}

