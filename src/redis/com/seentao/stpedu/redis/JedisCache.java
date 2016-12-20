package com.seentao.stpedu.redis;

import java.util.List;
import java.util.Map;

import com.seentao.stpedu.constants.GameConstants;

import redis.clients.jedis.Jedis;

public class JedisCache {
	
	
	/**
	 * 批量设置Redis缓存
	 * @author chengshx
	 * @param mapName map名
	 * @param mapValue 批量缓存值<id, json>格式
	 */
	public static void setRedisMap(String mapName, Map<String, String> mapValue){
		Jedis jedis = JedisUtil.getJedis();
		jedis.hmset(GameConstants.REDIS_PATH+mapName, mapValue);
		JedisUtil.release(jedis);
	}
	
	/**
	 * 设置一条Redis缓存
	 * @param mapName map名
	 * @param key id(主键)
	 * @param val json串
	 */
	public static void setRedisVal(String prefix, String mapName, String key, String val){
		Jedis jedis = JedisUtil.getJedis();
		if(prefix == null){
			jedis.hset(GameConstants.REDIS_PATH + mapName, key, val);
		} else {
			jedis.hset(prefix + mapName, key, val);
		}
		JedisUtil.release(jedis);
	}
	
	/**
	 * 获取对象Map缓存
	 * @author chengshx
	 * @param mapName map名
	 * @return 一张表的对象缓存 <id, json>格式
	 */
	public static Map<String, String> getRedisMap(String prefix, String mapName){
		Jedis jedis = JedisUtil.getJedis();
		Map<String, String> map;
		if(prefix == null){
			map = jedis.hgetAll(GameConstants.REDIS_PATH+mapName);
		} else {
			map = jedis.hgetAll(prefix+mapName);
		}
		JedisUtil.release(jedis);
		return map;
	}
	
	/**
	 * 获取对象缓存
	 * @author chengshx
	 * @param mapName map名
	 * @param key 对象ID
	 * @return 一个对象json串
	 */
	public static String getRedisVal(String prefix, String mapName, String key){
		Jedis jedis = JedisUtil.getJedis();
		String val = null;
		if(prefix == null){
			val = jedis.hget(GameConstants.REDIS_PATH + mapName, key);
		} else {
			val = jedis.hget(prefix + mapName, key);
		}
		JedisUtil.release(jedis);
		return val;
	}
	
	/**
	 * 删除对象缓存
	 * @author chengshx
	 * @param mapName map名
	 * @param key 对象ID
	 * @return 一个对象json串
	 */
	public static void removeRedisVal(String prefix, String mapName, String key){
		Jedis jedis = JedisUtil.getJedis();
		if(prefix == null){
			jedis.del(GameConstants.REDIS_PATH + mapName, key);
		} else {
			jedis.del(prefix + mapName, key);
		}
		JedisUtil.release(jedis);
	}
	
	/**
	 * 批量获取缓存内容
	 * @param mapName
	 * @param keys
	 * @return
	 * @author 			lw
	 * @date			2016年6月22日  下午8:25:42
	 */
	public static List<String> getBatchRedisVal(String mapName, String... keys){
		Jedis jedis = JedisUtil.getJedis();
		List<String> hmget = jedis.hmget(GameConstants.REDIS_PATH+mapName, keys);
		JedisUtil.release(jedis);
		return hmget;
	}
	
	
	/**
	 * 删除一个表的缓存
	 * @author chengshx
	 * @param mapName map名
	 */
	public static void delRedisMap(String prefix, String mapName){
		Jedis jedis = JedisUtil.getJedis();
		if(prefix == null){
			jedis.del(GameConstants.REDIS_PATH+mapName);
		} else {
			jedis.del(prefix+mapName);
		}
		JedisUtil.release(jedis);
	}
	
	/**
	 * 删除一个或多个缓存值
	 * @param mapName map名称
	 * @param keys id数组
	 */
	public static void delRedisVal(String mapName, String... keys){
		Jedis jedis = JedisUtil.getJedis();
		jedis.hdel(GameConstants.REDIS_PATH+mapName, keys);
		JedisUtil.release(jedis);
	}
	
	/**
	 * 删除一个或多个缓存值
	 * @param mapName map名称
	 * @param keys id数组
	 */
	public static void delRedisValWithPrefix(String prefix, String mapName, String... keys){
		Jedis jedis = JedisUtil.getJedis();
		if(prefix == null){
			jedis.hdel(GameConstants.REDIS_PATH+mapName, keys);
		} else {
			jedis.hdel(prefix+mapName, keys);
		}
		JedisUtil.release(jedis);
	}
	
	/**
	 * 设置Redis缓存
	 * @author chengshx
	 * @param key 对象id 
	 * @param val json串
	 */
	public static void setRedis(String key, String val){
		Jedis jedis = JedisUtil.getJedis();
		jedis.set(GameConstants.REDIS_PATH+key, val);
		JedisUtil.release(jedis);
	}
	
	/**
	 * 获取Redis缓存
	 * @author chengshx
	 * @param key 对象id
	 * @return json串
	 */
	public static String getRedis(String key){
		Jedis jedis = JedisUtil.getJedis();
		String val = jedis.get(GameConstants.REDIS_PATH+key);
		JedisUtil.release(jedis);
		return val;
	}
	
	/**
	 * 删除Redis缓存
	 * @author chengshx
	 * @param key 对象id
	 */
	public static void delRedis(String key){
		Jedis jedis = JedisUtil.getJedis();
		jedis.del(GameConstants.REDIS_PATH+key);
		JedisUtil.release(jedis);
	}
	
	/**
	 * 设置一条Redis缓存
	 * @param mapName map名
	 * @param key id(主键)
	 * @param val json串
	 */
//	public static void setRedisValWithPrefix(String prefix, String mapName, String key, String val){
//		Jedis jedis = JedisUtil.getJedis();
//		jedis.hset(GameConstants.REDIS_PATH+mapName, key, val);
//		JedisUtil.release(jedis);
//	}
//	
//	/**
//	 * 获取对象缓存
//	 * @author chengshx
//	 * @param mapName map名
//	 * @param key 对象ID
//	 * @return 一个对象json串
//	 */
//	public static String getRedisValWithPrefix(String prefix, String mapName, String key){
//		Jedis jedis = JedisUtil.getJedis();
//		String val = jedis.hget(GameConstants.REDIS_PATH+mapName, key);
//		JedisUtil.release(jedis);
//		return val;
//	}
//	
//	/**
//	 * 删除一个或多个缓存值
//	 * @param mapName map名称
//	 * @param keys id数组
//	 */
//	public static void delRedisValWithPrefix(String prefix, String mapName, String... keys){
//		Jedis jedis = JedisUtil.getJedis();
//		jedis.hdel(GameConstants.REDIS_PATH+mapName, keys);
//		JedisUtil.release(jedis);
//	}
}
