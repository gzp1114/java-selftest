package org.selftest.interview.redis.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author guozhipeng
 * @date 2020/7/24 15:47
 */
public class RedisTemplateLock {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public Boolean getLock(String key){
        String value = UUID.randomUUID().toString();
        Boolean lockFlag = redisTemplate.opsForValue().setIfAbsent(key, value, 10, TimeUnit.SECONDS);
        return lockFlag;
    }

    public Boolean releaseLock(String key){
        Boolean result = stringRedisTemplate.delete(key);
        return result;
    }

    public void safedUnLock(String key, String val) {
        String luaScript = "local in = ARGV[1] local curr=redis.call('get', KEYS[1]) if in==curr then redis.call('del', KEYS[1]) end return 'OK'";
        RedisScript<String> redisScript = RedisScript.of(luaScript);
        redisTemplate.execute(redisScript, Collections.singletonList(key), Collections.singleton(val));
    }

    public Long stock(){
        Long currStock = redisTemplate.opsForHash().increment("key", "stock", -1);
        return currStock;
    }

}
