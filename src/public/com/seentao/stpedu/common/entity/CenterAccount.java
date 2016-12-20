package com.seentao.stpedu.common.entity;

public class CenterAccount {
	
	private Integer start;
	
	private Integer limit;
	
	private Integer accountId;
	
	private Integer userId;
	
	private Integer userType;
	
	private Integer createTime;
	
	private Integer accountType;
	
	private Double balance;
	
	private Double lockAmount;
	
	//批量查询参数传递
	private String ids;
	
	public CenterAccount(){
		
	}
	public CenterAccount(Integer accountType ,Integer userId,Integer userType,Integer createTime,Double balance,Double lockAmount){
		this.accountType =accountType;
		this.userId = userId;
		this.userType =userType;
		this.createTime = createTime;
		this.balance = balance;
		this.lockAmount = lockAmount;
	}
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
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
	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getLockAmount() {
		return lockAmount;
	}

	public void setLockAmount(Double lockAmount) {
		this.lockAmount = lockAmount;
	}

}
