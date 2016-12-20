package com.seentao.stpedu.errormessage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.errormessage.service.ErrorMessageService;

@Controller
public class ErrorMessageController implements IErrorMessageController {

	
	@Autowired
	private ErrorMessageService service;
	
	
	
	/**
	 * 通过错误编码获取错误消息
	 * @param webResult
	 * @param msg
	 * @return
	 * @author 			lw
	 * @date			2016年7月28日  下午8:34:57
	 */
	@Override
	@ResponseBody
	public String tellMeMessage(String msg){
		return service.tellMeMessage(msg);
	}
	
	
}
