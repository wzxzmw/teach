package com.seentao.stpedu.common.interfaces;

import com.alibaba.fastjson.JSONArray;

public interface IAlipayServiceInterface {
	
	/** 
	* 获取支付宝需要的字段 
	* @author W.jx
	* @date 2016年7月27日 下午7:40:57 
	* @param outTradeNo 流水号
	* @param rechargeAmount 金额
	* @return
	*/
	JSONArray subOrder(String outTradeNo, Double rechargeAmount);
}
