package org.selftest.interview.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.UUID;

/**
 * @author guozhipeng
 * @date 2020/9/28 14:40
 */
@Component
public class RedisDelayingQueue<T> {
    protected static final Logger logger = LoggerFactory.getLogger(RedisDelayingQueue.class);

    private static String host="127.0.0.1";
    private static int port=6379;
    private static int maxTotal=100;
    private static int maxIdle=20;
    private static int minIdle=5;
    private static String password;
    private static int timeout=1000;

    private static JedisPool jedisPool;

    static class TaskItem<T> {
        public String id;
        public T msg;
    }

    private Type taskType = new TypeReference<TaskItem<T>>() { }.getType();

    public static void init() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, null);
        logger.info("JedisPool注入成功！！");
        logger.info("redis地址：" + host + ":" + port);
    }

    private String queueKey;

    public RedisDelayingQueue(String queueKey) {
        this.queueKey = queueKey;
    }

    public void delay(T msg) {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            TaskItem task = new TaskItem();
            // 分配唯一的 uuid
            task.id = UUID.randomUUID().toString();
            task.msg = msg;
            // fastjson 序列化
            String s = JSON.toJSONString(task);
            // 塞入延时队列 ,5s 后再试
            jedis.zadd(queueKey, System.currentTimeMillis() + 5000, s);
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void loop() {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();

            while (!Thread.interrupted()) {
                // 只取一条
                Set values = jedis.zrangeByScore(queueKey, 0, System.currentTimeMillis(), 0, 1);
                if (values.isEmpty()) {
                    try {
                        // 歇会继续
                        Thread.sleep(500);
                    }
                    catch (InterruptedException e) {
                        break;
                    }
                    continue;
                }
                String s = (String) values.iterator().next();
                if (jedis.zrem(queueKey, s) > 0) { // 抢到了
                    TaskItem task = JSON.parseObject(s, taskType); // fastjson 反序列化
                    this.handleMsg((T) task.msg);
                }
            }
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }
    public void handleMsg(T msg) {
        System.out.println(msg);
    }

    public static void main(String[] args) {
        init();

        RedisDelayingQueue queue = new RedisDelayingQueue<>("q-demo");
        Thread producer = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    queue.delay("codehole" + i);
                }
            }
        };
        Thread consumer = new Thread() {
            @Override
            public void run() {
                queue.loop();
            }
        };
        producer.start();
        consumer.start();
        try {
            producer.join();
            Thread.sleep(6000);
            consumer.interrupt();
            consumer.join();
        }
        catch (InterruptedException e) {
        }
    }

}
