package com.seentao.stpedu.api.controller;

import com.alibaba.fastjson.JSONObject;

/**
 * json对象对比
 * @author 	lw
 * @date	2016年7月7日  下午4:24:10
 *
 */
public class ParseJSONObject extends ParseJSON{

	//传入对象数据
	private JSONObject inputObj = new JSONObject();
	
	@Override
	public ParseJSON init(JSONObject API_START, String funcKey, Object inputObj) {
		super.API_START = API_START;
		super.API_OBJ 	= (JSONObject) API_START.get(funcKey);
		this.inputObj 	= (JSONObject) inputObj;
		if(this.API_OBJ.isEmpty()){
			this.mark = false;
			super.error("函数名：[ "+funcKey+" ] 的API未找到!");
		}
		return this;
	}

	/**
	 * 本类方法执行入口
	 * 
	 * TODO 拓展
	 * 如果以后返回类型组件拓展超过4总类型：目前3种组件类型.	1.基本类型+String 2.对象类型 3.数组类型
	 * 可以尝试在此方法中调用父类的执行子类实现方法： 在父类中的super.parseJSONArray 方法引用到new 对象的子类实现。
	 * 这样可以组件结构清晰的添加不同类型 返回数据 组件
	 * 
	 */
	@Override
	public ParseJSON execute() {
		
		//初始化成功
		if(super.mark){
			super.result = super.parseJSONObject(this.inputObj, super.API_OBJ, new String());
		}else{
		}
		
		return this;
	}

	
	@Override
	protected void clearInput() {
		this.inputObj.clear();
	}


}
