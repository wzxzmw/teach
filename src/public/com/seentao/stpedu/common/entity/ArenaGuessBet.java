package com.seentao.stpedu.common.entity;


public class ArenaGuessBet {
	private Integer start;
	
	private Integer limit;
	
	private Integer betId;
	
	private Integer guessId;
	
	private Integer userId;
	
	private Double amount;
	
	private Integer position;
	
	private Integer lockId;
	
	private Integer betResult;
	
	private Double bonus;
	
	private Integer betTime;

	
	//数据库查询字段
	private String guessIds;
	
	
	public String getGuessIds() {
		return guessIds;
	}

	public void setGuessIds(String guessIds) {
		this.guessIds = guessIds;
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

	public Integer getBetId() {
		return betId;
	}

	public void setBetId(Integer betId) {
		this.betId = betId;
	}
	public Integer getGuessId() {
		return guessId;
	}

	public void setGuessId(Integer guessId) {
		this.guessId = guessId;
	}
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	 
	public Integer getLockId() {
		return lockId;
	}

	public void setLockId(Integer lockId) {
		this.lockId = lockId;
	}
	public Integer getBetResult() {
		return betResult;
	}

	public void setBetResult(Integer betResult) {
		this.betResult = betResult;
	}
	public Double getBonus() {
		return bonus;
	}

	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}
	public Integer getBetTime() {
		return betTime;
	}

	public void setBetTime(Integer betTime) {
		this.betTime = betTime;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

}
