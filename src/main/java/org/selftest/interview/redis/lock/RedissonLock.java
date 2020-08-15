package org.selftest.interview.redis.lock;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @author guozhipeng
 * @date 2020/7/29 9:26
 */
public class RedissonLock {

    private static RedissonClient redissonClient;

    static {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        redissonClient = Redisson.create(config);
    }

    public static void main(String[] args) throws InterruptedException {
        RLock rLock = redissonClient.getLock("rlock");
        //最多等待30秒，上锁10秒后自动解锁
        if(rLock.tryLock(30,10, TimeUnit.MINUTES)){
            System.out.println("获取锁成功");
        }

        rLock.unlock();

        redissonClient.shutdown();

    }

}
