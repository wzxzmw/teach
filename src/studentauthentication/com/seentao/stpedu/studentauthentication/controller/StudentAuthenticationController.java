package com.seentao.stpedu.studentauthentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.studentauthentication.service.StudentAuthenticationAppService;


/**
 * 学生认证控制器
 * @author 	lw
 * @date	2016年6月22日  下午2:57:43
 *
 */
@Controller
public class StudentAuthenticationController implements IStudentAuthenticationController {
	
	
	@Autowired
	private StudentAuthenticationAppService appService;
	
	
	/**
	 * 学生的证书认证操作
	 * @param userId			用户id
	 * @param certObjectId		认证对象id
	 * @param actionType		认证操作
	 * @return
	 * @author 					lw
	 * @date					2016年6月22日  下午2:59:18
	 */
	@Override
	@ResponseBody
	public String submitCertRequest(Integer userId, Integer certObjectId, Integer actionType){
		if(userId == null || certObjectId == null || actionType == null){
			return Common.getReturn(AppErrorCode.ERROR_TYPE_TWO, AppErrorCode.ERROR_ACTIONTYPE_REQUEST_PARAM).toJSONString();
		}
		return appService.submitCertRequest(userId,certObjectId,actionType);
	}

}
