//package org.selftest.interview.redis.lock;
//
//import io.micrometer.core.instrument.util.IOUtils;
//import org.apache.commons.codec.Charsets;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.script.DefaultRedisScript;
//import org.springframework.util.ResourceUtils;
//
//import java.util.Collections;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author guozhipeng
// * @date 2020/8/15 11:50
// */
//public class LuaLock {
//
//    @Autowired
//    RedisTemplate redisTemplate;
//    @Autowired
//    StringRedisTemplate stringRedisTemplate;
//
//    static String lockLuaScript = IOUtils.toString(ResourceUtils.getURL("classpath:lock.lua").openStream(), Charsets.UTF_8);
//    static String lockScript = new DefaultRedisScript<>(lockLuaScript, Boolean.class);
//
//    static String unlockLuaScript = IOUtils.toString(ResourceUtils.getURL("classpath:unlock.lua").openStream(), Charsets.UTF_8);
//    static String unlockScript = new DefaultRedisScript<>(unlockLuaScript, Long.class);
//
//    /**
//     * 可重入锁
//     *
//     * @param lockName  锁名字,代表需要争临界资源
//     * @param request   唯一标识，可以使用 uuid，根据该值判断是否可以重入
//     * @param leaseTime 锁释放时间
//     * @param unit      锁释放时间单位
//     * @return
//     */
//    public Boolean tryLock(String lockName, String request, long leaseTime, TimeUnit unit) {
//        long internalLockLeaseTime = unit.toMillis(leaseTime);
//        return redisTemplate.execute(lockScript, Collections.singletonList(lockName), Collections.singleton(internalLockLeaseTime) String.valueOf(internalLockLeaseTime), request);
//    }
//
//    /**
//     * 解锁
//     * 若可重入 key 次数大于 1，将可重入 key 次数减 1 <br>
//     * 解锁 lua 脚本返回含义：<br>
//     * 1:代表解锁成功 <br>
//     * 0:代表锁未释放，可重入次数减 1 <br>
//     * nil：代表其他线程尝试解锁 <br>
//     * <p>
//     * 如果使用 DefaultRedisScript<Boolean>，由于 Spring-data-redis eval 类型转化，<br>
//     * 当 Redis 返回  Nil bulk, 默认将会转化为 false，将会影响解锁语义，所以下述使用：<br>
//     * DefaultRedisScript<Long>
//     * <p>
//     * 具体转化代码请查看：<br>
//     * JedisScriptReturnConverter<br>
//     *
//     * @param lockName 锁名称
//     * @param request  唯一标识，可以使用 uuid
//     * @throws IllegalMonitorStateException 解锁之前，请先加锁。若为加锁，解锁将会抛出该错误
//     */
//    public void unlock(String lockName, String request) {
//        Long result = redisTemplate.execute(unlockScript, Collections.singletonList(lockName));
//        // 如果未返回值，代表其他线程尝试解锁
//        if (result == null) {
//            throw new IllegalMonitorStateException("attempt to unlock lock, not locked by lockName:+" + lockName + " with request: "
//                    + request);
//        }
//    }
//
//}
