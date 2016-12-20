package com.seentao.stpedu.phonecode.service;

public interface IPhoneMessagesService {

	/** 
	* 发送短信 
	* @author W.jx
	* @date 2016年7月28日 下午7:01:50 
	* @param phone 手机号
	* @param note 短信内容
	* @return
	*/
	String sendPhoneNote(String phone, String note);

}