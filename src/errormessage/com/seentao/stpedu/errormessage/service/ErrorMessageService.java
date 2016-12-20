package com.seentao.stpedu.errormessage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seentao.stpedu.common.service.BasePromptInfoService;

@Service
public class ErrorMessageService {

	@Autowired
	private BasePromptInfoService service;
	

	/**
	 * 通过错误编码获取错误消息
	 * @param webResult
	 * @param msg
	 * @return
	 * @author 			lw
	 * @date			2016年7月28日  下午8:34:57
	 */
	public String tellMeMessage(String msg){
		return service.tellMeMessage(msg);
	}
	
}
