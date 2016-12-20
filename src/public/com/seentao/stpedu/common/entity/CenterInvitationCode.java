package com.seentao.stpedu.common.entity;


public class CenterInvitationCode {

	private Integer dataId;
	
	private String invitationCode;
	
	private Integer status;
	
	private Integer useTime;
	
	private Integer regUserId;
	
	public CenterInvitationCode(){
		
	}
	public CenterInvitationCode(Integer status, Integer regUserId,Integer useTime){
		this.status = status;
		this.regUserId = regUserId;
		this.useTime = useTime;
	}
	public CenterInvitationCode(String invitationCode){
		this.invitationCode = invitationCode;
	}
	public Integer getDataId() {
		return dataId;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}
	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getUseTime() {
		return useTime;
	}

	public void setUseTime(Integer useTime) {
		this.useTime = useTime;
	}
	public Integer getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(Integer regUserId) {
		this.regUserId = regUserId;
	}

}
