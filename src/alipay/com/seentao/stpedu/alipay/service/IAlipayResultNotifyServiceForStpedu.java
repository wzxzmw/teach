package com.seentao.stpedu.alipay.service;

import com.alibaba.fastjson.JSONArray;

public interface IAlipayResultNotifyServiceForStpedu {
	
	/** 
	* 往充值表中插入记录，返回客户端需要发给支付宝的信息
	* @author W.jx
	* @date 2016年6月28日 下午9:11:16 
	* @param outTradeNo 流水号
	* @param rechargeAmount 订单金额
	* @return
	*/
	JSONArray subOrder(String outTradeNo, Double rechargeAmount);
	/** 
	* 支付宝同步通知
	* @author W.jx
	* @date 2016年6月28日 下午9:49:12 
	* @param params
	* @return
	*/
	String zfbCallBack(String out_trade_no, String trade_no, String trade_status, String seller_id, String total_fee,
			String buyer_email, String notify_time);

	/** 
	* 支付宝异步通知 
	* @author W.jx
	* @date 2016年6月28日 下午9:49:32 
	* @param params
	* @return
	*/
	String zfbAsynchronizationCallBack(String out_trade_no, String trade_no, String trade_status, String seller_id,
			String total_fee, String buyer_email, String notify_time);

}