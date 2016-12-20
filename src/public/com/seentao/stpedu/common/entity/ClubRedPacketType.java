package com.seentao.stpedu.common.entity;


public class ClubRedPacketType {

	private Integer typeId;
	
	private String typeName;
	
	private String imageUrl;
	
	private Integer redPacketCount;//拥有的数量

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getRedPacketCount() {
		return redPacketCount;
	}

	public void setRedPacketCount(Integer redPacketCount) {
		this.redPacketCount = redPacketCount;
	}

}
