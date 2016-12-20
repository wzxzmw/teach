package com.seentao.stpedu.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.dao.BasePromptInfoDao;
import com.seentao.stpedu.common.entity.BasePromptInfo;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.StringUtil;

@Service
public class BasePromptInfoService{
	
	@Autowired
	private BasePromptInfoDao basePromptInfoDao;
	
	public String getBasePromptInfo(Integer id) {
		BasePromptInfo basePromptInfo = new BasePromptInfo();
		basePromptInfo.setInfoId(id);
		List<BasePromptInfo> basePromptInfoList = basePromptInfoDao .selectSingleBasePromptInfo(basePromptInfo);
		if(basePromptInfoList == null || basePromptInfoList .size() <= 0){
			return null;
		}
		
		return JSONArray.toJSONString(basePromptInfoList);
	}
	
	
	
	
	/**
	 * 获取错误消息
	 * @param id		错误码
	 * @return
	 * @author 			lw
	 * @date			2016年7月16日  下午5:29:10
	 */
	public BasePromptInfo getBasePromptInfoEntity(BasePromptInfo basePromptInfo){
		basePromptInfo = basePromptInfoDao.selectBasePromptInfo(basePromptInfo);
		if(basePromptInfo != null && !StringUtils.isEmpty(basePromptInfo.getContent())){
			return basePromptInfo;
		}
		return null;
	}



	/**
	 * 获取错误消息：	如果没有获取到消息，返回错误编码!
	 * @param msg		错误编码
	 * @author 			lw
	 * @date			2016年7月16日  下午7:41:21
	 */
	public String tellMeMessage(String msg) {
		
		
		BasePromptInfo errorInfo = this.findErrorBeanToRedis(msg);
		
		
		//如果数据库存在消息信息，返回该消息的描述
		if(errorInfo != null){
			return errorInfo.getContent();
			
		//如果数据库不存在该消息，返回该消息的原始信息
		}else{
			return msg;
		}
		
	}
	
	
	/**
	 * api解析消息码逻辑：
	 * 1.查询redis ，如果有数据返回对象
	 * 2.查询数据库，如果有数据返回对象
	 * 3.如果有没有返回查询消息码到页面
	 * 
	 * @param code		消息码
	 * @return
	 * @author 			lw
	 * @date			2016年7月17日  上午10:55:22
	 */
	public BasePromptInfo findErrorBeanToRedis(String  code){
		
		BasePromptInfo dbParam = null;
		
		//通过 code 从redis中获取数据
		String redis = JedisCache.getRedisVal(null, BasePromptInfo.class.getSimpleName(), code);
		
		//如果没有查询到，查询数据库
		if(StringUtil.isEmpty(redis)){
			
			dbParam = new BasePromptInfo();
			dbParam.setCode(code);
			dbParam = this.getBasePromptInfoEntity(dbParam);
			
			if(dbParam != null){
				
				JedisCache.setRedisVal(null, BasePromptInfo.class.getSimpleName(), dbParam.getCode(), JSONObject.toJSONString(dbParam));
				return dbParam;
			}
			
		//如果查询到了返回
		}else{
			
			dbParam = JSONObject.parseObject(redis, BasePromptInfo.class);
			if(dbParam != null){
				
				return dbParam;
			}
		}
		
		//条件不满足返回查询条件到消息	
		dbParam = new BasePromptInfo();
		dbParam.setContent(code);
		return dbParam;
	}
}