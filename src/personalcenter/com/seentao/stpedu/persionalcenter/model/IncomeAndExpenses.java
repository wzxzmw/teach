package com.seentao.stpedu.persionalcenter.model;

public class IncomeAndExpenses {
	
	private int incomeAndExpensesId;
	private String incomeAndExpensesNo;
	private String iaeCreateDate;
	private int iaeType;
	private String iaeName;
	private double iaeValue;
	private int iaeStatus;
	private int iaeObjectType;

	public int getIncomeAndExpensesId() {
		return incomeAndExpensesId;
	}

	public void setIncomeAndExpensesId(int incomeAndExpensesId) {
		this.incomeAndExpensesId = incomeAndExpensesId;
	}

	public String getIncomeAndExpensesNo() {
		return incomeAndExpensesNo;
	}

	public void setIncomeAndExpensesNo(String incomeAndExpensesNo) {
		this.incomeAndExpensesNo = incomeAndExpensesNo;
	}

	public String getIaeCreateDate() {
		return iaeCreateDate;
	}

	public void setIaeCreateDate(String iaeCreateDate) {
		this.iaeCreateDate = iaeCreateDate;
	}

	public int getIaeType() {
		return iaeType;
	}

	public void setIaeType(int iaeType) {
		this.iaeType = iaeType;
	}

	public String getIaeName() {
		return iaeName;
	}

	public void setIaeName(String iaeName) {
		this.iaeName = iaeName;
	}

	public double getIaeValue() {
		return iaeValue;
	}

	public void setIaeValue(double iaeValue) {
		this.iaeValue = iaeValue;
	}

	public int getIaeStatus() {
		return iaeStatus;
	}

	public void setIaeStatus(int iaeStatus) {
		this.iaeStatus = iaeStatus;
	}

	public int getIaeObjectType() {
		return iaeObjectType;
	}

	public void setIaeObjectType(int iaeObjectType) {
		this.iaeObjectType = iaeObjectType;
	}

}
