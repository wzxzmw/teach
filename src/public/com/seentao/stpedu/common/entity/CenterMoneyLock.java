package com.seentao.stpedu.common.entity;


public class CenterMoneyLock {

	private Integer lockId;
	
	private Integer userId;
	
	private Integer createTime;
	
	private Integer lockStatus;
	
	private Integer relObjetType;
	
	private Integer relObjetId;
	
	private Integer accountId;
	
	//竞猜公布结果，数据库批量修改现金锁定表传参使用
	private String lockIds;

	public Integer getLockId() {
		return lockId;
	}

	public void setLockId(Integer lockId) {
		this.lockId = lockId;
	}
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	public Integer getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
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
	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getLockIds() {
		return lockIds;
	}

	public void setLockIds(String lockIds) {
		this.lockIds = lockIds;
	}

}
