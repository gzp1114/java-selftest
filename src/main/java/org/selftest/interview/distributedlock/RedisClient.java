package org.selftest.interview.distributedlock;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.params.SetParams;

import java.util.*;

public class RedisClient {

    private static final Long RELEASE_SUCCESS = 1L;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 获取锁
     *
     * @param key
     * @param seconds
     * @return
     */
    public String getLock(String key, int seconds){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = UUID.randomUUID().toString();
            long end = System.currentTimeMillis() + 6000;//默认拥有6秒轮询获取锁
            while (System.currentTimeMillis() < end) {//阻塞
                SetParams setParams = new SetParams();
                setParams.nx();
                setParams.ex(seconds);
                String result = jedis.set(key, value,setParams);
                if ("OK".equalsIgnoreCase(result)) {
                    //锁设置成功，redis操作成功，并返回锁的值
                    return value;
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
    /**
     * 释放锁
     *
     * @param lockName
     * @param value
     * @return
     */
    public boolean releaseLock(String lockName, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String lockValue=jedis.get(lockName);
            if (value.equals(lockValue)) {
                jedis.del(lockName);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;

    }
    /**
     * 释放锁
     *
     * @param key
     * @param value
     * @return
     */
    public boolean safeReleaseLock(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(key), Collections.singletonList(value));
            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
            return false;


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;

    }


    public void set(String key, Object value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }


    public void setnx(String key, Object value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.setnx(key, value.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param value
     * @param exptime seconds
     * @throws Exception
     */
    public void setWithExpireTime(String key, String value, int exptime) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            jedis.expire(key, exptime);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }


    public String get(String key) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 批量查询 jedis.keys
     *
     * @param pattern
     * @return
     */
    public List<Object> getList(String pattern) {
        List<Object> list = new ArrayList<>();
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Set<String> keys = jedis.keys(pattern);
            Pipeline pip = jedis.pipelined();
            for (String s : keys) {
                pip.get(s);
            }
            List<Object> searchList = pip.syncAndReturnAll();
            if (searchList != null && searchList.size() > 0) {
                list = searchList;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            jedis.close();
        }
        return list;
    }

    /**
     * 批量插入or更新
     *
     * @param map
     */
    public void setList(Map<String, String> map) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Pipeline pip = jedis.pipelined();
            for (String s : map.keySet()) {
                pip.set(s, map.get(s));
            }
            pip.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

    public Long del(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            jedis.close();
        }
    }


    /**
     * 向Redis中Set集合添加值:点赞
     *
     * @return
     */
    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            ////logger.error("Jedis sadd 异常 ：" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 移除：取消点赞
     *
     * @param key
     * @param value
     * @return
     */
    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            ////logger.error("Jedis srem 异常：" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 判断key,value是否是集合中值
     *
     * @param key
     * @param value
     * @return
     */
    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            ////logger.error("Jedis sismember 异常：" + e.getMessage());
            return false;
        } finally {
            if (jedis != null) {
                try {
                    jedis.close();
                } catch (Exception e) {
                    ////logger.error("Jedis关闭异常" + e.getMessage());
                }
            }
        }
    }

    /**
     * 获取集合大小
     *
     * @param key
     * @return
     */
    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            ////logger.error("Jedis scard 异常：" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    public Set<String> smembers(String key) {

        Jedis jedis = null;
        Set<String> smembers = new HashSet<String>();
        try {
            jedis = jedisPool.getResource();
            smembers = jedis.smembers(key);
            return smembers;
        } catch (Exception e) {
            ////logger.error("Jedis scard 异常：" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
            return smembers;
        }
    }


    public String setnx(String key, String value, int expireTime) {
        Jedis jedis = null;
        String result = "";
        try {
            jedis = jedisPool.getResource();
            SetParams setParams = new SetParams();
            setParams.nx();
            setParams.ex(expireTime);
            result = jedis.set(key, value,setParams);
            System.out.println("-------"+result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return result;
    }

    public String getset(String key, String value) {
        Jedis jedis = null;
        String result = "";
        try {
            jedis = jedisPool.getResource();
            result = jedis.getSet(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return result;
    }

    public String delKeys(String headKey) {
        Jedis jedis = null;
        String result = "";
        try {
            jedis = jedisPool.getResource();
            Set<String> keySet = jedis.keys(headKey + "*");
            if (keySet != null) {
                String[] keys = new String[keySet.size()];
                keys = keySet.toArray(keys);
                jedis.del(keySet.toArray(keys));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return result;
    }

    public Long incr(String key) {
        Jedis jedis = null;
        Long value = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.incr(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return value;
    }

    public Long incrByTime(String key, long increment,int expireTime ) {
        Jedis jedis = null;
        Long value = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.incrBy(key,increment);
            jedis.expire(key, expireTime);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return value;
    }

    public Long incrByTime(String key, int expireTime) {
        Jedis jedis = null;
        Long value = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.incr(key);
            jedis.expire(key, expireTime);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return value;
    }

    public Long decrByTime(String key, int expireTime) {
        Jedis jedis = null;
        Long value = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.decr(key);
            jedis.expire(key, expireTime);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return value;
    }

    public String hget(String key,String field) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hget(key, field);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return  result;
    }
    public Long hset(String key,String field,String value) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hset(key, field, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return result;
    }
    public Long hincrby(String key,String field,Long value) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hincrBy(key, field, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return result;
    }
    public Long expire(String key,Integer value) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.expire(key,value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return result;
    }
    public Long hdel(String key,String f) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hdel(key,f);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return result;
    }
    public Boolean exists(String key) {
        Jedis jedis = null;
        Boolean result = true;
        try {
            jedis = jedisPool.getResource();
            result = jedis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return result;
    }
    public Map hgetAll(String key) {
        Jedis jedis = null;
        Map result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hgetAll(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return result;
    }
    public Long ttl(String key) {
        Jedis jedis = null;
        Long result = 0L;
        try {
            jedis = jedisPool.getResource();
            result = jedis.ttl(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return result;
    }
}