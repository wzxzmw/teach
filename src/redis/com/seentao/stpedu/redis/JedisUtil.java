package com.seentao.stpedu.redis;

import com.seentao.stpedu.config.SystemConfig;
import com.seentao.stpedu.utils.LogUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {
    //Redis服务器IP
    private static String host;
    //Redis的端口号
    private static int port;
    //访问密码
    private static String auth;
    
    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 1024;
    
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;
    
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 10000;
    
    private static int TIMEOUT = 10000;
    
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;
    
    private static JedisPool jedisPool = null;
    
    /**
     * 初始化Redis连接池
     */
    /*private static void initPool() {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setTestOnBorrow(TEST_ON_BORROW);
            host = SystemConfig.getString("redis.host");
            port = SystemConfig.getInteger("redis.port");
            auth = SystemConfig.getString("redis.auth");
            jedisPool = new JedisPool(config, host, port, TIMEOUT, auth);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    
    /**
     * 初始化Redis连接池
     */
    static {
    	 JedisPoolConfig config = new JedisPoolConfig();
         config.setMaxTotal(MAX_ACTIVE);
         config.setMaxIdle(MAX_IDLE);
         config.setMaxWaitMillis(MAX_WAIT);
         config.setTestOnBorrow(TEST_ON_BORROW);
         host = SystemConfig.getString("redis.host");
         port = SystemConfig.getInteger("redis.port");
         auth = SystemConfig.getString("redis.auth");
         jedisPool = new JedisPool(config, host, port, TIMEOUT, auth);
    }
    
    /**
     * 获取Jedis实例
     * @return
     */
    public static Jedis getJedis() {
    	Jedis jedis = jedisPool.getResource();
        return jedis;
    }
    
    /**
     * 释放Jedis资源
     * @param jedis
     */
    public static void release(Jedis jedis) {
    	try {
			jedisPool.returnResource(jedis);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
			LogUtil.error("release is error", e);
		}
    }
}
