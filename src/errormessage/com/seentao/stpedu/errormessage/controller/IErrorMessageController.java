package com.seentao.stpedu.errormessage.controller;

public interface IErrorMessageController {

	
	/**
	 * 通过错误编码获取错误消息
	 * @param webResult
	 * @param msg
	 * @return
	 * @author 			lw
	 * @date			2016年7月28日  下午8:34:57
	 */
	public abstract String tellMeMessage(String msg);
}
