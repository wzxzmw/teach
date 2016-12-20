package com.seentao.stpedu.api.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public abstract class ParseJSON {


	/**
	 * 执行灯
	 */
	protected boolean mark = true;
	
	/**
	 * 返回对象
	 */
	protected Object result ;
	
	/**
	 * 起点api规范
	 */
	protected JSONObject API_START;
	
	/**
	 * 如果没有寻找到api的key，给定默认值 
	 */
	protected static final String DEFAULT_VAL = "";
	
	/**
	 * 本次 字段api规范：缩小解析范围
	 */
	protected JSONObject API_OBJ = new JSONObject();
	
	
	/**
	 * 清除返回对象
	 * 
	 * @author 			lw
	 * @date			2016年7月7日  下午4:21:11
	 */
	protected abstract void clearInput();

	/**
	 * 错误信息返回
	 */
	protected void error(String msg){
		this.result = msg;
	}
	
	/**
	 * 初始化对象
	 * @param API_START	函数级别api
	 * @param funcKey	函数名称
	 * @param inputObj	输入参数
	 * @return
	 * @author 			lw
	 * @date			2016年7月8日  下午4:33:25
	 */
	public abstract ParseJSON init(JSONObject API_START, String funcKey, Object inputObj);
	
	

	
	
	/**
	 * 执行程序：主驱动
	 * 
	 * @author 			lw
	 * @date			2016年7月7日  下午4:22:19
	 */
	public abstract ParseJSON execute();


	/**
	 * 获取结果集
	 * @return
	 * @author 			lw
	 * @date			2016年7月7日  下午4:59:23
	 */
	public Object getResult() {
		return this.result;
	}
	
	/**
	 * 清除返回对象
	 * 
	 * @author 			lw
	 * @date			2016年7月7日  下午4:19:18
	 */
	public void clean(){
		this.clearApi();
		this.clearInput();
		this.mark = true;
	}
	
	
	/**
	 * 清除api对象
	 * 
	 * @author 			lw
	 * @date			2016年7月7日  下午4:21:23
	 */
	private void clearApi() {
		this.API_OBJ.clear();
	}
	



	/**
	 * 数据解析分发器：
	 	 * 把数据分发给解析器。不同解析器有不同的解析方式.
	 	 * TODO 目前所有的解析器都写在本类(两种类型)，如果类型较多，可以考虑拆分
		 * 寻找结果：可能是对象，可能是数组，可能是字符串
		 * 获取输入对象，并组装成json规范值
	 * @param reObj_in				返回对象，在对象引用中最终只对字符串起作用，根据业务逻辑可以在obj和arr嵌套中不传入该对象，但是考虑方法的调用都引入的返回对象
	 * @param inputObj_in_obj 		接口函数对应结果集
	 * @param apiGetInputKey 		接口函数对应结果集对应api规范中的value。也是预定义的接口结果集传入对象key
	 * @return
	 * @author 					lw
	 * @date					2016年7月7日  下午5:31:09
	 */
	protected Object findValue(Object reObj_in, Object inputObj_in_obj, String apiGetInputKey) {
		
		reObj_in = inputObj_in_obj == null ? "" : inputObj_in_obj;
		
		/*
		 * 从API中获取key规范：
		 * 	1.在api中没有key映射的规范：直接返回基本对象。(这个是java基本类型数据)
		 * 	2.在api中还有key映射的规范：继续解析
		 */
		Object my_api = this.API_START.get(apiGetInputKey);
		if(my_api == null){
			return reObj_in;
		}
		
		if(my_api instanceof JSONObject){
			
			//如果返回集是 数组
			if(inputObj_in_obj instanceof JSONArray){
				
				return parseJSONArray(inputObj_in_obj, my_api, apiGetInputKey);
				
			//如果返回集是 对象
			}else if(inputObj_in_obj instanceof JSONObject){
				
				return parseJSONObject(inputObj_in_obj, my_api, apiGetInputKey);
				
			}
		}
		
		return reObj_in;
		
	}





	/**
	 * 对象驱动
	 * @param reObj_in				返回数据对象
	 * @param inputObj_in_obj		输入数据对象
	 * @param my_api				json规范中 对应key(apiGetInputKey) 的所有规范
	 * @param apiGetInputKey 		返回数据的key
	 * @return
	 * @author 			lw
	 * @date			2016年7月8日  上午9:46:48
	 */
	protected JSONObject parseJSONObject(Object inputObj_in_obj, Object my_api, String apiGetInputKey) {
		//如果结果集是空返回结果集
		if(inputObj_in_obj == null){
			return new JSONObject();
		}
		
		JSONObject tmpJsonObj = (JSONObject)inputObj_in_obj;		//函数结果集合
		
		//校验空对象
		if(tmpJsonObj.size() == 0){
			return tmpJsonObj;
		}
		
		JSONObject myApi = (JSONObject) my_api;						//本次规范api格式化
		JSONObject reObj = new JSONObject();						//返回对象集合对象
		JSONObject re_obj = null;									//返回对象中对象字段
		Set<String> tmpObj_keySet = tmpJsonObj.keySet();			//函数对象集合中对象的所有key
		
		//循环api组装数据
		for(Entry<String, Object> apiEn : myApi.entrySet()){
			re_obj = new JSONObject();
			apiGetInputKey = apiEn.getValue().toString();
			
			//寻找到对比的key
			if(tmpObj_keySet.contains(apiGetInputKey)){
				reObj.put(apiEn.getKey(), this.findValue(re_obj, tmpJsonObj.get(apiGetInputKey), apiGetInputKey));
			}else{
			//设置api默认key
				reObj.put(apiEn.getKey(), DEFAULT_VAL);
			}
		}
		return reObj;
	}





	/**
	 * 数组驱动
	 * @param reObj_in				返回数据对象
	 * @param inputObj_in_obj		输入数据对象
	 * @param my_api				json规范中 对应key(apiGetInputKey) 的所有规范
	 * @param apiGetInputKey 		返回数据的key
	 * @return
	 * @author 			lw
	 * @date			2016年7月8日  上午9:35:10
	 */
	protected JSONArray parseJSONArray(Object inputObj_in_obj, Object my_api, String apiGetInputKey) {
		//如果结果集是空直接返回空
		if(inputObj_in_obj == null){
			return new JSONArray();
		}
		
		JSONArray tmpJsonArr = (JSONArray) inputObj_in_obj;		//函数结果集合
		
		//校验是否是空对象
		if(tmpJsonArr.size() == 0){
			return tmpJsonArr;
		}
		
		JSONObject myApi = (JSONObject) my_api;					//本次规范api格式化
		JSONArray reObj = new JSONArray();						//返回数组集合对象
		JSONObject re_obj = null;								//返回数组中对象字段
		JSONObject tmpObj = null;								//函数结果集合中对象
		Set<String> tmpObj_keySet = null;						//函数对象集合中对象的所有key
		//循环集合数组
		for(Object tmp_In : tmpJsonArr){
			re_obj = new JSONObject();
			tmpObj = (JSONObject) tmp_In;
			tmpObj_keySet = tmpObj.keySet();
			
			//循环api组装数据
			for(Entry<String, Object> apiEn : myApi.entrySet()){
				apiGetInputKey = apiEn.getValue().toString();
				//寻找到对比的key
				if(tmpObj_keySet.contains(apiGetInputKey)){
					re_obj.put(apiEn.getKey(), this.findValue(re_obj, tmpObj.get(apiGetInputKey), apiGetInputKey));
				}else{
				//设置api默认key
					re_obj.put(apiEn.getKey(), DEFAULT_VAL);
				}
			}
			reObj.add(re_obj);
		}
		return reObj;
	}
	
	
	
	/**
	 * 深度复制
	 * 	在本次逻辑中已经抛弃，可以在其他地方进行克隆
	 * @param obj		赋值对象
	 * @return
	 * @author 			lw
	 * @date			2016年7月8日  上午10:27:41
	 */
	@Deprecated
	protected Object deepClone(Object obj) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			baos = new ByteArrayOutputStream(); 
			oos = new ObjectOutputStream(baos); 
			oos.writeObject(obj);
			bais = new ByteArrayInputStream(baos.toByteArray()); 
			ois = new ObjectInputStream(bais); 
		   return ois.readObject(); 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if(baos != null){
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				if(oos != null){
					oos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				if(baos != null){
					baos.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				if(ois != null){
					ois.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	   return null;
	 }
	
	
	
	
}
