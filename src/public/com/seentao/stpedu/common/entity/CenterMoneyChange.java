package com.seentao.stpedu.common.entity;


public class CenterMoneyChange {

	private Integer changeId;
	
	private Integer accountId;
	
	private Integer changeTime;
	
	private Integer lockId;
	
	private Double amount;
	
	private Integer changeType;
	
	private Integer relObjetType;
	
	private Integer relObjetId;
	
	private String serialNumber;//交易流水号
	
	private Integer status;//状态
	
	
	
	public CenterMoneyChange() {
		super();
	}


	/**
	 * 构造对象
	 * @param accountId			账户id
	 * @param changeTime		变动时间
	 * @param lockId			锁定id
	 * @param amount			变动金额
	 * @param changeType		变动类型：1:收入，2:支出，3:提现。
	 * @param relObjetType		关联对象类型。1.竞猜，2:红包。
	 * @param relObjetId		关联对象ID
	 * 少一个流水号
	 */
	public CenterMoneyChange(Integer accountId, Integer changeTime, Integer lockId, 
			Double amount, Integer relObjetType, Integer changeType, Integer relObjetId,Integer status, String serialNumber){
		this.accountId = accountId;
		this.changeTime = changeTime;
		this.lockId = lockId;
		this.amount = amount;
		this.changeType = changeType;
		this.relObjetType = relObjetType;
		this.relObjetId = relObjetId;
		this.status = status;
		this.serialNumber = serialNumber;
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getChangeId() {
		return changeId;
	}

	public void setChangeId(Integer changeId) {
		this.changeId = changeId;
	}
	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public Integer getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Integer changeTime) {
		this.changeTime = changeTime;
	}
	public Integer getLockId() {
		return lockId;
	}

	public void setLockId(Integer lockId) {
		this.lockId = lockId;
	}
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getChangeType() {
		return changeType;
	}

	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}
	public Integer getRelObjetType() {
		return relObjetType;
	}

	public void setRelObjetType(Integer relObjetType) {
		this.relObjetType = relObjetType;
	}
	public Integer getRelObjetId() {
		return relObjetId;
	}

	public void setRelObjetId(Integer relObjetId) {
		this.relObjetId = relObjetId;
	}

}
