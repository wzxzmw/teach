package com.seentao.stpedu.verificationcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.verificationcode.service.BusinessVerificationCodeService;

/** 
* @author yy
* @date 2016年6月27日 下午5:40:47 
*/
@Controller
public class VerificationCodeController implements IVerificationCodeController {
	@Autowired
	private BusinessVerificationCodeService businessVerificationCodeService;
	
	/* (non-Javadoc)
	 * @see com.seentao.stpedu.verificationcode.controller.IVerificationCodeController#submitIdentifyingCode(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	@ResponseBody
	@RequestMapping(value = "/submitIdentifyingCode")
	public String submitIdentifyingCode(Integer userType,String userName,String userId,String userToken,
			Integer iType,String phoneNumber,String clubId
			){
		return businessVerificationCodeService.submitIdentifyingCode(userType,userName,userId,phoneNumber.trim(),clubId,iType);
	}
}


