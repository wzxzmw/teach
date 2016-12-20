package com.seentao.stpedu.common.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.factory.ClassLoaderBeanFactory;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.StringUtil;

/**
 * redis简单逻辑封装
 * @author 	lw
 * @date	2016年6月19日  下午8:45:04
 * 缓存定义：
 * 分为两种：
 * 一:单表实体存放规则
 * stpedu:TeachClass+map
 * 二:接口对应的业务缓存存放规则
 * stpedu:12+map
 */
@Component
public class RedisComponent{
	
	
	
	/**
	 * 重定义redis查询方法
	 * @param id
	 * @param t
	 * @param selectForDBEntityMethodName
	 * @return
	 * @author 			lw
	 * @date			2016年6月23日  下午5:23:41
	 */
	public static <T> String findRedisObjectByMethodName(Integer id,Class<T> t , String selectForDBEntityMethodName){
		return findRedisObjectExecute(id, new HashMap<String ,Object>(), t, selectForDBEntityMethodName);
	}
	
	
	
	
	
	/**
	 * 操作步骤：redis对象查询
	 * 	每个Dao只能实现一种查询
	 * 	执行过程
	 * 	1.使用 t.getSimpleName 类名 查询redis 的 hashmap
	 * 	2.查询hashmap 的 key 是 id
	 * 	3.如果在redis中未查询到对象，使用类加载器 反射获取  t 类型 Dao
	 * 	4.执行 Dao 类 getEntityForDB 方法查询对象
	 * 	5.getEntityForDB 查询在 t.xml中自定义实现
	 * 
	 * 		当初在设计该方法使用到反射，原来是定义了约定接口.使用本方法需要实现指定类Dao中某些方法。但是遭到强烈反对。
	 * 		但是思前想后还是觉得不能这样，愿后来的维护者能够把该功能实现。
	 * 		
	 * @param id							redis查询key
	 * @param selectParam					数据库查询传入参数
	 * @param methodName					查询数据库entity方法
	 * @param t								使用 t 类型的dao 和 xml
	 * @return
	 * @author 								lw
	 * @date								2016年6月20日  上午9:13:46
	 */
	public static <T> String findRedisObjectExecute(Integer id,Map<String,Object> selectParam,Class<T> t , String methodName){
		
		// 1. 校验redis查询key
		if(t == null || id == null){
			return null;
		}
		
		try {
			
			/*
			 * 	2. 使用工具查询redis数据
			 * 		2.1 规范：
			 * 			a.传入库名：t.getSimpleName() 某个持久化的简单实体名
			 * 			b.id字符串类型
			 */
			String redisEntity = JedisCache.getRedisVal(null, t.getSimpleName(),String.valueOf(id));
			
			//	3. 如果redis中查询到数据直接返回结果字符串
			if(!StringUtil.isEmpty(redisEntity)){
				return redisEntity;
			}else{
				
				/*
				 * 	4. 反射查询数据库
				 * 	
				 * 	使用反射查询：
				 * 		1.考虑到数据库查询的方式可能不一样。
				 * 		2.考虑到使用过程中可能不是直接查询数据库，可能存在其他个性化逻辑
				 * 		3.考虑到一个类中可能会自定多个redis查询的方法
				 */
				selectParam.put("id_key", id);
				Class<?> forName = Class.forName(GameConstants.REDIS_PPATH_PACKAGE + t.getSimpleName() + "Dao");
				Object bean = ClassLoaderBeanFactory.getBean(forName);
				
				/*
				 * 	5. 方法调用：
				 * 		5.1 默认调用方法名为 GameConstants.REDIS_QUERY_ENTITY 字符串的方法
				 * 		5.2 如果传入 methodName 方法名称使用该自定义方法
				 * 		5.2 方法参数都是 ClassLoaderBeanFactory.CLASS_ARR
				 */
				Object dbEntity = ClassLoaderBeanFactory.executiveMethod(bean, forName, methodName == null ? GameConstants.REDIS_QUERY_ENTITY : methodName	//
						, ClassLoaderBeanFactory.CLASS_ARR, selectParam);
				
				// 6. 如果查询到数据，保存到redis中
				if(dbEntity != null && !StringUtil.isEmpty(dbEntity.toString())){
					JedisCache.setRedisVal(null, t.getSimpleName(), String.valueOf(id), JSON.toJSON(dbEntity).toString());
				}
				
				// 7. 返回结果
				return dbEntity == null ? null : JSON.toJSONString(dbEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("RedisComponent类使用" + t.getSimpleName() + "类加载失败!请检查类对象路径.");
			return Common.getReturn(AppErrorCode.ERROR_REDISCOMPONENT_RUNTIMEEXCEPTION, t.getSimpleName()).toJSONString();
		}
	}
	
	
	
	
	/**
	 * redis带参查询
	 * @param id			redis查询key
	 * @param selectParam	数据库查询传入参数
	 * @param t				查询对象数据库	在该对象DAO中需要实现getEntityForDB 查询方法
	 * @return
	 * @author 				lw
	 * @date				2016年6月20日  下午5:31:31
	 */
	public static <T> String findRedisObject(Integer id,Map<String,Object> selectParam,Class<T> t){
		return findRedisObjectExecute(id, selectParam, t , null);
	}
	
	
	
	
	
	/**
	 * 简单查询
	 * @param id			redis查询key
	 * @param t				查询对象数据库	在该对象DAO中需要实现getEntityForDB 查询方法
	 * @return
	 * @author 				lw
	 * @date				2016年6月20日  下午5:31:31
	 */
	public static <T> String findRedisObject(Integer id,Class<T> t){
		return findRedisObjectExecute(id, new HashMap<String, Object>(), t , null);
	}
	
	/**
	 * 条件查询redis图片对象方法：
	 * 	 id == null ? 查询key = type ：  查询key = id
	 * 图片压缩
	 * @param id			redis查询key
	 * @param type			预设定key
	 * @return
	 * @author 				lw
	 * @date				2016年6月20日  下午5:31:31
	 */
	public static <T> String findRedisObjectPublicPicture(Integer id, int type){
		String img = findRedisObjectExecute( (id == null || id <= 0) ? type : id, new HashMap<String, Object>(), PublicPicture.class , null);
		if(StringUtil.isEmpty(img)){
    		PublicPicture pp = JSONObject.parseObject(img, PublicPicture.class);
    		if(null != pp){
//    			img = pp.getDownloadUrl()+BusinessConstant.OSS_RATE;
    			/*
    			 * 压缩图片 2016-11-21  wangzx 图片统一在出口压
    			 */
    			img = pp.getDownloadUrl();
    		}
    	}
		return img;
	}
	
	
	
	
	
	/**
	 * redis递归查询
	 * 
	 * @param id			初始查询redis 对象 唯一标识
	 * @param t				查询对象数据库	在该对象DAO中需要实现getEntityForDB 查询方法
	 * @param redisEnum
	 * @return
	 * @author 				lw
	 * @date				2016年6月22日  上午10:51:19
	 */
	public static <T> RedisAndDBBean<T> recursionRedisObjectExecute(Integer id,Class<T>  t,GameConstants.REDIS_AGREEMENT... redisEnum){
		RedisAndDBBean<T> redisAndDBBean = new RedisAndDBBean<T>(String.valueOf(id), new HashMap<String, Object>(), 0, redisEnum);
		return findRedisObjectExecute(redisAndDBBean);
				
	}
	
	
	
	

	/**
	 * redis递归查询对象
	 * 
	 * 		当初在设计该方法使用到反射，原来是定义了约定接口.使用本方法需要实现指定类Dao中某些方法。但是遭到强烈反对。
	 * 		但是思前想后还是觉得不能这样，愿后来的维护者能够把该功能实现。
	 * @param queryBean		查询消息装载对象
	 * @return
	 * @author 				lw
	 * @date				2016年6月22日  上午10:51:07
	 */
	@SuppressWarnings("unchecked")
	static <T> RedisAndDBBean<T> findRedisObjectExecute(RedisAndDBBean<T> queryBean){
		
		// 1. 校验递归查询起始查询KEY
		if(StringUtil.isEmpty(queryBean.getId())){
			return queryBean.error();
		}
		
		try {
			/*
			 *	2.查询当前指针上的redis数据
			 *		1.在RedisAndDBBean(查询消息装载对象)中封装了查询顺序列表
			 *		2.在RedisAndDBBean中封装了查询指针
			 *		3.在RedisAndDBBean中封装了当前查询对象key
			 */
			String redisEntity = JedisCache.getRedisVal(null, queryBean.getIndexClassSimpleName(),queryBean.getId());
			if(!StringUtil.isEmpty(redisEntity)){
				
				/*
				 * 	2.1. 所有查询过程中的结果保存起来
				 * 		在使用的过程中，可能需要使用到查询的任意结果数据
				 */
				queryBean.getSelectList().add(redisEntity);
				
				/*
				 * 	2.2. 校验是否查询到最后一个需要的目标对象：
				 * 		2.2.1 true:直接返回查询到的结果
				 * 		2.2.2 false：指针向下移动，继续递归查询
				 */
				if(queryBean.isLast()){
					return queryBean.success((T) JSONObject.parseObject(redisEntity, queryBean.getReturnBeanClass()));
				}
				return findRedisObjectExecute(queryBean.next(JSONObject.parseObject(redisEntity)));
				
			}else{
				
				/*
				 * 3.反射查询数据库
				 * 	1.调用固定路径下的某个dao 
				 * 	2.调用这个dao的GameConstants.REDIS_QUERY_ENTITY方法
				 * 	3.这个方法参数是ClassLoaderBeanFactory.CLASS_ARR
				 * 	4.传入数据是 queryBean.getParamMap()
				 */
				Class<?> forName = Class.forName(GameConstants.REDIS_PPATH_PACKAGE + queryBean.getIndexClassSimpleName() + "Dao");
				Object bean = ClassLoaderBeanFactory.getBean(forName);
				
				// 4. 方法调用
				Object dbEntity = ClassLoaderBeanFactory.executiveMethod(bean, forName, GameConstants.REDIS_QUERY_ENTITY, ClassLoaderBeanFactory.CLASS_ARR, queryBean.getParamMap());
				
				// 5. 数据整理
				if(dbEntity != null && !StringUtil.isEmpty(dbEntity.toString())){
					
					//	5.1.1 如果查询到数据放到redis中
					String jsonStr = JSON.toJSON(dbEntity).toString();
					JedisCache.setRedisVal(null, queryBean.getIndexClassSimpleName(), queryBean.getId(), jsonStr);
					
					//	5.1.2 保存数据到临时数据集合
					queryBean.getSelectList().add(jsonStr);
					
				}else{
					
					//	5.2.1 如果没有查询数据给出默认值null
					queryBean.getSelectList().add(null);
					
					//	5.2.1 数据库中未查询到 处理 ： 如果是查询目标对象未查到返回 null ，否则返回消息
					if(queryBean.isLast()){
						return null;
					}
					
					return queryBean.error();
				}

				//	6. 查询到对象 处理: 如果是查询目标对象 返回对象 ,否者指针下移并继续递归
				if(queryBean.isLast()){
					return queryBean.success((T) dbEntity);
				}
				return findRedisObjectExecute(queryBean.next((JSONObject) JSONObject.toJSON(dbEntity)));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("RedisComponent.findRedisObjectExecute 递归redis查询方法 出错，出错参数循环：" + queryBean.getIndexClassSimpleName());
			return queryBean.runtimeException();
		}
	}
	
}
