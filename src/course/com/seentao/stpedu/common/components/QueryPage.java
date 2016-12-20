package com.seentao.stpedu.common.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.StringUtil;

public class QueryPage<T> {


	/**  每页显示数量 */
	private Integer limit=3;
	
	/** 当前页 */
	private Integer currentPage=1;
	
	/** 查询数据开始index */
	private Integer start=0;
	
	/** 查询数据里对象类型 */
	private Class<?> clazz;
	
	/** 查询对象集合 */
	private List<T> list = new ArrayList<T>();
	
	/** 总记录数*/
	private Integer count = 0;
	
	/** 操作状态 */
	private boolean state;
	
	/** 消息返回标记 */
	private String msgMark;
	
	/** 数据库查询参数 */
	private Map<String,Object> paraMap;
	
	/** redis数据展示存储 */
	private List<JSONObject> redisObj = new ArrayList<JSONObject>();
	
	public QueryPage(Integer limit, Integer start, Map<String,Object> paraMap,Class<T> t){
		if(limit > 0 ){
			this.limit = limit;
		}
		if(start >= 0 ){
			this.start = start;
		}
		this.clazz = t;
		this.paraMap = paraMap;
	}
	public QueryPage(){};
	
	/** 参数校验，跳出分页逻辑 */
	public boolean isJumpOut(){
		if(limit <= 0){
			return true;
		}else if(start < 0){
			return true;
		}else if(clazz == null){
			return true;
		}
		return false;
	}

	/** 初始化分页数据 */
	public Map<String,Object> getInitParam(){
		if(this.paraMap == null){
			this.paraMap = new HashMap<String,Object>();
		}
		this.paraMap.put("limit", this.limit);
		this.paraMap.put("offset", start);
		return this.paraMap;
	}
	
	/**
	 * 获取总页数
	 * @return
	 * @author 			lw
	 * @date			2016年6月19日  下午4:49:42
	 */
	public Integer getAllPage() {
		if(this.count>0 && this.limit>0){
			int tmp = this.count/this.limit;
			return (this.count%this.limit > 0) ? ++tmp : tmp ;
		}
		return 0;
	}

