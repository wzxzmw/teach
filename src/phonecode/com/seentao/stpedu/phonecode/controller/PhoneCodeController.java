package com.seentao.stpedu.phonecode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.phonecode.service.PhoneCodeService;

/** 
* @ClassName: phoneCodeController 
* 发送短信验证码 
* @author W.jx
* @date 2016年6月29日 下午5:35:30 
*  
*/
@Controller
public class PhoneCodeController {
	@Autowired
	private PhoneCodeService phoneCodeService;
	
	
	/** 
	* 发送验证码
	* @author W.jx
	* @date 2016年6月30日 下午3:31:58 
	* @param phone 手机号
	* @param note 短信内容
	* @return
	*/
	@RequestMapping("/sendPhoneCode")
	@ResponseBody
	public String sendPhoneCode(String phone, String note){
		return phoneCodeService.getPhoneCode(phone, note);
	}
	
}
