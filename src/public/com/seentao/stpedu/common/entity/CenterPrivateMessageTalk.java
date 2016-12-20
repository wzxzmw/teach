package com.seentao.stpedu.common.entity;



public class CenterPrivateMessageTalk {

	private Integer talkId;

	private Integer sendUserId;

	private Integer receiveUserId;

	private Integer createTime;

	private Integer privateMessageNum;

	private Integer lastPrivateMessageId;

	public Integer getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(Integer sendUserId) {
		this.sendUserId = sendUserId;
	}
	public Integer getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(Integer receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	public Integer getPrivateMessageNum() {
		return privateMessageNum;
	}

	public void setPrivateMessageNum(Integer privateMessageNum) {
		this.privateMessageNum = privateMessageNum;
	}

	public Integer getTalkId() {
		return talkId;
	}

	public void setTalkId(Integer talkId) {
		this.talkId = talkId;
	}

	public Integer getLastPrivateMessageId() {
		return lastPrivateMessageId;
	}

	public void setLastPrivateMessageId(Integer lastPrivateMessageId) {
		this.lastPrivateMessageId = lastPrivateMessageId;
	}




}
