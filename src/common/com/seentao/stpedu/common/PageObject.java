package com.seentao.stpedu.common;

import com.alibaba.fastjson.JSONObject;

/** 
* @author yy
* @date 2016年7月9日 下午3:58:37 
*/
public class PageObject {
	
	/**
	 * @param count  记录总数
	 * @param start 开始截取位置
	 * @param limit 截取数量
	 * @param obj 查询的分页对象
	 * @param key 分页对象的key
	 * @return
	 */
	public static JSONObject getPageObject(Integer count,Integer start,Integer limit ,Object obj,String key){
		JSONObject jo = new JSONObject();
		jo.put("returnCount", count);
		if(count==0){
			jo.put("allPage", 0);
			jo.put("currentPage", 0);
		}else if(start==0 && limit==0){
			jo.put("allPage", 0);
			jo.put("currentPage", 0);
		}else{
			int allPage=(count-1)/limit+1;
			jo.put("allPage", allPage);
			jo.put("currentPage", (start/limit)+1);
		}
		Object json = JSONObject.toJSON(obj);
		jo.put(key, json);
		return jo;
	}
}


