package com.seentao.stpedu.common.entity;


public class PublicRegion {

	private Integer regionId;
	
	private String regionName;
	
	private String regionAllName;
	
	private Integer parentId;
	
	private Integer regionType;
	
	private String zip;
	

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getRegionAllName() {
		return regionAllName;
	}

	public void setRegionAllName(String regionAllName) {
		this.regionAllName = regionAllName;
	}
	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getRegionType() {
		return regionType;
	}

	public void setRegionType(Integer regionType) {
		this.regionType = regionType;
	}
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

}