	/**
	 * 获取默认JSONObject
	 * 	返回映射 key有：
	 * 		AppErrorCode
	 * 		returnCount		总数量
	 * 		allPage			总页数
	 * 		currentPage		当前页
	 * 		listName		返回list对象名称
	 * @return
	 * @author 			lw
	 * @date			2016年6月19日  下午8:15:24
	 */
	public JSONObject getMessageJSONObject(String listName) {
		return this.getMessageJSONObject(listName, null); 
	}
	
	
	/**
	 * 获取默认JSONObject
	 * 	返回映射 key有：
	 * 		AppErrorCode
	 * 		returnCount		总数量
	 * 		allPage			总页数
	 * 		currentPage		当前页
	 * 		listName		返回list对象名称
	 * 		map				添加返回参数
	 * @return
	 * @author 			lw
	 * @param b 
	 * @date			2016年6月19日  下午8:15:24
	 */
	public JSONObject getMessageJSONObject(String listName, Map<String, Object> map) {
		JSONObject json = new JSONObject();
		json.put("returnCount", this.count);
		json.put("allPage", 	this.getAllPage());
		json.put("currentPage", this.currentPage);
		// TODO 使用新版api之后可以删除该三原运算
		json.put(listName, this.list == null ? new ArrayList<T>() : this.list);
		if(!CollectionUtils.isEmpty(map)){
			for(Entry<String, Object> en : map.entrySet()){
				json.put(en.getKey(), en.getValue());
			}
		}
		
		//校验是否是成功还是失败
		if(this.state){
			return Common.getReturn(AppErrorCode.SUCCESS, null, null, json);
		}else{
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, this.msgMark, null, json);
		}
	}
	
	/**
	 * 返回参数倒叙
	 * @return
	 * @author 			lw
	 * @date			2016年8月23日  上午10:39:35
	 */
	public QueryPage<T> resultListReverse(){
		Collections.reverse(this.list);
		return this;
	}

	
	public Integer getLimit() {
		return limit;
	}


	public Integer getCurrentPage() {
		if(this.start > 0 && this.limit >0){
			return (this.start/this.limit)+1;
		}
		return currentPage;
	}


	public Class<?> getClazz() {
		return clazz;
	}
	
	public String getClazzName(){
		return clazz.getSimpleName();
	}


	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}


	public List<T> getList() {
		return list;
	}


	public void setList(List<T> list) {
		this.list = list;
	}


	public void setCount(Integer count) {
		this.count = count;
	}
	
	public Integer getCount() {
		return this.count;
	}


	public Map<String, Object> getParaMap() {
		return paraMap;
	}


	public void setParaMap(Map<String, Object> paraMap) {
		this.paraMap = paraMap;
	}

	

	public void success() {
		this.state = true;
	}
	
	public void error(String strMsg) {
		this.state = false;
		this.msgMark = strMsg;
	}
	
	public boolean getState() {
		return this.state;
	}
	
	
	/**
	 * 检查是否需要查询数据库
	 * @return
	 * @author 			lw
	 * @param list2 
	 * @param ids 
	 * @date			2016年6月23日  下午3:05:44
	 */
	public boolean checkRedisObject(List<Integer> dataBaseDB, List<String> redisDB){
		
		//	1. 如果redis中没有数据，查询数据库
		if(CollectionUtils.isEmpty(redisDB)){
			return true;
		}
		
		//	2. 获取常量中维护的对象主键keyName
		String fileName = GameConstants.REDIS_AGREEMENT.getRedisObjectIdName(clazz);
		
		/*
		 * 3.对比校验：是够要查询数据库
		 * 	1.获取到key主键
		 * 	2.判断需要查询的ids长度 和 查询到数据的长度
		 */
		if(fileName != null){
			JSONObject json = null;
			
			//	3. 复制查询ids集合
			Integer[] array = dataBaseDB.toArray(new Integer[dataBaseDB.size()]);
			
			/*
			 * 	4. 校验数据
			 * 		4.1 校验redis数据，并保存到本地redis
			 * 		4.2校验redis数据，并保存redis数据转换成返回数据
			 * 		4.3校验redis数据，并删除需要查询的ids中的id
			 */
			for(String str : redisDB){
				if(StringUtil.isEmpty(str)){
					continue;
				}
				docheck(json, str, fileName, dataBaseDB);
			}
			
			
			//	4. 如果数据全清查询数据库
			if(CollectionUtils.isEmpty(dataBaseDB)){
				return false;
				
			//如果redis数据不全 ， 拿全部ids 查询数据库
			}else{
				this.paraMap.put(GameConstants.QUERYPAGE_IDS_KEY, StringUtil.arrToString(array, ","));
			}
		}
		
		return true;
	}
	

	
	
	/**
	 * 检查redis对象是否查询到需要的对象id,并把redis对象放入list返回对象集合
	 * @param str
	 * @param dataBaseDB
	 * @param selectDateBaseIds
	 * @return
	 * @author 			lw
	 * @param json 
	 * @param fileName 
	 * @date			2016年6月23日  下午3:37:27
	 */
	@SuppressWarnings("unchecked")
	private void docheck(JSONObject json, String str, String fileName, List<Integer> dataBaseDB) {
		json = JSONObject.parseObject(str);
		Integer redisID = Integer.valueOf(json.get(fileName).toString());
		if(dataBaseDB.contains(redisID)){
			redisObj.add(json);
			dataBaseDB.remove(redisID);
		}
		
		this.list.add((T) JSONObject.parseObject(str, clazz));
	}
	
	
	
	/**
	 * 判断查询目标数据在数据库是否存在
	 * @return
	 * @author 			lw
	 * @date			2016年7月7日  下午2:22:38
	 */
	public boolean hasData(){
		if(this.start < this.count){
			return true;
		}
		return false;
	}
	
	/**
	 * 保存差异数据到redis(数据库查询结果和redis中查询结果查询)
	 * @param list2
	 * @author 			lw
	 * @date			2016年7月10日  上午9:28:47
	 */
	public void setRedisMap(List<T> dbList) {
		
		//	1. redis保存格式化数据容器
		Map<String, String> upDataToRedis = new HashMap<String, String>();
		
		//	2. 获取当前对象的唯一标识id字段名称
		String fileName = GameConstants.REDIS_AGREEMENT.getRedisObjectIdName(clazz);
		JSONObject json ;
		String id = null;
		
		/*
		 * 	3. 数据对比并把差异对象保存到upDataToRedis保存结果容器
		 * 		1.redis查询到的数据和数据查询全部数据做比对，
		 * 		2.唯一标识不等的加入保存到redis
		 */
		for(T dbobj : dbList){
			
			//	3.1 如果redis未查询到数据，直接保存到结果集容器
			if(CollectionUtils.isEmpty(redisObj)){
				json = (JSONObject) JSONObject.toJSON(dbobj);
				if(json != null){
					id = json.getString(fileName);
					if(id != null ){
						upDataToRedis.put(id, json.toJSONString());
					}
				}
				continue;
			}
			
			//	3.2 如果redis查询到数据进行数据对比并保存到结果集容器
			for(JSONObject redis : redisObj){
				json = (JSONObject) JSONObject.toJSON(dbobj);
				if(json != null){
					id = json.getString(fileName);
					if(id != null && !json.getInteger(fileName).equals(redis.getInteger(fileName))){
						
						upDataToRedis.put(id, json.toJSONString());
						break;
					}
				}
			}
		}
		
		
		/*
		 * 	3.3	redis操作 
		 * 		1. 删除redis上结果集需要保存的数据key
		 * 		2. 并保存数据到数据库
		 */
		if(!CollectionUtils.isEmpty(upDataToRedis)){
			JedisCache.delRedisVal(clazz.getSimpleName(), upDataToRedis.keySet().toArray(new String[upDataToRedis.keySet().size()]));
			JedisCache.setRedisMap(clazz.getSimpleName(), upDataToRedis);
		}
	}

}
