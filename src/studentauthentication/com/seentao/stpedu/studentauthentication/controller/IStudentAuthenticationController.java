package com.seentao.stpedu.studentauthentication.controller;

public interface IStudentAuthenticationController {

	
	/**
	 * 学生的证书认证操作
	 * @param userId			用户id
	 * @param certObjectId		认证对象id
	 * @param actionType		认证操作
	 * @return
	 * @author 					lw
	 * @date					2016年6月22日  下午2:59:18
	 */
	public String submitCertRequest(Integer userId, Integer certObjectId, Integer actionType);
}
