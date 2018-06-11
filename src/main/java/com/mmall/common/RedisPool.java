package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author dodo
 * @date 2018/5/18
 * @description
 */
public class RedisPool {

    //jedis连接池
    private static JedisPool pool;
    //最大连接数
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total"));
    //最大空闲状态连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle"));
    //最小空闲状态连接数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle"));
    //
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow"));
    //
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return"));
    // ip
    private static String redisIp = PropertiesUtil.getProperty("redis.ip");
    // port
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        // default settings
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setMaxWaitMillis(10000);
        pool = new JedisPool(config, redisIp, redisPort, 1000 * 2);

    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

    static {
        initPool();
    }

    public static void returnBrokenResource(Jedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void returnResource(Jedis jedis) {
        pool.returnResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("abc", "abc");
        pool.destroy();
        System.out.println("end");
    }

}
