package com.seentao.stpedu.common.entity;


public class PublicSmsSendCount {

	private Long dataId;
	
	private String phone;
	
	private String sendTime;
	
	private Integer sendNum;
	
	private Integer sendSumNum;
	

	public Long getDataId() {
		return dataId;
	}

	public void setDataId(Long dataId) {
		this.dataId = dataId;
	}
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public Integer getSendNum() {
		return sendNum;
	}

	public void setSendNum(Integer sendNum) {
		this.sendNum = sendNum;
	}
	public Integer getSendSumNum() {
		return sendSumNum;
	}

	public void setSendSumNum(Integer sendSumNum) {
		this.sendSumNum = sendSumNum;
	}

}
