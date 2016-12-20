package com.seentao.stpedu.common.entity;

import com.seentao.stpedu.constants.BusinessConstant;

public class CenterSmsVerification {

	private Integer verificationId;
	
	private String verificationCode;
	
	private String phone;
	/*
	 * 验证码类型。1:注册验证码，
	 * 			2:手机绑定验证码，
	 * 			3:密码找回验证码，
	 * 			4:提现验证码。
	 */
	private Integer type;
	
	private Integer sendTime;
	
	private Integer todaySendNum;
	
	public CenterSmsVerification(){
		
	}
	
	public CenterSmsVerification(String phone,String verificationCode,Integer type){
		this.phone = phone;
		this.verificationCode = verificationCode;
		this.type = type;
	}
	public Integer getVerificationId() {
		return verificationId;
	}

	public void setVerificationId(Integer verificationId) {
		this.verificationId = verificationId;
	}
	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getSendTime() {
		return sendTime;
	}

	public void setSendTime(Integer sendTime) {
		this.sendTime = sendTime;
	}

	public Integer getTodaySendNum() {
		return todaySendNum;
	}

	public void setTodaySendNum(Integer todaySendNum) {
		this.todaySendNum = todaySendNum;
	}

	
	 
	
}
