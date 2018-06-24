package com.mmall.util;

import redis.clients.jedis.ShardedJedis;

/**
 * @author dodo
 * @date 2018/6/18
 * @description
 */
public class RedisShardedPoolUtil {


    public static Long expire(String key, int exTime) {
        ShardedJedis jedis = null;
        Long result = null;
        return null;
    }
}
