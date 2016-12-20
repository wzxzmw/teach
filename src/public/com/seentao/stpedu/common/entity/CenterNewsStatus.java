package com.seentao.stpedu.common.entity;


public class CenterNewsStatus {

	private Integer statusId;
	
	private Integer userId;
	
	private Integer isNewPrivateMessage;
	
	private Integer isNewAttention;
	
	private Integer isNewQuestionInvite;
	
	private Integer isNewGameInvite;
	
	private Integer isNewSysNews;
	

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getIsNewPrivateMessage() {
		return isNewPrivateMessage;
	}

	public void setIsNewPrivateMessage(Integer isNewPrivateMessage) {
		this.isNewPrivateMessage = isNewPrivateMessage;
	}
	public Integer getIsNewAttention() {
		return isNewAttention;
	}

	public void setIsNewAttention(Integer isNewAttention) {
		this.isNewAttention = isNewAttention;
	}
	public Integer getIsNewQuestionInvite() {
		return isNewQuestionInvite;
	}

	public void setIsNewQuestionInvite(Integer isNewQuestionInvite) {
		this.isNewQuestionInvite = isNewQuestionInvite;
	}
	public Integer getIsNewGameInvite() {
		return isNewGameInvite;
	}

	public void setIsNewGameInvite(Integer isNewGameInvite) {
		this.isNewGameInvite = isNewGameInvite;
	}
	public Integer getIsNewSysNews() {
		return isNewSysNews;
	}

	public void setIsNewSysNews(Integer isNewSysNews) {
		this.isNewSysNews = isNewSysNews;
	}

}
