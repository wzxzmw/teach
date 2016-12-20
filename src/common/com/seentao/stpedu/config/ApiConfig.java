package com.seentao.stpedu.config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @ClassName: ApiConfig 
 * @Description: 获取API参数类
 * @author zhengchunlei
 * @date 2016年5月31日 下午2:58:13
 *
 */
public class ApiConfig {
	private static String apiJson = "";
	private static String jsonFile = "";
	
	public static void getJsonFile(String json){
		jsonFile = json;
	}
	/**
	 * 根据接口名称获取接口参数
	 * @param key 接口名称
	 * @return
	 */
	public static JSONObject getObject(String key){
		JSONObject jsonReturn = new JSONObject();
		InputStream in = ApiConfig.class.getClassLoader().getResourceAsStream(jsonFile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		try {
			while ((i = in.read()) != -1) {
				baos.write(i);
			}
			apiJson = baos.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			jsonReturn.put("code", 1);
			jsonReturn.put("msg", "从文件里获取数据失败");
			return jsonReturn;
		}
		
		
		try{
			JSONObject ja = JSONObject.parseObject(apiJson);
			jsonReturn.put("code", 0);
			jsonReturn.put("result", ja.get(key));
		}catch(Exception e){
			//json格式不正确
			jsonReturn.put("code", 1);
			jsonReturn.put("msg", "json格式不正确");
			return jsonReturn;
		}
		return jsonReturn;
	}
	
	
	
}
