package com.seentao.stpedu.common.entity;


public class CenterConvert {

	private Integer convertId;
	
	private String serialNumber;
	
	private Integer createUserId;
	
	private Integer createTime;
	
	private Double virtualAmount;
	
	private String remarks;
	
	private Integer convertType;
	

	public Integer getConvertId() {
		return convertId;
	}

	public void setConvertId(Integer convertId) {
		this.convertId = convertId;
	}
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
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
	public Double getVirtualAmount() {
		return virtualAmount;
	}

	public void setVirtualAmount(Double virtualAmount) {
		this.virtualAmount = virtualAmount;
	}
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getConvertType() {
		return convertType;
	}

	public void setConvertType(Integer convertType) {
		this.convertType = convertType;
	}

}
