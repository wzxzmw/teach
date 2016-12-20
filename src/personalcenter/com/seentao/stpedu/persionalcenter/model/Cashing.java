package com.seentao.stpedu.persionalcenter.model;

public class Cashing {
	private long cashingId;
	private String cCreateDate;
	private double cashingFLevelAccount;
	private int cashingStatus;

	public long getCashingId() {
		return cashingId;
	}

	public void setCashingId(long cashingId) {
		this.cashingId = cashingId;
	}

	public String getcCreateDate() {
		return cCreateDate;
	}

	public void setcCreateDate(String cCreateDate) {
		this.cCreateDate = cCreateDate;
	}

	public double getCashingFLevelAccount() {
		return cashingFLevelAccount;
	}

	public void setCashingFLevelAccount(double cashingFLevelAccount) {
		this.cashingFLevelAccount = cashingFLevelAccount;
	}

	public int getCashingStatus() {
		return cashingStatus;
	}

	public void setCashingStatus(int cashingStatus) {
		this.cashingStatus = cashingStatus;
	}

}
