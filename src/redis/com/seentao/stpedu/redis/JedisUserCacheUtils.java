
package com.seentao.stpedu.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.seentao.stpedu.common.entity.BsRelVoteItemUser;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.RedisUserPhone;
import com.seentao.stpedu.constants.GameConstants;

import redis.clients.jedis.Jedis;

/** 
* @author 作者 wangzx: 
* @date 创建时间：2016年10月27日 下午3:21:23
	Email :wzxzmw@163.com
* 	类描述功能说明 :
*/
public class JedisUserCacheUtils {
	
/*	public static void hdelUserPasswordHashByPhone(final String phoneKey){
		Jedis jedis = JedisUtil.getJedis();
		jedis.hdel(GameConstants.REDIS_REGISTER_USER+RedisLogin.class.getSimpleName(), phoneKey);
		JedisUtil.release(jedis);
	}*/
/*	public static void setUserPasswordHashByPhone(final String phoneKey,final String value){
		Jedis jedis = JedisUtil.getJedis();
		jedis.hset(GameConstants.REDIS_REGISTER_USER+RedisLogin.class.getSimpleName(), phoneKey, value);
		JedisUtil.release(jedis);
	}*/
	/**
	 * 根据mapName,phonekey删除注册信息
	 */
	public static void hdelRegisterUserHashByPhone(final String phoneKey){
		Jedis jedis = JedisUtil.getJedis();
		jedis.hdel(GameConstants.REDIS_USER+RedisUserPhone.class.getSimpleName(), phoneKey);
		JedisUtil.release(jedis);
	}
	/**注册用户存入redis中
	 * @param mapKey
	 * @param value
	 */
	public static void setRegisterUserHash(final String mapName,final String mapKey ,String value){
		Jedis jedis = JedisUtil.getJedis();
		jedis.hset(GameConstants.REDIS_USER+mapName, mapKey, value);
		JedisUtil.release(jedis);
	}
	
	/** 删除user_phone
	 * @param mapKey
	 * @param value
	 */
	public static void delHdelUserHash(final String mapName,final String mapKey){
		Jedis jedis = JedisUtil.getJedis();
		jedis.hdel(GameConstants.REDIS_USER+mapName, mapKey);
		JedisUtil.release(jedis);
	}
	
	/**
	 * @param mapName
	 * @param mapKey
	 * @param value
	 * @return
	 */
	public static String getRegisterUserHash(final String mapName,final String mapKey){
		Jedis jedis = JedisUtil.getJedis();
		String reslut = jedis.hget(GameConstants.REDIS_USER+mapName, mapKey);
		JedisUtil.release(jedis);
		return reslut;
	}
	/**针对注册校验使用的redis
	 * @param mapName
	 * @param mapKey
	 * @param value
	 * @return
	 */
	public static long setCheckLoginHash(final String mapKey,final String value){
		Jedis jedis = JedisUtil.getJedis();
		long count = jedis.hset(GameConstants.REDIS_USER+RedisUserPhone.class.getSimpleName(), mapKey, value);
		JedisUtil.release(jedis);
		return count;
	}
	/**根据key查询Hash列表中的数据
	 * @param key
	 * @return
	 */
	public static String getCheckLoginHash(final String key){
		Jedis jedis = JedisUtil.getJedis();
		String result = jedis.hget(GameConstants.REDIS_USER+RedisUserPhone.class.getSimpleName(), key);
		JedisUtil.release(jedis);
		return result;
	}
	
	public static void hdelCenterUserByPhoneHash(final String key){
		Jedis jedis = JedisUtil.getJedis();
		jedis.hdel(GameConstants.REDIS_USER+CenterUser.class.getSimpleName(), key);
		JedisUtil.release(jedis);
	}
	/*
	 * 用户的批量投票添加
	 * mapName hashKey
	 * Map<String,String> map value值
	 */
	public static void hmsetVoteRelVoteItemUser(final String mapName,final Map<String,String> map){
		Jedis jedis = JedisUtil.getJedis();
		jedis.hmset(GameConstants.REDIS_HASH_VOTE+mapName, map);
		JedisUtil.release(jedis);
	}
	/*
	 * 用户的批量查询
	 */
	public static List<String> hmgetVoteRelVoteItemUser(final String mapName,final String...fields){
		Jedis jedis = JedisUtil.getJedis();
		List<String> result = jedis.hmget(GameConstants.REDIS_HASH_VOTE+mapName, fields);
		JedisUtil.release(jedis);
		return result;
	}
	
	public static List<String> hgetAllValus(final String mapName){
		Jedis jedis = JedisUtil.getJedis();
		List<String> result = jedis.hvals(GameConstants.REDIS_HASH_VOTE+mapName);
		JedisUtil.release(jedis);
		return result;
	}
	/*
	 * 根据userId+""+voteId+""+voteIds[i]; 查询次数
	 */
	public static String hgetVoteRelVoteItemUser(final String mapName,final String key){
		Jedis jedis = JedisUtil.getJedis();
		String result = jedis.hget(GameConstants.REDIS_HASH_VOTE+mapName, key);
		JedisUtil.release(jedis);
		return result;
	}
	/**
	 * @param sortSetKey
	 * @param scoreMembers
	 * @return
	 */
	public static long zaddVoteRelVoteItemUser(final String sortSetKey,final Map<String,Double> scoreMembers){
		Jedis jedis = JedisUtil.getJedis();
		long count = jedis.zadd(GameConstants.REDIS_ZSET_VOTE+sortSetKey, scoreMembers);
		JedisUtil.release(jedis);
		return count;
	}
	
