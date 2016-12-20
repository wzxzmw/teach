package com.seentao.stpedu.verificationcode.controller;

/** 
* @author yy
* @date 2016年7月5日 下午9:31:38 
*/
public interface IVerificationCodeController {

	/**
	 * 发送验证码
	 * @param userType	用户类型
	 * @param userName	用户名
	 * @param userId	用户id
	 * @param userToken 用户唯一标识
	 * @param clubId 俱乐部id
	 * @param phoneNumber 手机号码
	 * @param iType 验证类型(1:注册；2:密码找回；3:申请提现；4:绑定手机号；)
	 */
	String submitIdentifyingCode(Integer userType, String userName, String userId, String userToken, Integer iType,
			String phoneNumber, String clubId);

}
