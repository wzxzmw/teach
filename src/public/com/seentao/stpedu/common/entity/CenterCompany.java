package com.seentao.stpedu.common.entity;


public class CenterCompany {

	private Integer dataId;
	
	private String companyId;
	
	private Integer praiseNum;
	
	public CenterCompany(String companyId){
		this.companyId = companyId;
	}
	public CenterCompany(){
		
	}

	public Integer getDataId() {
		return dataId;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public Integer getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(Integer praiseNum) {
		this.praiseNum = praiseNum;
	}

}
