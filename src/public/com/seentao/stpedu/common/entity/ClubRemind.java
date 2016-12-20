package com.seentao.stpedu.common.entity;


public class ClubRemind {

	private Integer remindId;
	
	private Integer clubId;
	
	private String content;
	
	private Integer createTime;
	
	private Integer createUserId;
	

	public Integer getRemindId() {
		return remindId;
	}

	public void setRemindId(Integer remindId) {
		this.remindId = remindId;
	}
	public Integer getClubId() {
		return clubId;
	}

	public void setClubId(Integer clubId) {
		this.clubId = clubId;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

}
