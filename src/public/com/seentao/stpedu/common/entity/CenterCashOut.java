package com.seentao.stpedu.common.entity;



public class CenterCashOut {

	private Long cashOutId;
	
	private Integer createUserId;
	
	private Integer createTime;
	
	private Integer cashOutType;
	
	private Double amount;
	
	private Integer status;
	
	private Double virtualAmount;
	
	private String account;
	
	private String serialNumber;
	
	private String reason;
	
	private String errorInfo;
	

	public Long getCashOutId() {
		return cashOutId;
	}

	public void setCashOutId(Long cashOutId) {
		this.cashOutId = cashOutId;
	}
	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	public Integer getCashOutType() {
		return cashOutType;
	}

	public void setCashOutType(Integer cashOutType) {
		this.cashOutType = cashOutType;
	}
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	public Double getVirtualAmount() {
		return virtualAmount;
	}

	public void setVirtualAmount(Double virtualAmount) {
		this.virtualAmount = virtualAmount;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

}
