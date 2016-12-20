package com.seentao.stpedu.hprose;

import java.io.IOException;

import com.seentao.stpedu.utils.LogUtil;

import hprose.client.HproseClient;
import hprose.client.HproseHttpClient;

public class HproseRPC {

	/**
	 * Hprose远程方法调用
	 * @param url 地址
	 * @param methodName 方法名
	 * @param args 方法执行的参数，没有传null
	 * @return 方法执行返回值
	 */
	public static Object invoke(String url, String methodName, Object[] args){
		Object result = null;
		HproseClient client = new HproseHttpClient();
		client.useService(url);
		try {
			if(args == null){
				result = client.invoke(methodName);
			} else {
				result = client.invoke(methodName, args);
			}
			LogUtil.info(HproseRPC.class, "invoke", "远程调用：" + methodName + "成功");
		} catch (IOException e) {
			LogUtil.error(HproseRPC.class, "invoke", "远程调用：" + methodName + "失败", e);
		}
		return result;
	}
	
	/**
	 * Hprose远程方法调用
	 * @param url 服务端暴露的链接
	 * @param type 接口的类型
	 * @return 接口的实例
	 */
	public static <T> T getRomoteClass(String url, Class<T> type){
		HproseClient client = new HproseHttpClient(url);
		 return client.useService(type);
	}
}
