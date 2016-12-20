package com.seentao.stpedu.common.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;

/**
 * 递归查询结果对象
 * @author 	lw
 * @date	2016年6月22日  上午10:07:26
 *
 */
public class RedisAndDBBean<T>{
	
	
	//执行状态
	private boolean isSuccess = false;
	
	//返回参数
	private T resultBean;
	
	//返回错误消息
	private JSONObject msg;
	
	//参数查询id
	String id;
	
	//数据库查询参数传递集合
	HashMap<String, Object> paramMap;
	
	//当前查询索引
	int index;
	
	//redis查询规范数组
	GameConstants.REDIS_AGREEMENT[]  redisEnum;
	
	/** 查询所有结果集合 */
	private List<String> selectList = new ArrayList<String>(); 
	
	public RedisAndDBBean() {
		super();
	}
	
	public  RedisAndDBBean(String id, HashMap<String, Object> paramMap, int index ,GameConstants.REDIS_AGREEMENT...  redisEnum){
		this.id = id;
		this.paramMap = paramMap;
		this.index = index;
		this.redisEnum = redisEnum;
		paramMap.put("id_key", Integer.valueOf(id));
	}
	
	/**
	 * 获取当前查询对象类型 class
	 * @return
	 * @author 			lw
	 * @date			2016年6月22日  上午10:31:00
	 */
	public String getIndexClassSimpleName(){
		return redisEnum[this.index].getClazz().getSimpleName();
	}
	
	
	/**
	 * 获取结果返回类型
	 * @return
	 * @author 			lw
	 * @date			2016年6月22日  上午10:41:32
	 */
	public Class<?> getReturnBeanClass(){
		return redisEnum[redisEnum.length-1].getClazz();
	}
	
	/**
	 * 获取当前查询对象查询列 名称
	 * @return
	 * @author 			lw
	 * @date			2016年6月22日  上午10:31:00
	 */
	String getIndexFieldName(){
		return redisEnum[this.index].getId_FieldName();
	}
	
	String getIndexSelectError(){
		return redisEnum[this.index].getSelect_Error();
	}
	 
	/**
	 * 是否是查询目标（最后一个查询对象）
	 * @return
	 * @author 			lw
	 * @date			2016年6月22日  上午10:30:48
	 */
	public boolean isLast(){
		if(redisEnum.length == (index+1)){
			return true;
		}
		return false;
	}
	
	public RedisAndDBBean<T> next(JSONObject parseObject){
		//改变查询角标
		this.index++;
		//改变查询参数
		this.paramMap.clear();
		Integer idData = parseObject.getInteger(this.getIndexFieldName());
		if(idData != null){
			this.paramMap.put("id_key", idData);
			this.id = idData.toString() ;
		}
		return this;
	}
	
	/**
	 * 成功
	 * @param resultBean
	 * @author 			lw
	 * @date			2016年6月22日  上午10:31:16
	 */
	public RedisAndDBBean<T> success (T resultBean){
		this.resultBean = resultBean;
		this.isSuccess = true;
		return this;
	}
	
	/**
	 * 失败
	 * @param msg
	 * @author 			lw
	 * @date			2016年6月22日  上午10:31:22
	 */
	public RedisAndDBBean<T> error (){
		this.isSuccess = false;
		this.msg = Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, this.getIndexSelectError());
		return this;
	}
	
	public RedisAndDBBean<T> runtimeException (){
		this.isSuccess = false;
		this.msg = Common.getReturn(AppErrorCode.ERROR_REDISCOMPONENT_RUNTIMEEXCEPTION, this.getIndexSelectError());
		return this;
	}

	public T getResultBean() {
		return resultBean;
	}

	public JSONObject getMsg() {
		return msg;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	public void setParamMap(HashMap<String, Object> paramMap) {
		this.paramMap = paramMap;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public GameConstants.REDIS_AGREEMENT[] getRedisEnum() {
		return redisEnum;
	}

	public void setRedisEnum(GameConstants.REDIS_AGREEMENT[] redisEnum) {
		this.redisEnum = redisEnum;
	}

	public List<String> getSelectList() {
		return selectList;
	}

	public void setSelectList(List<String> selectList) {
		this.selectList = selectList;
	}
	
	
}