	public static void hsetVoteNumber(final String mapName,final String field,final String value){
		Jedis jedis = JedisUtil.getJedis();
		jedis.hset(GameConstants.REDIS_PATH+mapName, field, value);
		JedisUtil.release(jedis);
	}
	
	public static List<String> hgetAllVoteNumber(final String mapName){
		Jedis jedis = JedisUtil.getJedis();
		List<String> ls = jedis.hvals(GameConstants.REDIS_PATH+mapName);
		JedisUtil.release(jedis);
		return ls;
	}
	
	public static String hgetVoteNumber(final String mapName,final String key){
		Jedis jedis = JedisUtil.getJedis();
		String set = jedis.hget(GameConstants.REDIS_PATH+mapName, key);
		JedisUtil.release(jedis);
		return set;
	}
	/**
	 * @param sortKey
	 * @param members
	 * @return
	 */
	public static long zremVoteRelVoteItemUser(final String sortKey,String...members){
		Jedis jedis = JedisUtil.getJedis();
		long count = jedis.zrem(GameConstants.REDIS_ZSET_VOTE+sortKey, members);
		JedisUtil.release(jedis);
		return count;
	}
	/**
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public static Set<String> zrangebyscore(final String key,final String min,final String max ){
		Jedis jedis = JedisUtil.getJedis();
		Set<String> result = jedis.zrangeByScore(GameConstants.REDIS_ZSET_VOTE+key, min, max);
		JedisUtil.release(jedis);
		return result;
	}
	
	/**根据定时器插入数据库中
	 * @param key
	 */
	public static Map<String, String> hgetAllVoteRelVoteItemUser(final String key){
		Jedis jedis = JedisUtil.getJedis();
		Map<String, String> sets = jedis.hgetAll(GameConstants.REDIS_HASH_VOTE+key);
		JedisUtil.release(jedis);
		return sets;
	}
	/*
	 * 更新已经插入投票
	 */
	public static void hsetBsVoteByVoteId(final String key,final String field,final String value){
		Jedis jedis = JedisUtil.getJedis();
		jedis.hset(GameConstants.REDIS_PATH+key, field, value);
		JedisUtil.release(jedis);
	}
	/*
	 * 备份投票
	 */
	public static Map<String, String> hgetBackAllBsVote(final String key){
		Jedis jedis = JedisUtil.getJedis();
		Map<String, String> sets = jedis.hgetAll(GameConstants.REDIS_HASH_BACKUP_VOTE+key);
		JedisUtil.release(jedis);
		return sets;
	}
	/*
	 * 删除已经投票结束
	 */
	public static void hdelBsVoteByVotes(final String mapName,final String...strings ){
		Jedis jedis = JedisUtil.getJedis();
		jedis.hdel(GameConstants.REDIS_HASH_BACKUP_VOTE+mapName, strings);
		JedisUtil.release(jedis);
	}
	/*
	 * 查询评选项
	 */
	public static  Set<String>  zrangeByScoreVoteItem(final String key,final String min,final String max ){
		Jedis jedis = JedisUtil.getJedis();
		Set<String> result = jedis.zrangeByScore(GameConstants.REDIS_ZSET_VOTE+key, min, max);
		JedisUtil.release(jedis);
		return result;
	}
	/*
	 *  查询单个投票 
	 */
	public static BsRelVoteItemUser zrangeByScoreByVoteId(final String userId,final String voteId,final String itemId){
		Jedis jedis = JedisUtil.getJedis();
		Set<String> result = jedis.zrangeByScore(GameConstants.REDIS_ZSET_VOTE+BsRelVoteItemUser.class.getSimpleName(), voteId, voteId);
		JedisUtil.release(jedis);
		List<BsRelVoteItemUser> ls = JSONArray.parseArray(result.toString(), BsRelVoteItemUser.class);
		BsRelVoteItemUser itemUser = null;
		for(BsRelVoteItemUser bs :ls){
			if(String.valueOf(bs.getItemId()).equals(itemId) && String.valueOf(bs.getUserId()).equals(userId)&& String.valueOf(bs.getVoteId()).equals(voteId)){
				itemUser = bs;
			}
		}
		return itemUser;
	} 
	/*
	 * 更新删除
	 */ 	
	public static long zaddAndZremBsRel(BsRelVoteItemUser startBs,BsRelVoteItemUser endBs){
		Jedis jedis = JedisUtil.getJedis();
		/*
		 * 组装hashKey
		 */
		String hashKey = startBs.getUserId()+""+startBs.getVoteId()+""+startBs.getItemId();
		Map<String,Double> maps = new HashMap<String,Double>();
		maps.put(JSON.toJSONString(startBs),Double.valueOf(startBs.getVoteId()));
		/*
		 * 存储hash
		 */
		jedis.hset(GameConstants.REDIS_HASH_VOTE+BsRelVoteItemUser.class.getSimpleName(), String.valueOf(hashKey), JSON.toJSONString(endBs));
		/*
		 * 删除zset
		 */
		long result = jedis.zrem(GameConstants.REDIS_ZSET_VOTE+BsRelVoteItemUser.class.getSimpleName(), JSON.toJSONString(endBs));
		if(result == 1){
			/*
			 * 存储
			 */
			long count = jedis.zadd(GameConstants.REDIS_ZSET_VOTE+BsRelVoteItemUser.class.getSimpleName(), maps);
			JedisUtil.release(jedis);
			return  count;
			
		}
		JedisUtil.release(jedis);
		return 0;
	}
}
