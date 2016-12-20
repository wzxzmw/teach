package com.seentao.stpedu.common.entity;
/** 
* @author yy
* @date 2016年7月1日 上午9:41:33 
* 获取红包信息
* 返回前台实体
*/
public class ResRedPacket {
	private String redPacketId;//红包id
	
	private String rpCreaterId;//红包的发布者id
	
	private String rpCreaterName;//红包的发布者姓名
	
	private String rpCreaterNickname;//红包的发布者昵称
	
	private String rpCreaterHeadLink;//红包的发布者头像
	
	private Integer rpClubRole;//红包发布者的俱乐部模块角色
	
	private String redPacketContent;//红包的内容
	
	private String rpCreateDate;//红包发布时间的时间戳
	
	private String rpReceiverId;//红包的接收者id
	
	private String rpReceiverName;//红包的接收者姓名
	
	private String rpReceiverNickname;//红包的接收者昵称
	
	private String rpReceiverHeadLink;//红包接收者的头像
	
	private Integer rpReceiverClubRole;//红包接收者的俱乐部模块角色
	
	private Integer redPacketType;//红包类型
	
	private Integer redPacketCount;//数量

	public String getRedPacketId() {
		return redPacketId;
	}

	public void setRedPacketId(String redPacketId) {
		this.redPacketId = redPacketId;
	}

	public String getRpCreaterId() {
		return rpCreaterId;
	}

	public void setRpCreaterId(String rpCreaterId) {
		this.rpCreaterId = rpCreaterId;
	}

	public String getRpCreaterName() {
		return rpCreaterName;
	}

	public void setRpCreaterName(String rpCreaterName) {
		this.rpCreaterName = rpCreaterName;
	}

	public String getRpCreaterNickname() {
		return rpCreaterNickname;
	}

	public void setRpCreaterNickname(String rpCreaterNickname) {
		this.rpCreaterNickname = rpCreaterNickname;
	}

	public String getRpCreaterHeadLink() {
		return rpCreaterHeadLink;
	}

	public void setRpCreaterHeadLink(String rpCreaterHeadLink) {
		this.rpCreaterHeadLink = rpCreaterHeadLink;
	}

	public String getRpReceiverHeadLink() {
		return rpReceiverHeadLink;
	}

	public void setRpReceiverHeadLink(String rpReceiverHeadLink) {
		this.rpReceiverHeadLink = rpReceiverHeadLink;
	}

	public String getRedPacketContent() {
		return redPacketContent;
	}

	public void setRedPacketContent(String redPacketContent) {
		this.redPacketContent = redPacketContent;
	}

	public String getRpCreateDate() {
		return rpCreateDate;
	}

	public void setRpCreateDate(String rpCreateDate) {
		this.rpCreateDate = rpCreateDate;
	}

	public String getRpReceiverId() {
		return rpReceiverId;
	}

	public void setRpReceiverId(String rpReceiverId) {
		this.rpReceiverId = rpReceiverId;
	}

	public String getRpReceiverName() {
		return rpReceiverName;
	}

	public void setRpReceiverName(String rpReceiverName) {
		this.rpReceiverName = rpReceiverName;
	}

	public String getRpReceiverNickname() {
		return rpReceiverNickname;
	}

	public void setRpReceiverNickname(String rpReceiverNickname) {
		this.rpReceiverNickname = rpReceiverNickname;
	}

	public Integer getRpClubRole() {
		return rpClubRole;
	}

	public void setRpClubRole(Integer rpClubRole) {
		this.rpClubRole = rpClubRole;
	}

	public Integer getRpReceiverClubRole() {
		return rpReceiverClubRole;
	}

	public void setRpReceiverClubRole(Integer rpReceiverClubRole) {
		this.rpReceiverClubRole = rpReceiverClubRole;
	}

	public Integer getRedPacketType() {
		return redPacketType;
	}

	public void setRedPacketType(Integer redPacketType) {
		this.redPacketType = redPacketType;
	}

	public Integer getRedPacketCount() {
		return redPacketCount;
	}

	public void setRedPacketCount(Integer redPacketCount) {
		this.redPacketCount = redPacketCount;
	}
	
}


