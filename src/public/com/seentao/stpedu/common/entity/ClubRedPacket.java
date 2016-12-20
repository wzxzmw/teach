package com.seentao.stpedu.common.entity;

import java.util.List;

public class ClubRedPacket {

	private Integer redPacketId;
	
	private Integer clubId;
	
	private Integer userType;
	
	private String message;
	
	private Integer everybodyNum;
	
	private Integer type;
	
	private Integer createUserId;
	
	private Integer createTime;
	
	private List<ClubRelRedPacketMember> receiveUserList;//接收红包的人list
	
	public List<ClubRelRedPacketMember> getReceiveUserList() {
		return receiveUserList;
	}

	public void setReceiveUserList(List<ClubRelRedPacketMember> receiveUserList) {
		this.receiveUserList = receiveUserList;
	}
	
	public Integer getRedPacketId() {
		return redPacketId;
	}

	public void setRedPacketId(Integer redPacketId) {
		this.redPacketId = redPacketId;
	}

	public Integer getClubId() {
		return clubId;
	}

	public void setClubId(Integer clubId) {
		this.clubId = clubId;
	}
	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getEverybodyNum() {
		return everybodyNum;
	}

	public void setEverybodyNum(Integer everybodyNum) {
		this.everybodyNum = everybodyNum;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

}
