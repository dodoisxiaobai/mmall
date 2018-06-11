package com.mmall.util;

import com.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;


/**
 * @author dodo
 * @date 2018/5/20
 * @description
 */
@Slf4j
public class RedisPoolUtil {



    public static String set(String key, String value) {
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result  = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static String get(String key) {
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result  = jedis.get(key);
        } catch (Exception e) {
            log.error("set key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     *
     * @param key
     * @param value
     * @param exTime 过期时间，单位是秒
     * @return
     */
    public static String setEx(String key, String value, int exTime) {
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result  = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     *
     * @param key
     * @param exTime 有效期，单位是秒
     * @return
     */
    public static Long expire(String key, int exTime) {
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result  = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("set key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static Long del(String key) {
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result  = jedis.del(key);
        } catch (Exception e) {
            log.error("set key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }


    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();
        RedisPoolUtil.set("a", "bc");

        String val = RedisPoolUtil.get("a");

        RedisPoolUtil.setEx("b", "cd", 5000);

        String val2 = RedisPoolUtil.get("b");

        RedisPoolUtil.expire("a", 20);

        RedisPoolUtil.del("b");

        System.out.println(val);
        System.out.println(val2);
    }
}
